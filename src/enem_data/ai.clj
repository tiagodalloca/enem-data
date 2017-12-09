(ns enem-data.ai
  (:require [jutsu.ai.core :as ai])

  (:import [org.datavec.api.records.reader.impl.csv CSVRecordReader]
           [org.datavec.api.split FileSplit]
           [org.datavec.api.util ClassPathResource]
           [org.deeplearning4j.optimize.listeners ScoreIterationListener]
           [org.nd4j.linalg.dataset SplitTestAndTrain DataSet]
           [org.nd4j.linalg.dataset.api.preprocessor
            NormalizerStandardize
            DataNormalization]
           [org.deeplearning4j.eval Evaluation]
           [org.deeplearning4j.datasets.datavec RecordReaderDataSetIterator]))

(def data-path "MICRODADOS_ENEM_2016-PROCESSED-SHUF.csv")

(def net-config
  [:seed 123
   :optimization-algo :sgd
   :learning-rate 0.1
   :regularization true
   :l2 1e-4
   :layers [[:dense [:n-in 49 :n-out 80 :activation :relu :weight-init :xavier]]
            [:dense [:n-in 80 :n-out 30 :activation :relu :weight-init :xavier]]
            [:output :negative-log-likelihood
             [:n-in 30 :n-out 5 :activation :softmax :weight-init :xavier]]]
   :pretrain false
   :backprop true])

(def net (ai/network net-config))

(defn do-yer-thing []
  (let [reader (CSVRecordReader.)
        _ (.initialize reader (FileSplit. (.getFile (ClassPathResource. data-path))))
        iterator (RecordReaderDataSetIterator. reader 5000 45 49 true)
        all-data (.next iterator)
        _ (.shuffle all-data)
        test-and-train (.splitTestAndTrain all-data 0.75)
        training-data (.getTrain test-and-train)
        test-data (.getTest test-and-train)
        normalizer (NormalizerStandardize.)]
    (.fit normalizer training-data)
    (.transform normalizer training-data)
    (.transform normalizer test-data)
    (.init net)
    (.setListeners net (ScoreIterationListener. 15000))
    (.fit net training-data)
    
    (let [eval (Evaluation. 3)
          output (.output net (.getFeatureMatrix test-data))]
      (.eval eval (.getLabels test-data) output)
      (println (.stats eval)))))

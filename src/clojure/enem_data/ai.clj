(ns enem-data.ai
  ;; (:require [jutsu.ai.core :as ai])

  (:import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
           [org.deeplearning4j.util ModelSerializer]
           [org.datavec.api.records.reader.impl.csv CSVRecordReader]
           [org.datavec.api.split FileSplit NumberedFileInputSplit]
           [org.datavec.api.util ClassPathResource]
           [org.deeplearning4j.optimize.listeners ScoreIterationListener]
           [org.nd4j.linalg.dataset SplitTestAndTrain DataSet]
           [org.nd4j.linalg.dataset.api.preprocessor
            NormalizerStandardize
            DataNormalization]
           [org.deeplearning4j.eval Evaluation RegressionEvaluation]
           [org.deeplearning4j.datasets.datavec RecordReaderDataSetIterator]

           org.deeplearning4j.api.storage.StatsStorage
           org.deeplearning4j.ui.api.UIServer
           org.deeplearning4j.ui.stats.StatsListener
           org.deeplearning4j.ui.storage.InMemoryStatsStorage

           [enemdata NeuralNet]))

(def all-path "MICRODADOS_ENEM_2016-PROCESSED.csv/")

(def net-config
  [:seed 123
   :iterations 6000
   :optimization-algo :sgd
   :weight-init :xavier
   :learning-rate 0.01
   ;; :regularization true
   ;; :l2 1e-4
   :layers [[:dense [:n-in 45 :n-out 80 :activation :tanh]]
            ;; [:dense [:n-in 80 :n-out 80 :activation :relu :weight-init :xavier]]
            [:dense [:n-in 80 :n-out 30 :activation :tanh]]
            [:output :mse
             [:n-in 30 :n-out 5 :activation :identity]]]
   :pretrain false
   :backprop true])

(comment (def net (ai/network net-config)))

(set! NeuralNet/momentum 0.5)
(set! NeuralNet/learning_rate 1E-6)
(def net (MultiLayerNetwork. (NeuralNet/getNetConfiguration)))

(defn set-ui [net]
  (let [ui-server (UIServer/getInstance)
        stats-storage (InMemoryStatsStorage.)] 
    (.setListeners net (into-array `(~(StatsListener. stats-storage))))
    (.attach ui-server stats-storage)
    net))

(defn test-net
  ([net test-iterator]
   (let [eval (.evaluateRegression net test-iterator)]
     (println "Evaluated net")
     (println (.stats eval))))
  ([file-name]
   (let [net (ModelSerializer/restoreMultiLayerNetwork
              (java.io.File. file-name))
         test-reader (CSVRecordReader.)
         _ (.initialize test-reader
                        (NumberedFileInputSplit.
                         (str "resources/" all-path "part-%05d")
                         151 169)) 
         test-iterator (RecordReaderDataSetIterator.
                        test-reader 1000 45 49 true)]
     (.init net)
     (test-net net test-iterator))))

(defn do-yer-thing []
  (let [train-reader (CSVRecordReader. 0 \,)
        _ (.initialize train-reader
                       (NumberedFileInputSplit.
                        (str "resources/" all-path "part-%05d")
                        0 150))
        test-reader (CSVRecordReader.)
        _ (.initialize test-reader
                       (NumberedFileInputSplit.
                        (str "resources/" all-path "part-%05d")
                        151 169))
        train-iterator (RecordReaderDataSetIterator.
                        train-reader 1000 45 49 true)
        test-iterator (RecordReaderDataSetIterator.
                       test-reader 1000 45 49 true)
        epochs 10
        ready-for-more true]
    (println "Initializing net...")
    (.init net)
    (println "Initialized net")

    (set-ui net)
    (println "UI set")

    ;; (ai/train-net! net 1 train-iterator)

    (doseq [n (range 0 epochs)]
      (.reset train-iterator)
      (println "train-iterator reset")
      (.fit net train-iterator))
    (println "Trained net")

    (ModelSerializer/writeModel
     net (java.io.File. "resources/nets/enem-net-8") ready-for-more)
    (println "Saved net")
    (test-net net test-iterator)))


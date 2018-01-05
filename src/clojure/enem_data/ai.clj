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

           [enemdata NeuralNet]))

(def all-path "MICRODADOS_ENEM_2016-PROCESSED.csv/")

(def training-path "MICRODADOS_ENEM_2016-TRAINING/")

(def test-path "MICRODADOS_ENEM_2016-TESTING/")

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

;; (def net (ai/network net-config))

(def net (MultiLayerNetwork. (NeuralNet/getNetConfiguration)))

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
        train-iterator (RecordReaderDataSetIterator. train-reader 1000 45 49 true)
        test-iterator (RecordReaderDataSetIterator. test-reader 1000 45 49 true)
        epochs 10
        ready-for-more true]
    (println "Initializing net...")
    (.init net)
    (println "Initialized net")
    ;; (.setListeners net (ScoreIterationListener. 15000))
    
    ;; (ai/train-net! net 1 train-iterator)
    (doseq [n (range 0 epochs)]
      (.reset train-iterator)
      (println "train-iterator reset")
      (.fit net train-iterator))
    (println "Trained net")

    (ModelSerializer/writeModel net (java.io.File. "enem-net") ready-for-more)
    (println "Saved net")

    (let [eval (.evaluateRegression net test-iterator)
          ;; eval (RegressionEvaluation. (doto (java.util.List.)
          ;;                               (.add "NU_NOTA_CN")
          ;;                               (.add "NU_NOTA_CH")
          ;;                               (.add "NU_NOTA_LC")
          ;;                               (.add "NU_NOTA_MT")
          ;;                               (.add "NU_NOTA_REDACAO")))
          ]
      (println "Evaluated net")
      (println (.stats eval)))))

(defn test-net
  ([net test-iterator]
   (let [eval (.evaluateRegression net test-iterator)]
     (println "Evaluated net")
     (println (.stats eval))))
  ([]
   (let [net (ModelSerializer/restoreMultiLayerNetwork
              (java.io.File. "enem-net"))
         test-reader (CSVRecordReader.)
         _ (.initialize test-reader
                        (NumberedFileInputSplit.
                         (str "resources/" all-path "part-%05d")
                         151 169)) 
         test-iterator (RecordReaderDataSetIterator.
                        test-reader 1000 45 49 true)]
     (.init net)
     (test-net net test-iterator))))

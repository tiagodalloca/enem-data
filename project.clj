(defproject enem-data "0.1.0-SNAPSHOT"
  :description "Some tools to handle Enem data" 
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [mount "0.1.11"]

                 [hswick/jutsu.ai "0.1.1"]

                 [org.nd4j/nd4j-native-platform "0.8.0"]
                 [org.datavec/datavec-api "0.8.0"]
                 [org.deeplearning4j/dl4j-spark_2.11 "0.8.0_spark_2"]
                 [org.datavec/datavec-spark_2.11 "0.8.0_spark_2"]]

  :source-paths ["src"]

  :main ^:skip-aot enem-data.core

  :jvm-opts ["-Xms1024m" "-Xmx8000m"]
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
                   :source-paths ["dev"]
                   :repl-options {:init-ns user}}
             :uberjar {:aot :all}})

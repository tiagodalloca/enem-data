(defproject enem-data "0.1.0-SNAPSHOT"
  :description "Some tools to handle Enem data" 
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [mount "0.1.11"]

                 ;; [hswick/jutsu.ai "0.1.1"]

                 [org.deeplearning4j/deeplearning4j-ui_2.11 "0.9.2-SNAPSHOT"]
                 [org.nd4j/nd4j-native-platform "0.9.2-SNAPSHOT"]
                 [org.datavec/datavec-api "0.9.2-SNAPSHOT"]
                 [org.deeplearning4j/dl4j-spark_2.11 "0.9.2_spark_2-SNAPSHOT"]
                 [org.datavec/datavec-spark_2.11 "0.9.2_spark_2-SNAPSHOT"]]

  :repositories [["snapshots" "https://oss.sonatype.org/content/repositories/snapshots"]]

  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]

  ;; :javac-options ["-target" "1.9" "-source" "1.9"]

  ;; :prep-tasks [["javac"] ["compile"]]

  :jvm-opts ["-Xms1024m" "-Xmx8000m"]
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
                   ;; :source-paths ["dev"]
                   :repl-options {:init-ns user}}
             :uberjar {:aot :all}})

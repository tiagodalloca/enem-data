(defproject enem-data "0.1.0-SNAPSHOT"
  :description "Some tools to handle Enem data" 
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.4"]]
  :main ^:skip-aot enem-data.core

  :jvm-opts ["-Xms1024m" "-Xmx4096m"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

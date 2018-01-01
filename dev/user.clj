(ns user
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :as repl]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]

            [enem-data.ai :as ed-ai]
            [enem-data.pre.vectorization :as ed-vec]))


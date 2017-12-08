(ns enem-data.sketch.s15-1)

(def data-file-name "MICRODADOS_ENEM_2015.csv")

(def data-file-path (str "resources/" data-file-name))

(def labels (with-open [reader (io/reader data-file-path)]
              (->> reader csv/read-csv (take 1) first)))

(defn find-label [row label]
  (let [n (.indexOf labels label)]
    (row n)))

(defn find-labels [row & labs]
  (mapv (fn [label] (row (.indexOf labels label))) labs))

(defn find-nth [n]
  (with-open [reader (io/reader data-file-path)]
    (nth (drop 1 (csv/read-csv reader)) n)))

(defn find-by-k-pred [k pred]
  (with-open [reader (io/reader data-file-path)]
    (let [label-i (.indexOf labels k)]
      (doall (filter #(pred (% label-i))
                     (drop 1 (csv/read-csv reader)))))))

(defn average
  [numbers]
  (/ (apply + numbers) (count numbers)))

;; (def the-70s (first (find-by-k-pred "NU_IDADE" (partial = "70"))))

;; (def lorenzo (find-by-k-pred "NU_INSCRICAO"
;;                              (partial = "?")))

;; (find-label the-70s "NU_NOTA_REDACAO")
;; (find-label the-70s "IN_SURDEZ")



;; (def lorenzo
;;   (->> (find-by-k-pred "NU_INSCRICAO" (partial = "151004872116"))
;;        first))

;; (def cotucas (find-by-k-pred "CO_ESCOLA" (partial = "35045949")))

;; (average (map (fn [x]
;;                 (some-> (x (.indexOf labels
;;                                      "NU_NOTA_MT"))
;;                         (#(if (empty? %) "0" %))
;;                         Double.))
;;               cotucas))

;; (def inds (filter (fn [x]
;;                     (= ["Indaiatuba"]
;;                        (find-labels
;;                         x
;;                         "NO_MUNICIPIO_RESIDENCIA"
;;                         )))
;;                   cotucas))


;; (def m (first (filter (fn [x]
;;                         (= ["17"]
;;                            (find-labels
;;                             x
;;                             "NU_IDADE"
;;                             )))
;;                       inds)))

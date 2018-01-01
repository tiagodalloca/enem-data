(ns enem-data.pre.vectorization
  (:import [org.apache.spark SparkConf]
           [org.apache.spark.api.java JavaRDD]
           [org.apache.spark.api.java JavaSparkContext]
           [org.datavec.api.records.reader.impl.csv CSVRecordReader]
           [org.datavec.api.transform TransformProcess]
           [org.datavec.api.transform.schema Schema]
           [org.datavec.api.writable Writable]
           [org.datavec.spark.transform SparkTransformExecutor]
           [org.datavec.spark.transform.misc StringToWritablesFunction]
           [org.datavec.spark.transform.misc WritablesToStringFunction]
           [org.datavec.api.transform.transform.column ReorderColumnsTransform]))

(def data-name "MICRODADOS_ENEM_2016")

(def data-file-name (str data-name ".csv"))

(def data-file-path (str "resources/" data-file-name))

(def default-output-path (str "resources/" data-name "-PROCESSED.csv"))

(defn- schema-builder []
  (org.datavec.api.transform.schema.Schema$Builder.))

(defn- transform-process-builder [built-schema]
  (org.datavec.api.transform.TransformProcess$Builder. built-schema))

(def labels
  (apply
   array-map
   ["NU_INSCRICAO" nil
    "NU_ANO" nil
    "CO_MUNICIPIO_RESIDENCIA" nil
    "NO_MUNICIPIO_RESIDENCIA" nil 
    "CO_UF_RESIDENCIA" nil
    "SG_UF_RESIDENCIA" nil
    "NU_IDADE" :int
    "TP_SEXO" ["M" "F"]
    "TP_ESTADO_CIVIL" ["0" "1" "2" "3"]
    "TP_COR_RACA"  ["0" "1" "2" "3" "4" "5" "6"]
    "TP_NACIONALIDADE" nil
    "CO_MUNICIPIO_NASCIMENTO" nil
    "NO_MUNICIPIO_NASCIMENTO" nil
    "CO_UF_NASCIMENTO" nil
    "SG_UF_NASCIMENTO" nil
    "TP_ST_CONCLUSAO" ["1" "2" "3" "4"]
    "TP_ANO_CONCLUIU" nil
    "TP_ESCOLA"  ["1" "2" "3" "4"]
    "TP_ENSINO"  ["1" "2" "3"]
    "IN_TREINEIRO" ["0" "1"]
    "CO_ESCOLA" nil
    "CO_MUNICIPIO_ESC" nil
    "NO_MUNICIPIO_ESC" nil
    "CO_UF_ESC" nil
    "SG_UF_ESC" nil
    "TP_DEPENDENCIA_ADM_ESC" nil
    "TP_LOCALIZACAO_ESC" nil
    "TP_SIT_FUNC_ESC" nil
    "IN_BAIXA_VISAO" nil
    "IN_CEGUEIRA" nil
    "IN_SURDEZ" nil
    "IN_DEFICIENCIA_AUDITIVA" nil
    "IN_SURDO_CEGUEIRA" nil
    "IN_DEFICIENCIA_FISICA" nil
    "IN_DEFICIENCIA_MENTAL" nil
    "IN_DEFICIT_ATENCAO" nil
    "IN_DISLEXIA" nil
    "IN_DISCALCULIA" nil
    "IN_AUTISMO" nil
    "IN_VISAO_MONOCULAR" nil
    "IN_OUTRA_DEF" nil
    "IN_SABATISTA" nil
    "IN_GESTANTE" nil
    "IN_LACTANTE" nil
    "IN_IDOSO" nil
    "IN_ESTUDA_CLASSE_HOSPITALAR" nil
    "IN_SEM_RECURSO" nil
    "IN_BRAILLE" nil
    "IN_AMPLIADA_24" nil
    "IN_AMPLIADA_18" nil
    "IN_LEDOR" nil
    "IN_ACESSO" nil
    "IN_TRANSCRICAO" nil
    "IN_LIBRAS" nil
    "IN_LEITURA_LABIAL" nil
    "IN_MESA_CADEIRA_RODAS" nil
    "IN_MESA_CADEIRA_SEPARADA" nil
    "IN_APOIO_PERNA" nil
    "IN_GUIA_INTERPRETE" nil
    "IN_MACA" nil
    "IN_COMPUTADOR" nil
    "IN_CADEIRA_ESPECIAL" nil
    "IN_CADEIRA_CANHOTO" nil
    "IN_CADEIRA_ACOLCHOADA" nil
    "IN_PROVA_DEITADO" nil
    "IN_MOBILIARIO_OBESO" nil
    "IN_LAMINA_OVERLAY" nil
    "IN_PROTETOR_AURICULAR" nil
    "IN_MEDIDOR_GLICOSE" nil
    "IN_MAQUINA_BRAILE" nil
    "IN_SOROBAN" nil
    "IN_MARCA_PASSO" nil
    "IN_SONDA" nil
    "IN_MEDICAMENTOS" nil
    "IN_SALA_INDIVIDUAL" nil
    "IN_SALA_ESPECIAL" nil
    "IN_SALA_ACOMPANHANTE" nil
    "IN_MOBILIARIO_ESPECIFICO" nil
    "IN_MATERIAL_ESPECIFICO" nil
    "IN_NOME_SOCIAL" nil
    "IN_CERTIFICADO" nil
    "NO_ENTIDADE_CERTIFICACAO" nil
    "CO_UF_ENTIDADE_CERTIFICACAO" nil
    "SG_UF_ENTIDADE_CERTIFICACAO" nil
    "CO_MUNICIPIO_PROVA" nil
    "NO_MUNICIPIO_PROVA" nil
    "CO_UF_PROVA" nil
    "SG_UF_PROVA" nil
    "TP_PRESENCA_CN" nil
    "TP_PRESENCA_CH" nil
    "TP_PRESENCA_LC" nil
    "TP_PRESENCA_MT" nil
    "CO_PROVA_CN" nil
    "CO_PROVA_CH" nil
    "CO_PROVA_LC" nil
    "CO_PROVA_MT" nil
    "NU_NOTA_CN" :double
    "NU_NOTA_CH" :double
    "NU_NOTA_LC" :double
    "NU_NOTA_MT" :double
    "TX_RESPOSTAS_CN" nil
    "TX_RESPOSTAS_CH" nil
    "TX_RESPOSTAS_LC" nil
    "TX_RESPOSTAS_MT" nil
    "TP_LINGUA" nil
    "TX_GABARITO_CN" nil
    "TX_GABARITO_CH" nil
    "TX_GABARITO_LC" nil
    "TX_GABARITO_MT" nil
    "TP_STATUS_REDACAO" nil
    "NU_NOTA_COMP1" nil
    "NU_NOTA_COMP2" nil
    "NU_NOTA_COMP3" nil
    "NU_NOTA_COMP4" nil
    "NU_NOTA_COMP5" nil
    "NU_NOTA_REDACAO" :double
    "Q001" ["A" "B" "C" "D" "E" "F" "G" "H"]
    "Q002" ["A" "B" "C" "D" "E" "F" "G" "H"]
    "Q003" ["A" "B" "C" "D" "E" "F"]
    "Q004" ["A" "B" "C" "D" "E" "F"]
    "Q005" :int
    "Q006" ["A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K" "L" "M" "N" "O" "P" "Q"]
    "Q007" ["A" "B" "C" "D"]
    "Q008" ["A" "B" "C" "D" "E"]
    "Q009" ["A" "B" "C" "D" "E"]
    "Q010" ["A" "B" "C" "D" "E"]
    "Q011" ["A" "B" "C" "D" "E"]
    "Q012" ["A" "B" "C" "D" "E"]
    "Q013" ["A" "B" "C" "D" "E"]
    "Q014" ["A" "B" "C" "D" "E"]
    "Q015" ["A" "B" "C" "D" "E"]
    "Q016" ["A" "B" "C" "D" "E"]
    "Q017" ["A" "B" "C" "D" "E"]
    "Q018" ["A" "B" "C" "D" "E"]
    "Q019" ["A" "B" "C" "D" "E"]
    "Q020" ["A" "B"]
    "Q021" ["A" "B"]
    "Q022" ["A" "B" "C" "D" "E"]
    "Q023" ["A" "B"]
    "Q024" ["A" "B" "C" "D" "E"]
    "Q025" ["A" "B"]
    "Q026" ["A" "B" "C"]
    "Q027" ["A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K" "L" "M"]
    "Q028" ["A" "B" "C" "D" "E"]
    "Q029" nil
    "Q030" nil
    "Q031" nil
    "Q032" nil
    "Q033" nil
    "Q034" nil
    "Q035" nil
    "Q036" nil
    "Q037" nil
    "Q038" nil
    "Q039" nil
    "Q040" nil
    "Q041" nil
    "Q042" ["A" "B" "C" "D" "E" "F" "G" "H"]
    "Q043" ["A" "B" "C" "D"]
    "Q044" ["A" "B" "C"]
    "Q045" ["A" "B" "C" "D"]
    "Q046" ["A" "B" "C" "D"]
    "Q047" ["A" "B" "C" "D" "E"]
    "Q048" ["A" "B" "C" "D"]
    "Q049" ["A" "B" "C"]
    "Q050" ["A" "B" "C" "D"]]))

(def important-labels
  ["NU_IDADE" "TP_SEXO" "TP_ESTADO_CIVIL" "TP_COR_RACA" "TP_ST_CONCLUSAO" "TP_ESCOLA" "TP_ENSINO" "IN_TREINEIRO" "Q001" "Q002" "Q003" "Q004" "Q005" "Q006" "Q007" "Q008" "Q009" "Q010" "Q011" "Q012" "Q013" "Q014" "Q015" "Q016" "Q017" "Q018" "Q019" "Q020" "Q021" "Q022" "Q023" "Q024" "Q025" "Q026" "Q027" "Q028" "Q042" "Q043" "Q044" "Q045" "Q046" "Q047" "Q048" "Q049" "Q050" "NU_NOTA_CN" "NU_NOTA_CH" "NU_NOTA_LC" "NU_NOTA_MT" "NU_NOTA_REDACAO"])

(defn build-schema! [schema]
  (let [sb (schema-builder)]
    (doseq [[k v] schema]
      (if v
        (cond
          (= v :int) (.addColumnInteger sb k)
          (= v :double) (.addColumnDouble sb k)
          (vector? v) (.addColumnCategorical sb k (java.util.ArrayList. (conj v ""))))
        (.addColumnString sb k)))
    (.build sb)))

(defn build-transform-process! [schema built-schema]
  (let [tpb (transform-process-builder built-schema)]
    (doseq [[k v] schema]
      (if-not v 
        (.removeColumns tpb (into-array String [k]))
        (when (vector? v)
          (.categoricalToInteger tpb (into-array String [k])))))
    (.reorderColumns tpb (into-array String important-labels))
    (.build tpb)))

(defn vectorize-enem-data!
  ([output]
   (let [schema (build-schema! labels)
         tp (build-transform-process! labels schema)
         spark-conf (SparkConf.)]
     (doto spark-conf
       (.setMaster "local[*]")
       (.setAppName "Enem Data Analysis"))
     (let [sc (JavaSparkContext. spark-conf)]
       (try (let [labels-n (reduce-kv (fn [acc _ vl] (if vl (inc acc) acc))
                                      0 labels)
                  lines (.filter
                         (.textFile sc data-file-path)
                         (reify org.apache.spark.api.java.function.Function
                           (call [this x] 
                             (let [v (clojure.string/split x #",")] 
                               (= (count v) (count labels))))))
                  all-that-data
                  (->> (CSVRecordReader.)
                       StringToWritablesFunction. 
                       (.map lines))
                  processed-data (SparkTransformExecutor/execute all-that-data tp)
                  to-save
                  (-> processed-data
                      (.map (WritablesToStringFunction. ","))
                      (.filter (reify org.apache.spark.api.java.function.Function
                                 (call [this x] 
                                   (let [v (clojure.string/split x #",")]
                                     ;; (= 50 (count v))
                                     ;; the following was not tested and
                                     ;; is subject to errors
                                     (= labels-n (count v)))))))]
              ;; (.saveAsTextFile (.coalesce to-save 1 true) output)
              (.saveAsTextFile to-save output))
            (catch Exception e
              (println e)))
       (.stop sc))))
  ([] (vectorize-enem-data! default-output-path)))

;; Schema inputDataSchema = new Schema.Builder()
;; .addColumnsString("datetime","severity","location","county","state")
;; .addColumnsDouble("lat","lon")
;; .addColumnsString("comment")
;; .addColumnCategorical("type","TOR","WIND","HAIL")
;; .build();


;; /**
;; * Define a transform process to extract lat and lon
;; * and also transform type from one of three strings
;; * to either 0,1,2
;; */


;; TransformProcess tp = new TransformProcess.Builder(inputDataSchema)
;; .removeColumns("datetime","severity","location","county","state","comment")
;; .categoricalToInteger("type")
;; .build();

;; SparkConf sparkConf = new SparkConf();
;; sparkConf.setMaster("local[*]");
;; sparkConf.setAppName("Storm Reports Record Reader Transform");
;; JavaSparkContext sc = new JavaSparkContext(sparkConf);
;; /**
;; * Get our data into a spark RDD
;; * and transform that spark RDD using our
;; * transform process
;; */

;; // read the data file
;; JavaRDD<String> lines = sc.textFile(inputPath);
;; // convert to Writable
;; JavaRDD<List<Writable>> stormReports = lines.map(new StringToWritablesFunction(new CSVRecordReader()));
;; // run our transform process
;; JavaRDD<List<Writable>> processed = SparkTransformExecutor.execute(stormReports,tp);
;; // convert Writable back to string for export
;; JavaRDD<String> toSave= processed.map(new WritablesToStringFunction(","));

;; toSave.saveAsTextFile(outputPath);

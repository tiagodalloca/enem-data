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
           [java.util List]))

(defn schema-builder []
  (org.datavec.api.transform.schema.Schema$Builder.))

(defn transform-process-builder [data-schema]
  (org.datavec.api.transform.TransformProcess$Builder. ))


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

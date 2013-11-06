(ns raml2api-blueprint.core
  (:gen-class)
  (:require [clj-yaml.core :as yaml])
  (:require [clojure.string :as string]))

(def http-methods
  ["options" "get" "head" "post" "put" "delete" "trace" "connect" "patch"])


(defn walk-responses [yaml]
  (doseq [[http-code bodies] yaml]
    (doseq [[content-type body] bodies]
      (println (format "+ Response %s (%s)\n" http-code content-type))
      )))

(defn walk-body [yaml]
  (doseq [[content-type body] yaml
          :let [schema (body :schema)]]
    (println (format "+ Request (%s)" content-type))
    (when schema
      (println "    + Schema\n")
      (doseq [line (string/split-lines schema)]
        (println "       " line))))
  (println))

(defn walk [yaml path-context]
  (doseq [[yaml-key yaml-value] yaml]
    (cond
      (= (first yaml-key) \/)
        (do (println "#" (string/join (concat path-context yaml-key)) "\n")
            (walk yaml-value (concat path-context yaml-key)))
      (some #{yaml-key} http-methods)
        (do
            (println "##" (string/upper-case yaml-key) "\n")
            (walk yaml-value (concat path-context yaml-key)))
      (= yaml-key "description")
        (println yaml-value "\n")
      (= yaml-key "body")
        (walk-body yaml-value)
      (= yaml-key "responses")
        (walk-responses yaml-value)
      :else (println "wtf" yaml-key)
      )))

(defn walk-top [yaml]
  (println "#" (yaml "title") "version" (yaml "version") "\n")
  (walk (dissoc yaml "title" "version") ()))

(defn -main
  "Convert a raml file to api-blueprint."
  [& args]
  (walk-top (yaml/parse-string (slurp (first args)) false)))

(defproject raml2api-blueprint "0.1.0-SNAPSHOT"
  :description "Convert RAML to api-blueprint"
  :url "http://github.com/radix/raml2api-blueprint"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-yaml "0.4.0"]]
  :main ^:skip-aot raml2api-blueprint.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

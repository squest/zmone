(defproject zmone "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies
  [[org.clojure/clojure "1.10.0"]
   [com.stuartsierra/component "0.4.0"]
   [com.datomic/datomic-free "0.9.5697"]
   [io.pedestal/pedestal.service "0.5.5"]
   [io.pedestal/pedestal.jetty "0.5.5"]
   [org.immutant/web "2.1.10" :exclusions [org.jboss.logging/jboss-logging
                                           ch.qos.logback/logback-classic]]
   [io.pedestal/pedestal.immutant "0.5.5" :exclusions [org.immutant/web]]
   [io.pedestal/pedestal.log "0.5.5"]
   [clj-time "0.15.1"]

   [selmer "1.12.6"
    :exclusions [com.fasterxml.jackson.dataformat/jackson-dataformat-smile
                 com.fasterxml.jackson.dataformat/jackson-dataformat-cbor
                 com.fasterxml.jackson.core/jackson-core
                 cheshire common.codec]]
   ;; additions
   [datomic-schema "1.3.0"]

   [danlentz/clj-uuid "0.1.7"]
   [me.raynes/fs "1.4.6"]
   [clj-http "3.9.1"]
   [cheshire "5.8.1"]
   [prismatic/schema "1.1.10"]
   [org.clojure/data.json "0.2.6"]
   [org.clojure/data.csv "0.1.4"]]
  :source-paths ["src" "dev"]
  :repl-options {:init-ns user}
  :main zmone.core
  :uberjar-name "wow.jar"
  :jvm-opts ["-Xms1G" "-Xmx4G" "-server" "-XX:-OmitStackTraceInFastThrow"]
  :profiles
  {:dev  {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
          :source-paths ["dev"]}
   :test {:source-paths []}})

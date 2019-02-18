(ns zmone.utils
  (:require
    [clj-time.core :as t]
    [clj-time.format :as f]
    [clojure.edn :as edn]
    [cheshire.core :refer [parse-string]]
    [clojure.string :as cs]
    [clojure.pprint :refer [pprint]]
    [clojure.java.io :as io]
    [datomic.api :as d]
    [me.raynes.fs :as fs]
    [io.pedestal.log :as log]))

(defn squuid
  []
  (d/squuid))

(defn cslurp
  "Helper function to easily slurp and read-string a file"
  [fname]
  ((comp edn/read-string slurp) fname))

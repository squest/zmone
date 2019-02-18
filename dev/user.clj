(ns user
  (:require
    [clojure.tools.namespace.repl :as ns-repl]))

(defn dev
  []
  (require 'dev)
  (in-ns 'dev))

(ns dev
  (:require
    [clojure.tools.namespace.repl :as ns-repl]
    [com.stuartsierra.component :as component]
    [clojure.edn :as edn]
    [zmone.utils :as util]
    [zmone.system :as app-system]))

(defn config []
  (util/cslurp "resources/config.edn"))

(defonce dev-system (atom nil))

(defn init []
  (reset! dev-system (app-system/init-dev-system (config))))

(defn start []
  (swap! dev-system component/start-system))

(defn go []
  (init)
  ;; (init-db)
  (start))

(defn stop []
  (swap! dev-system component/stop-system))

(defn reset []
  (stop)
  (start))

(defn refresh
  "To reload your code in the system, you have to call this first. Only then you can successfully call (go)."
  []
  (stop)
  (ns-repl/refresh))

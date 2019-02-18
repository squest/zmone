(ns zmone.pedestal.component
  "Connection between the Component framework and the Pedestal web
  application server.
  inspired by: https://github.com/stuartsierra/component.pedestal/commit/9ee90960b86f84c02ec59848c6f34bd977c5e8d0"
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [io.pedestal.http.cors :as cors]
            [io.pedestal.http.impl.servlet-interceptor :as servlet-interceptor]
            [io.pedestal.interceptor :refer [interceptor interceptor-name]]))


(defn insert-context-interceptor
  "Returns an interceptor which associates key with value in the
  Pedestal context map."
  [& kv]
  (interceptor
    {:name  ::insert-context
     :enter (fn [context]
              (apply assoc context kv))}))

(defn add-component-interceptor
  "Adds an interceptor to the pedestal-config map which associates the
  pedestal-component into the Pedestal context map. Must be called
  before io.pedestal.http/create-server."
  [pedestal-config pedestal-component]
  (update pedestal-config
          ::http/interceptors
          conj
          (apply
            insert-context-interceptor
            (-> pedestal-component
                seq flatten))))

(defrecord Pedestal [pedestal-config-fn pedestal-server config]
  component/Lifecycle
  (start [this]
    (if pedestal-server
      this
      (assoc this :pedestal-server
                  (-> (pedestal-config-fn config)
                      (add-component-interceptor this)
                      (http/create-server (constantly nil)) ; no-op init-fn
                      http/start))))
  (stop [this]
    (when pedestal-server
      (http/stop pedestal-server))
    (assoc this :pedestal-server nil)))

(defn pedestal
  "Returns a new instance of the Pedestal server component.
  pedestal-config-fn is a no-argument function which returns the
  Pedestal server configuration map, which will be passed to
  io.pedestal.http/create-server. If you want the default
  interceptors, you must call io.pedestal.http/default-interceptors
  in pedestal-config-fn.
  The Pedestal component should have dependencies (as by
  com.stuartsierra.component/using or system-using) on all components
  needed by your web application. These dependencies will be available
  in the Pedestal context map via 'context-component'.
  You can make components available to your handler functions with
  'using-component' or 'component-handler'."
  ([pedestal-config-fn config]
   (->Pedestal pedestal-config-fn nil config)))

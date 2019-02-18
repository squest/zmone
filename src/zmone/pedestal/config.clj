(ns zmone.pedestal.config
  (:require [zmone.web.routes :as webrout]
            [zmone.pedestal.interceptors :as ceptors]
            [io.pedestal.http.csrf :as csrf]
            [ring.middleware.session.cookie :as cookie]
            [io.pedestal.http :as http]
            [io.pedestal.http.route.definition.table :as table]
            [io.pedestal.http.secure-headers :as http.sh]))

(defn plain-routes
  "Coded route tables."
  [config]
  (webrout/make-routes config))

(defn common-interceptors
  "Activate or deactivate csrf"
  [config]
  (if (-> config :pedestal :csrf)
    (vec (concat ceptors/cintercept-1 [(csrf/anti-forgery {:cookie-token true})] ceptors/cintercept-2))
    (vec (concat ceptors/cintercept-1 ceptors/cintercept-2))))

(defn routes-with-common-interceptors
  "Adding common interceptor from the common interceptor vector list."
  [config]
  (letfn [(foo [it] (into (common-interceptors config) it))
          (bar [route] (update-in route [2] foo))]
    (mapv bar (plain-routes config))))

(defn app-routes
  "Building route tables to be useable by pedestal components."
  [config]
  (->> config
       routes-with-common-interceptors
       (table/table-routes {})))


(defn pedestal-config
  "Pedestal config mapping"
  [config]
  {::http/host           (-> config :pedestal :host)
   ::http/port           (-> config :pedestal :port)
   ::http/type           (-> config :pedestal :http-type)
   ::http/join?          (-> config :pedestal :join?)
   ::http/resource-path  (-> config :pedestal :resource-path)
   ::http/enable-session {:store       (cookie/cookie-store
                                         {:key (-> config :cookie :cookie-key)})
                          :cookie-name (-> config :cookie :cookie-name)}
   ;; :cookie-attrs (-> config :cookie :cookie-attrs)
   ::http/routes         (app-routes config)
   ::http/secure-headers {:content-security-policy-settings
                          (http.sh/content-security-policy-header "object-src 'none';")}})

(defn pedestal-config-fn
  "Builds+verify whole shebang so it would be useable as a pedestal component (->Pedestal)."
  [config]
  (http/default-interceptors (pedestal-config config)))
(ns zmone.web.routes
  "Main route hub"
  (:require
    [io.pedestal.http :as http]
    [io.pedestal.interceptor :refer [interceptor]]
    [ring.util.response :as ring-resp]
    [selmer.parser :as selmer]
    [clojure.java.io :as io]))

(def home-page
  "dummy interceptor"
  (interceptor
    {:name  ::home-page
     :enter (fn [{{:keys [session form-params] :as request} :request ;; get path-params or body-params or query-params or json-params here
                  response                                  :response
                  datomic                                   :datomic
                  :as                                       context}]
              (println form-params)
              (println session)
              (->> (selmer/render-file "public/html/index.html" {})
                   (ring-resp/response)
                   (assoc context :response)))}))
(def api-page
  "dummy interceptor"
  (interceptor
    {:name  ::api-page
     :enter (fn [{{:keys [session form-params] :as request} :request ;; get path-params or body-params or query-params or json-params here
                  response                                  :response
                  datomic                                   :datomic
                  :as                                       context}]
              (println form-params)
              (println session)
              (->> {:sabda  "this is it"
                    :gerald [12 321 1234 32]}
                   (ring-resp/response)
                   (assoc context :response)))}))

(def routes
  "initroute"
  [["/" :get [home-page] :route-name :home-page]
   ["/jojon" :get [http/json-body api-page] :route-name :api-page]])

(defn make-routes [config] routes)

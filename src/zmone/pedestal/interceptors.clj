(ns zmone.pedestal.interceptors
  (:require [zmone.utils :as util]
            [io.pedestal.http :as http]
            [io.pedestal.interceptor :refer [interceptor]]
            [io.pedestal.http.csrf :as csrf]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.ring-middlewares :as r.m]
            [ring.middleware.session.cookie :as cookie]
            [ring.util.response :as ring-resp]))

(def debug
  (interceptor
    {:name  ::debug
     :enter (fn [{{:keys [path-params session uri] :as request} :request
                  response                                      :response
                  :as                                           context}]
              (println "------------------------------")
              (println "uri -" uri)
              (println "path-params: " path-params)
              ;(println "request header: " (->> request :headers))
              context)
     :leave (fn [{{:keys [transit-params session] :as request} :request
                  response                                     :response
                  :as                                          context}]
              (println "transit-params " transit-params)
              ;(println "response body: " (->> response :body))
              (println "session: " session)
              ;(println "------------------------------")
              context)}))

(def merge-session
  (interceptor
    {:name  ::merge-session
     :leave (fn [{{:keys [session] :as request} :request
                  response                      :response
                  :as                           context}]
              (let [new-session (-> response :session)]
                (if (and (empty? new-session)
                         (map? new-session))
                  context
                  (-> context
                      (assoc-in [:response :session]
                                (merge session new-session))))))}))

(def session-authenticate
  (interceptor
    {:name  ::authenticate
     :enter (fn [{{:keys [session] :as request} :request
                  :as                           context}]
              (let [id (get session :id nil)]
                (if id
                  context
                  (let [new-id (str (util/squuid))]
                    (-> context
                        (assoc-in [:request :session :id] new-id)
                        (assoc-in [:response :session :id] new-id))))))}))

(def cintercept-1
  [debug
   http/html-body
   (body-params/body-params)])

(def cintercept-2
  [(r.m/flash)
   (r.m/session {:store (cookie/cookie-store)})
   merge-session
   session-authenticate])

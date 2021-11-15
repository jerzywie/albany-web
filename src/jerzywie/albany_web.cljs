(ns ^:figwheel-hooks jerzywie.albany-web
  (:require
   [reagent.dom :as rdom]
   [jerzywie.albany-web.home-page :as home]
   ;[jerzywie.ffa-accounts.state :as state]
   [jerzywie.albany-web.util :as util]))

(defn multiply [a b] (* a b))

(defn get-app-element []
  (util/get-element-by-id "app"))

(defn mount [el]
  (rdom/render [home/home-page] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  (println "reload at " (str (util/time-now))))

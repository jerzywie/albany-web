(ns jerzywie.albany-web.state
  (:require [cljs.pprint :refer [pprint]]
            [reagent.core :as reagent :refer [atom]] ))

;;define app state
(defonce initial-app-state {})
(defonce chart-keys [:monthly-chart :exp-chart :weekly-analysis])
(defonce app-state (atom initial-app-state))

(defn state []
  @app-state)

(defn reset-state! []
  (reset! app-state initial-app-state))

(defn add-stuff! [k v]
  (swap! app-state assoc k v))

(defn add-file-name! [file-name]
  (add-stuff! :file-name file-name))

(defn debug-app-state []
  (when ^boolean js/goog.DEBUG
    (let [state @app-state
          ]
      [:pre.tiny-words
       [:div.mb-3.d-print-none
        [:h5 "debug app state"]
        [:div (with-out-str (pprint state))]]])))

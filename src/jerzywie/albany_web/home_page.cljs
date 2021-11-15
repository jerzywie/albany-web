(ns jerzywie.albany-web.home-page
  (:require
   [jerzywie.albany-web.file-upload :as fup]
   [jerzywie.albany-web.state :as state]))

(defn home-page []
  [:div.app.container
   [:div.row
    [:header.d-flex.border-bottom.mb-3.mt-3
     [:span.fs-2 "Albany Web"]]]
   (let [{:keys [file-name]} (state/state)]
     [:div
      [:div.row.border-bottom.d-print-none
       [:div.mb-3.col
        [fup/upload-btn file-name]]
       [state/debug-app-state]]])])

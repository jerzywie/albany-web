(ns jerzywie.albany-web.file-upload
  (:require 
   [jerzywie.albany-web.state :as state]
   [jerzywie.albany-web.spreadsheet :as ss]
   [cljs.core.async :as async :refer [put! chan <!]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(def first-file
  (map (fn [e]
         (let [target (.-currentTarget e)
               file (-> target .-files (aget 0))]
           (set! (.-value target) "")
           file))))

(def extract-result
  (map #(-> %
            .-target
            .-result
            ss/raw-data->workbook
            )))

(def upload-reqs (chan 1 first-file))
(def file-reads (chan 1 extract-result))

(defn put-upload [e]
  (put! upload-reqs e))

(go-loop []
  (let [reader (js/FileReader.)
        file (<! upload-reqs)]
    (state/add-file-name! (.-name file))
    (set! (.-onload reader) #(put! file-reads %))
    (.readAsBinaryString reader file)
    (recur)))

(go-loop []
  (let [data (<! file-reads)]
    (state/add-stuff! :data data)
    (recur)))

(defn upload-btn [file-name]
  [:span.upload-label
   [:label.btn.btn-outline-success.btn-sm {:type "button"}
    [:input.d-none
     {:type "file" :accept ".xlsm" :on-change put-upload}]
    [:i.fa.fa-upload.fa-lg.pe-2]
    (or file-name "Click here to load an Albany spreadsheet")]
   (when file-name
     [:i.fa.fa-times.ps-2 {:on-click #(
                                        state/reset-state!
                                       )}])])

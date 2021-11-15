(ns jerzywie.albany-web.test-util
  (:require [goog.dom :as gdom]
            [cljs.core.async :as async :refer [put! chan <!]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(def extract-result
  (map #(-> %
            .-target
            .-result)))

(def input-elements (chan 1))
(def file-reads (chan 1 extract-result))

(defn request-input-element-value [el-name]
  (put! input-elements el-name))

(defn get-input-element [el-name]
  (gdom/getElement el-name))

(defn get-file-from-input [el]
  (-> el .-files (aget 0)))

(go-loop []
  (let [input-el (get-input-element (<! input-elements))
        file (get-file-from-input input-el)
        reader (js/FileReader.)]
    (set! (.-onload reader) #(put! file-reads %))
    (.readAsBinaryString reader file)
    (recur)))

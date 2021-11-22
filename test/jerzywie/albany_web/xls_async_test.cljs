(ns jerzywie.albany-web.xls-async-test
  (:require [jerzywie.albany-web.test-util :as test-util]
            [cljs.test :refer-macros [deftest async is]]
            [cljs.core.async :as async :refer [go <!]]
            [reagent.core :as reagent :refer [atom]]
            ["xlsx" :as xlsx]
            [goog.object]))

(deftest file-tests-async
  (let [_ (test-util/request-input-element-value "inp")]
    (async done
           (go
             (let [raw-data (<! test-util/file-reads)
                   raw-wb (xlsx/read raw-data (clj->js {:type "binary"}))
                   ;clj-wb (js->clj raw-wb)
                   raw-sheet-by-index  (.-Sheets raw-wb 0)
                   raw-sheet-by-name (.-Sheets raw-wb "first-sheet")
                   ;clj-sheet (js->clj raw-sheet-by-index :keywordize-keys true)
                   ;raw-rows (.sheet_to_json raw-sheet-by-name)
                   sheet (xlsx/utils.sheet_to_json raw-sheet-by-name)
                   ]
               (prn "raw-wb is " raw-wb)
               ;(prn "clj-wb is " clj-wb)
               ;(prn "raw-sheet-by-index" raw-sheet-by-index)
               (prn "raw-sheet-by-name" raw-sheet-by-name)
               ;(prn "clj-sheet" clj-sheet)
               ;(prn "raw-rows" raw-rows)
               (prn "sheet" sheet)
               (is (= raw-sheet-by-name raw-sheet-by-index))
               (done))))))

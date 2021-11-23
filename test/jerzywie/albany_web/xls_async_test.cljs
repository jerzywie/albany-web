(ns jerzywie.albany-web.xls-async-test
  (:require [jerzywie.albany-web.test-util :as test-util]
            [jerzywie.albany-web.spreadsheet :as ss]
            [cljs.test :refer-macros [deftest async is]]
            [cljs.core.async :as async :refer [go <!]]
            ["xlsx" :as xlsx]
            [goog.object]))

(deftest file-tests-async
  (let [_ (test-util/request-input-element-value "inp")]
    (async done
           (go
             (let [raw-data (<! test-util/file-reads)
                   raw-wb (ss/raw-data->workbook raw-data)
                   raw-sheet-by-index  (.-Sheets raw-wb 0)
                   raw-sheet-by-name (.-Sheets raw-wb "first-sheet")
                   first-sheet (ss/get-sheet raw-wb "first-sheet")
                   vals-from-rsbn (xlsx/utils.sheet_to_json (goog.object/get raw-sheet-by-name "first-sheet" ))
                   vals-from-first-sheet (ss/sheet->map first-sheet)
                   clj-vfr (js->clj vals-from-rsbn)
                   clj-vffs (js->clj vals-from-first-sheet)]
               (prn "first-sheet" first-sheet)
               (println)
               (prn "       vals-from-rsbn" vals-from-rsbn)
               (println)
               (prn "vals-from-first-sheet" vals-from-first-sheet)
               (println)
               (prn "clj-vfr " clj-vfr)
               (println)
               (prn "clj-vffs" clj-vffs)
               (is (= raw-sheet-by-name raw-sheet-by-index))
               (is (= clj-vfr clj-vffs))
               (done))))))

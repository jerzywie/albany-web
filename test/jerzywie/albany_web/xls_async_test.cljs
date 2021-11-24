(ns jerzywie.albany-web.xls-async-test
  (:require [jerzywie.albany-web.test-util :as test-util]
            [jerzywie.albany-web.spreadsheet :as ss]
            [cljs.test :refer-macros [deftest async is]]
            [cljs.core.async :as async :refer [go <!]]
            [goog.object]))

(deftest file-tests-async
  (let [_ (test-util/request-input-element-value "inp")]
    (async done
           (go
             (let [sheet-name "first-sheet"
                   raw-data (<! test-util/file-reads)
                   raw-wb (ss/raw-data->workbook raw-data)
                   selected-sheet (ss/select-sheet sheet-name raw-wb)
                   options #js {:header #js ["A" nil "C" nil nil "F"] :defval nil}
                   sheet-cols-acf (ss/sheet->map selected-sheet options)
                   sel-cols (ss/select-columns {:A :cola :E :col-e :D :d-col} selected-sheet)
                   ]
               ;(prn "selected-sheet" selected-sheet)
               (println)
               (prn "!ref value is" (goog.object/get selected-sheet "!ref"))
               (println)
               (prn "sheet cols a-c-f" sheet-cols-acf)
               (println)
               (prn "sel-cols" sel-cols)
               (done))))))

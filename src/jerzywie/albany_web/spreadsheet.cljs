(ns jerzywie.albany-web.spreadsheet
  (:require ["xlsx" :as xlsx]
            [goog.object]))

(defn raw-data->workbook [data]
  (xlsx/read data #js {:type "binary"}))

(defn select-sheet [sheet-name wb]
  (goog.object/get (.-Sheets wb) sheet-name))

(defn sheet->map [sheet options]
  (-> sheet
      (xlsx/utils.sheet_to_json options)
      (js->clj :keywordize-keys true)))

(defn alpha-keyword->int [kwd]
  (-> kwd name (.charCodeAt 0)))

(defn consecutive-col-list
  "Given a list of column keywords in any order
  return a consecutive list of keywords from smallest to largest
  e.g. (consecutive-col-list '(:B :A :D :F))
       ==>
       '(:A :B :C :D :E :F)"
  [col-keywords]
  (let [letter-codes (map alpha-keyword->int col-keywords)
        first-letter (apply min letter-codes)
        last-letter (apply max letter-codes)]
    (->> (range first-letter (inc last-letter))
         (map (comp keyword str char)))))

(defn select-columns
  "Modelled on the 'docjure' function.
  Takes a column map e.g. {:A :keyword-for-column-a :C :keyword-for-column-c}
  and a sheet."
  [column-map sheet]
  (let [all-cols (consecutive-col-list (keys column-map))
        full-col-vals (map (fn [k] (k column-map)) all-cols)]
    (-> sheet
        (xlsx/utils.sheet_to_json (clj->js {:header (vec full-col-vals) :defval nil}))
        (js->clj :keywordize-keys true))))

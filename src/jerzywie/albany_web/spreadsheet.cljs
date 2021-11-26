(ns jerzywie.albany-web.spreadsheet
  (:require ["xlsx" :as xlsx]
            [clojure.string :as s]
            [goog.object]))

(defn raw-data->workbook [data]
  (xlsx/read data #js {:type "binary"}))

(defn select-sheet [sheet-name wb]
  (goog.object/get (.-Sheets wb) sheet-name))

(defn sheet->map [options sheet]
  (-> sheet
      (xlsx/utils.sheet_to_json options)
      (js->clj :keywordize-keys true)))

(defn col-keyword->int
  "Convert a column keyword to a numerical column.
  This is 1-based, e.g.:
   :A => 1, :X => 24, :AD => 30
  NOTE: Only works for 1 or 2 letter columns
  (Recent versions of Excel go up to 3 letters)."
  [col-kwd]
  (let [col-str (s/reverse (name col-kwd))
        char0 (- (.charCodeAt col-str 0) 64)
        char1 (- (if (> (count col-str) 1) (.charCodeAt col-str 1) 64) 64)]
    (+ char0 (* char1 26))))

;;This based on code obtained from Stackoverflow
(defn int->col-keyword
  "Convert a column number to a alphabetic keyword.
  This is 1-based, e.g.:
   1 => :A, 26 => :Z, 27 => :AA"
  [num]
  (loop [n (dec num) s ()]
    (if (> n 25)
      (let [r (mod n 26)]
        (recur (dec (/ (- n r) 26)) (cons (char (+ 65 r)) s)))
           (keyword (apply str (cons (char (+ 65 n)) s))))))

(defn consecutive-col-list
  "Given a list of column keywords in any order
  return a consecutive list of keywords from smallest to largest
  e.g. (consecutive-col-list '(:B :A :D :F))
       ==>
       '(:A :B :C :D :E :F)"
  [col-keywords]
  (let [letter-codes (map col-keyword->int col-keywords)
        first-letter (apply min letter-codes)
        last-letter (apply max letter-codes)]
    (->> (range first-letter (inc last-letter))
         (map int->col-keyword))))

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

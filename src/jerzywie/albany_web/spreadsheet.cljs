(ns jerzywie.albany-web.spreadsheet
  (:require ["xlsx" :as xlsx]))

(defn raw-data->workbook [data]
  (xlsx/read data #js {:type "binary"}))

(defn get-sheet [wb sheet-name]
  (aget (.-Sheets wb) sheet-name))

(defn sheet->map [sheet]
  (xlsx/utils.sheet_to_json sheet))



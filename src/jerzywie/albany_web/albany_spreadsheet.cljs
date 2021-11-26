(ns jerzywie.albany-web.albany-spreadsheet
  (:require [jerzywie.albany-web.spreadsheet :as ss]
            [jerzywie.albany-web.config :as config]
            [goog.string :as gs]
            [goog.string.format]))

(def sheet-map {:co   "Collated Order"
                :fin  "Financials"
                :rev  "Save Revision"})

(def co-config  {:member-start :O
                 :prefix "member-"
                 :suffices ["-des" "-cost"]})

(def fin-config {:member-start :E
                 :prefix "member-"
                 :suffices ["" nil]
                 :key-col-offset 2})

(def options #js {:header "A" :defval nil})

(defn make-member-map [start-col-key member-count prefix suffices]
  (let [map-values (flatten (for [memb (range member-count)]
                              (if (seq suffices)
                                (for [suffix suffices]
                                  (if (some? suffix)
                                    (gs/format "%s%02i%s" prefix memb suffix)
                                    nil))
                                (gs/format "%s%02i" prefix memb))))
        start-key-val (ss/col-keyword->int start-col-key)
        map-keys (range start-key-val (+ start-key-val (count map-values)))]
    (apply merge
           (map (fn [k v] {(ss/int->col-keyword k) (keyword v)})
                map-keys
                map-values))))

(defmulti read-workbook
  (fn [sheet wb] (:sheet sheet)))

(defmethod read-workbook :co [{:keys [sheet]} wb]
  (when wb (let [{:keys [member-start prefix suffices]} co-config
                 common-cols {:A :code
                              :B :description
                              :C :case-size
                              :E :albany-units
                              :G :del?
                              :H :unit-cost
                              :K :vat-amount}
                 member-cols (make-member-map member-start
                                              (count (:members config/config))
                                              prefix
                                              suffices)
                 col-map (merge common-cols member-cols)]
             (->> wb
                  (ss/select-sheet (sheet sheet-map))
                  (ss/select-columns col-map)
                  (drop 2)
                  (assoc {} sheet)))))

(defmethod read-workbook :fin [{:keys [sheet]} wb]
  (when wb (let [{:keys [member-start prefix suffices key-col-offset]} fin-config
                 common-cols {:A :name :C :subname}
                 member-cols (make-member-map member-start
                                              (count (:members config/config))
                                              prefix
                                              suffices)
                 key-col-index (->> (keys member-cols)
                                    (map ss/col-keyword->int)
                                    (apply max)
                                    (+ key-col-offset))
                 key-col-map {(ss/int->col-keyword key-col-index) :key}
                 col-map (merge common-cols member-cols key-col-map)]
             (prn "key-col-index" key-col-index)
             (prn "common-cols" common-cols)
             (prn "member-cols" member-cols)
             (prn "key-col-map" key-col-map)
             (prn "col-map" col-map)
             (->> wb
                  (ss/select-sheet (sheet sheet-map))
                  (ss/select-columns col-map)
                  (take 11)
                  (assoc {} sheet)))))

(defmethod read-workbook :rev [{:keys [sheet]} wb]
  (when wb (let [col-map {:A :item :B :value}]
             (->> wb
                  (ss/select-sheet (sheet sheet-map))
                  (ss/select-columns col-map)
                  (drop 2)
                  (map (fn [{:keys [item value]}] {(keyword item) value}))
                  (apply merge)
                  (assoc {} sheet)))))

(defmethod read-workbook :default [{:keys [sheet]} wb])

(defn read-sheets [wb]
  (->> (keys sheet-map)
       (map (fn [k] (read-workbook {:sheet k} wb)))
       (apply merge)))

(ns jerzywie.albany-web.spreadsheet-tests
  (:require [jerzywie.albany-web.spreadsheet :as sut]
            [cljs.test :refer-macros [deftest are]]))

(deftest alpha-keyword->int-tests
  (are [kwd result] (= (sut/alpha-keyword->int kwd) result)
      :A 65
      :B 66
      :XYZ 88))

(deftest consecutive-col-list-tests
  (are [kwd-list consecutive-kwd-list] (= (sut/consecutive-col-list kwd-list)
                                          consecutive-kwd-list)
    (list :A :D) (list :A :B :C :D)
    (list :D :A) (list :A :B :C :D)
    (list :E :C :G) (list :C :D :E :F :G)))

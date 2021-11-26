(ns jerzywie.albany-web.spreadsheet-tests
  (:require [jerzywie.albany-web.spreadsheet :as sut]
            [cljs.test :refer-macros [deftest are]]))

(deftest col-keyword->int-tests
  (are [kwd result] (= (sut/col-keyword->int kwd) result)
      :A 1
      :B 2
      :X 24
      :Z 26
      :AA 27
      :AD 30
      :AZ 52
      :BA 53
      :BZ 78
      :CA 79
      :CE 83))

(deftest int->col-keyword-tests
  (are [int-val result] (= (sut/int->col-keyword int-val) result)
       1 :A
       26 :Z
       27 :AA
       28 :AB
       30 :AD
       52 :AZ
       53 :BA
       54 :BB
       55 :BC
       80 :CB))

(deftest consecutive-col-list-tests
  (are [kwd-list consecutive-kwd-list] (= (sut/consecutive-col-list kwd-list)
                                          consecutive-kwd-list)
    (list :A :D) (list :A :B :C :D)
    (list :D :A) (list :A :B :C :D)
    (list :E :C :G) (list :C :D :E :F :G)))

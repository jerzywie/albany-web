(ns jerzywie.albany-web.albany-spreadsheet-tests
  (:require [jerzywie.albany-web.albany-spreadsheet :as sut]
            [cljs.test :refer-macros [deftest is are]]))


(deftest read-workbook-tests
  (sut/read-workbook {:sheet :co} nil)
  (sut/read-workbook {:sheet :fin} nil)
  (sut/read-workbook {:sheet :ss} nil)
  (sut/read-workbook {:sheet :nonesuch} nil))

(deftest make-member-map-tests
  (is (= (sut/make-member-map :T 3 "m" ["-c" "-d" "-e"]) {:T  :m00-c
                                                          :U  :m00-d
                                                          :V  :m00-e
                                                          :W  :m01-c
                                                          :X  :m01-d
                                                          :Y  :m01-e
                                                          :Z  :m02-c
                                                          :AA :m02-d
                                                          :AB :m02-e}))

  (is (= (sut/make-member-map :X 7 "memb" []) {:X  :memb00
                                               :Y  :memb01
                                               :Z  :memb02
                                               :AA :memb03
                                               :AB :memb04
                                               :AC :memb05
                                               :AD :memb06
                                               }))

  (is (= (sut/make-member-map :X 5 "abc-" ["-xyz" nil]) {:X  :abc-00-xyz
                                                         :Y  nil
                                                         :Z  :abc-01-xyz
                                                         :AA nil
                                                         :AB :abc-02-xyz
                                                         :AC nil
                                                         :AD :abc-03-xyz
                                                         :AE nil
                                                         :AF :abc-04-xyz
                                                         :AG nil}))

  (is (= (sut/make-member-map :X 4 "abc-" ["-xyz" "" nil]) {:X  :abc-00-xyz
                                                            :Y  :abc-00
                                                            :Z  nil
                                                            :AA :abc-01-xyz
                                                            :AB :abc-01
                                                            :AC nil
                                                            :AD :abc-02-xyz
                                                            :AE :abc-02
                                                            :AF nil
                                                            :AG :abc-03-xyz
                                                            :AH :abc-03
                                                            :AI nil})))

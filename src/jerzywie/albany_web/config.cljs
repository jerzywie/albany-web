(ns jerzywie.albany-web.config)

(def config {:members
             {:Olivia     {:pad  1 :col 1}
              :George     {:pad  2 :col 2}
              :Amelia     {:pad  3 :col 3}
              :Noah       {:pad  5 :col 4}
              :Isla       {:pad 12 :col 5}
              :Arthur     {:pad  7 :col 6}
              :Ava        {:pad 13 :col 7}
              :Harry      {:pad  9 :col 8}
              :Mia        {:pad 10 :col 9}
              :Leo        {:pad 16 :col 10}
              :Sophia     {:pad 15 :col 11}}
             :login-url   "https://www.essential-trading.coop/Extranet/Login.aspx"
             :basket-url  "https://www.essential-trading.coop/Extranet/CurrentBasket.aspx"
             :basket-url-onetime   "https://www.essential-trading.coop/Extranet/ArchiveBasket.aspx?orderheaderid=7471"
             :order-url "none!"
             })

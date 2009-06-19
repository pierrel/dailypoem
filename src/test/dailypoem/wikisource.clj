(ns test.dailypoem.wikisource
  (:use (dailypoem wikisource)
	(fact core)))


;; (defn test-follow-url
;;   (is (not (nil (follow-url "http://google.com"))))
;;   (is (nil (follow-url "this is not a url"))))

(fact "follow-url should return something on a correct urls"
      [the-good '("ign.com" "google.com" "wikisource.org")
       the-bad '("#not a url" "also not a url" "nothing#@#$* at !@$ all")]
      (not (nil? (follow-url the-good)))
      (nil? (follow-url the-bad)))


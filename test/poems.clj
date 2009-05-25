(ns DailyPoem.test.poems
  (:require (clojure xml)
	    (clojure.contrib test-is))
  (:use (app.poems)))

(def test-html
     "<html><body><a title=\"poem\" href=\"/wiki/This_Is_A_Poem\">This is a Poem</a></body></html>")


(deftest test-get-elements-by-tag
  (is (not-empty (DailyPoem.app.poems/get-elements-by-tag (clojure.xml/parse test-html) :a))))
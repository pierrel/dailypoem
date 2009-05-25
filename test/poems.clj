(ns DailyPoem.test.poems
  (:require (app.poems))
  (:use (clojure xml) 
	(clojure.contrib.test-is)))

(def test-html
     "<html><body><a title=\"poem\" href=\"/wiki/This_Is_A_Poem\">This is a Poem</a></body></html>")


(deftest test-get-elements-by-tag
  (is (not-empty (poems/get-elements-by-tag html-node :a))))
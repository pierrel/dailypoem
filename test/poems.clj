(ns DailyPoem.test.poems
  (:require (clojure xml)
	    (clojure.contrib test-is))
  (:use (app.poems)))

(def test-html "test/test-html.html")


(deftest test-get-elements-by-tag
  (is (not-empty (DailyPoem.app.poems/get-elements-by-tag (clojure.xml/parse test-html) :a))))
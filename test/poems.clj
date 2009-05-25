(ns DailyPoem.test.poems
  (:require (clojure xml)
	    (clojure.contrib test-is))
  (:use (app.poems)))

(def test-html "test/test-html.html")


(deftest test-get-elements-by-tag
  (let [html-node (clojure.xml/parse test-html)]
    (is (not-empty (DailyPoem.app.poems/get-elements-by-tag html-node :a)))
    (is (= (DailyPoem.app.poems/get-elements-by-tag html-node :table) nil))))
(ns DailyPoem.test.poems
  (:use (clojure xml)
	(clojure.contrib test-is)
	(fact))
  (:require (app poems)))

(def test-html-file "test/test-html.html")

(def test-node (parse test-html-file))

(deftest test-get-elements-by-tag
  (is (not-empty (DailyPoem.app.poems/get-elements-by-tag :a)))
  (is (= (DailyPoem.app.poems/get-elements-by-tag test-node :table) nil)))

(deftest test-isa-node?
  (is (DailyPoem.app.poems/isa-node? test-node))
  (is (not (DailyPoem.app.poems/isa-node? {:html "something"}))))

(deftest test-isa-poem-link?
  (let [a-tags (DailyPoem.app.poems/get-elements-by-tag test-node :a)]
    (is (DailyPoem.app.poems/isa-poem-link? (first a-tags)))
    (is (not (isa-poem-link? (second a-tags))))))


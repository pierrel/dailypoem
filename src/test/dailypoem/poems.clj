(ns test.dailypoem.poems
  (:use (clojure xml)
	(clojure.contrib test-is)
	(fact))
  (:require (dailypoem poems)))

(def test-html-file "src/test/dailypoem/test-html.html")

(def test-node (parse test-html-file))

(deftest test-get-elements-by-tag
  (is (not-empty (get-elements-by-tag test-node :a)))
  (is (= (get-elements-by-tag test-node :table) nil)))

(deftest test-isa-node?
  (is (isa-node? test-node))
  (is (not (isa-node? {:html "something"}))))

(deftest test-isa-poem-link?
  (let [a-tags (get-elements-by-tag test-node :a)]
    (is (isa-poem-link? (first a-tags)))
    (is (not (isa-poem-link? (second a-tags))))))


(ns test.dailypoem.poems
  (:use (clojure xml)
	(clojure.contrib test-is)
	(fact))
  (:require (dailypoem poems)))

(def test-html-file "src/test/dailypoem/test-html.html")

(def test-node (parse test-html-file))

(deftest test-get-elements-by-tag
  (is (not-empty (dailypoem.poems/get-elements-by-tag test-node :a)))
  (is (= (dailypoem.poems/get-elements-by-tag test-node :table) nil)))

(deftest test-isa-node?
  (is (dailypoem.poems/isa-node? test-node))
  (is (not (dailypoem.poems/isa-node? {:html "something"}))))

(deftest test-isa-poem-link?
  (let [a-tags (dailypoem.poems/get-elements-by-tag test-node :a)]
    (is (dailypoem.poems/isa-poem-link? (first a-tags)))
    (is (not (dailypoem.poems/isa-poem-link? (second a-tags))))))


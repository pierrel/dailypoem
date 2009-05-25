(ns DailyPoem.test.poems
  (:use (app.poems)
	(clojure xml)
	(clojure.contrib test-is)))

(def test-html-file "test/test-html.html")

(def test-node (parse test-html-file))

(deftest test-get-elements-by-tag
  (is (not-empty (DailyPoem.app.poems/get-elements-by-tag test-node :a)))
  (is (= (DailyPoem.app.poems/get-elements-by-tag test-node :table) nil)))

(deftest test-isa-node?
  (is (DailyPoem.app.poems/isa-node? test-node)))
(ns DailyPoem.test.poems
  (:use app.poems)
  (:use fact)
  (:use clojure xml))

(def test-html
     "<html><body><a title=\"poem\" href=\"/wiki/This_Is_A_Poem\">This is a Poem</a></body></html>")

(fact "The test-html variable has an \"a\" tag"
      [html-node '((xml.parse test-html))]
      (not-empty (get-elements-by-tag html-node :a)))
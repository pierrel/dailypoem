; Used as a wrapper for any connections to wikisource
(ns dailypoem.wikisource
  (:use (clojure xml)))

(def base-url "http://en.wikisource.org")
(def starting-url "http://en.wikisource.org/wiki/Category:Poems")

(defn follow-url
  "returns the parsed node of the page pointed to by the first href of the given node"
  [url]
  (parse url))

(defn first-poem-page
  "returns the node representing the first page of poems on wikisource"
  []
  (parse starting-url))






; Used as a wrapper for any connections to wikisource
(ns dailypoem.wikisource
  (:require (clojure xml)
	    (dailypoem node-utils)))

(def base-url "http://en.wikisource.org")
(def starting-url "http://en.wikisource.org/wiki/Category:Poems")
(def poem-link-regex #"/wiki/[A-Za-z0-1_():]+$")

(defn poem-page
  "Returns the parsed node of the page pointed to by a poem node"
  [poem-node]
  (parse (str base-url (poem-node-href poem-node))))

(defn follow-node
  "returns the parsed node of the page pointed to by the first href of the given node"
  [node]
  (if (node-utils/))
  (parse (node-utils/node-href node)))

(defn next-node
  "Returns the parsed node linked to by the 'next 200' link in 'node'"
  [node]
  (let [next-url (next-link-node node)]
    (if next-url
      (parse (.concat base-url next-url)))))

(defn all-poem-nodes
  "Returns a list of all poem nodes stored in wikisource"
  ([node]
      (filter-elements isa-poem-link? (first (filter-elements #(= (:id (:attrs %)) "mw-pages") node))))
  ([]
     (all-poem-nodes (parse starting-url))))

(defn all-poems
  [starting-node]
  (loop
      [node starting-node poem-links nil]
    (. Thread (sleep 200))
    (if (nil? (next-link-url node))
      (concat poem-links (all-poem-nodes node))
      (recur (next-node node) (concat poem-links (all-poem-nodes node))))))




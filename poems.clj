(ns poems
  (:import (java.net URL)
	   (java.io BufferedReader InputStreamReader)))

;(def parsed (clojure.xml/parse ""))
(def base-url "http://en.wikisource.org")
(def starting-url "http://en.wikisource.org/wiki/Category:Poems")
(def poem-link-regex #"/wiki/[A-Za-z0-1_()]+$")

(defn isa-node?
  "Returns true if 'node' is a node, nil otherwise"
  [node]
  (and (= (type node) clojure.lang.PersistentStructMap)
       (reduce 'and (map #(contains? node %)
			 '(:tag :content :attrs)))))

(defn flat?
  "Returns true if seq contains no sequences"
  [seq]
  (not-any? #(isa? (type %) java.util.List) seq))

(defn flatten 
  "Returns an unnested sequence from the non-sequence elements of seq
for example, it turns (1 (2) 3) into (1 2 3)"
  [seq]
  (if (isa? (type seq) java.util.List)
    (if (flat? seq)
      seq
      (apply concat (map flatten seq)))
    (list seq)))

(defn get-elements-by-tag
  "Returns a sequence of elements matching the given tag within the given root-node.
Assumes that 'root-node' does not have the same tag as 'tag-name'.
Also just returns the deepest node with that tag so that for '<tag><tag>hey</tag></tag> it will return <tag>hey</tag>"
 [root-node tag-name]
  (when (isa-node? root-node)
    (let [matching-child-nodes (filter not-empty (map #(get-elements-by-tag % tag-name) (:content root-node)))]
      (if (not-empty matching-child-nodes)
	(flatten matching-child-nodes)
	(if (= (:tag root-node) tag-name)
	  root-node)))))

(defn filter-elements
  "Returns a sequence of elements satisfying the given predicate.
The 'predicate' argument must be a function taking one argument, it will be given a node as an argument.
Also just returns the deepest node satifying the predicate"
 [predicate root-node]
  (when (isa-node? root-node)
    (let [matching-child-nodes (filter not-empty (map #(filter-elements predicate %) (:content root-node)))]
      (if (not-empty matching-child-nodes)
	(flatten matching-child-nodes)
	(if (predicate root-node)
	  root-node)))))

(defn isa-poem-link?
  "returns true if the node is a link ('a'), has a title attribute and the link matches the poem-link-regex regex"
  [node]
  (and (= (:tag node) :a)
       (contains? (:attrs node) :title)
       (contains? (:attrs node) :href)
       (re-find poem-link-regex (:href (:attrs node)))))

(defn isa-next-link?
  "returns true if the node is a link ('a') and has 'next 200' as the content"
  [node]
  (and (= (:tag node) :a)
       (= (first (:content node)) "next 200")))

(defn all-poem-links
  ([node]
      (filter-elements isa-poem-link? (first (filter-elements #(= (:id (:attrs %)) "mw-pages") node))))
  ([]
     (all-poem-nodes (clojure.xml/parse starting-url))))

(defn next-link-url
  [node]
  (:href (:attrs (first (filter-elements isa-next-link? node)))))

(defn example-node []
  (let [root (clojure.xml/parse starting-url)]
    (get-elements-by-tag root :a)))

;; DOESNT WORK
(def all-poems
     (fn [n]
       (loop
	   [node n poem-links nil]
	 (if (nil? (next-link-url node))
	   (all-poem-links node)
	   (recur (clojure.xml/parse (.concat base-url (next-link-url node))) (concat poem-links (all-poem-links node)))))))
(ns DailyPoem.app.poems
  (:use (clojure.contrib seq-utils)
	(clojure xml)))


; Static variables

(def base-url "http://en.wikisource.org")
(def starting-url "http://en.wikisource.org/wiki/Category:Poems")
(def poem-link-regex #"/wiki/[A-Za-z0-1_():]+$")


; Boolean functions

(defn isa-node?
  "Returns true if 'node' is a node, nil otherwise"
  [node]
  (and (= (type node) clojure.lang.PersistentStructMap)
       (reduce 'and (map #(contains? node %)
			 '(:tag :content :attrs)))))

(defn isa-poem-link?
  "returns true if the node is a link to a poem('a'), has a title attribute and the link matches the poem-link-regex regex"
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


; Node filtering functions

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

(defn all-poem-links
  ([node]
      (filter-elements isa-poem-link? (first (filter-elements #(= (:id (:attrs %)) "mw-pages") node))))
  ([]
     (all-poem-links (parse starting-url))))


; Node/page traversal functions

(defn next-link-url
  [node]
  (:href (:attrs (first (filter-elements isa-next-link? node)))))

(defn next-node
  "Returns the parsed node linked to by the 'next 200' link in 'node'"
  [node]
  (let [next-url (next-link-url node)]
    (if next-url
      (parse (.concat base-url next-url)))))

(defn poem-node-title
  "Returns the title attribute of the given node"
  [node]
  (:title (:attrs node)))

(defn all-poems
  [starting-node]
  (loop
      [node starting-node poem-links nil]
    (if (nil? (next-link-url node))
      (concat poem-links (all-poem-links node))
      (recur (next-node node) (concat poem-links (all-poem-links node))))))

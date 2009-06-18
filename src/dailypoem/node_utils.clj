(ns dailypoem.node-utils
  (:use (dailypoem wikisource)
	(clojure.contrib seq-utils)))

(defstruct poem :title :author :text :url)
(def poem-link-regex #"/wiki/[A-Za-z0-1_():]+$")

(defn isa-link-node?
  "returns true if the tag is an 'a' and has an href attribute"
  [node]
  (and (= (:tag node) :a)
       (contains? (:attrs node) :href)))

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

(defn node-href
  "Returns the link to the poem"
  [node]
  (if isa-link-node? node
      (:href (:attrs node))))

(defn node-title
  "Returns the title attribute of the given node"
  [node]
  (:title (:attrs node)))

(defn node-url
  "Returns the url string pointed to by the poem node"
  [node]
  (:href (:attrs node)))

(defn node-author
  "Returns the author of the given poem link node"
  [node]
  (let [poem-page-node (follow-url (node-url node))]
    (first (:content (first (filter-elements #(= "header_author_text" (:id (:attrs %))) poem-page-node))))))

;; (defn poem-node-body
;;   "Returns the body of the given poem link node as a string of straight xml"
;;   [poem-node]
;;   (let [poem-page-node (parse (str base-url (poem-node-href poem-node)))]
;;     (emit (first (filter-elements #(= "poem" (:class (:attrs %))) poem-page-node)))))

(defn isa-node?
  "Returns true if 'node' is an xml node (like the kind returned by xml/parse), nil otherwise"
  [node]
  (and (= (type node) clojure.lang.PersistentStructMap)
       (reduce 'and (map #(contains? node %)
			 '(:tag :content :attrs)))))

(defn isa-poem-link?
  "returns true if the node is a link to a poem('a'), has a title attribute and the link matches the poem-link-regex regex"
  [node]
  (and (isa-link-node? node)
       (contains? (:attrs node) :title)
       (re-find poem-link-regex (:href (:attrs node)))))

(defn isa-next-link?
  "returns true if the node is a link ('a') and has 'next 200' as the content"
  [node]
  (and (= (:tag node) :a)
       (= (first (:content node)) "next 200")))

(defn next-link-url
  "returns the url of the \"(next 200)\" link in the node"
  [node]
  (node-url (first (filter-elements isa-next-link? node))))

(defn poem-node-to-poem-struct
  "Given a poem link node returns a poem struct
DOES NOT WORK, depends on 'poem-node-body'"
  [poem-link-node]
  (let [node-body *out*]
    (poem-node-body poem-link-node) ; writes body to *out*, which is now node-body
    (struct-map poem 
      :title (node-title poem-link-node)
      :author (node-author poem-link-node)
      :text  (node-body poem-link-node)
      :url (node-url poem-link-node))))

(defn next-link-node
  "Returns the node representing the 'next 200' link in the given node"
  [node]
  (first (filter-elements isa-next-link? node)))

(defn all-poem-nodes
  "Returns a list of all poem nodes stored in wikisource"
  ([node]
      (filter-elements isa-poem-link? (first (filter-elements #(= (:id (:attrs %)) "mw-pages") node))))
  ([]
     (all-poem-nodes (first-poem-page))))

(defn next-node
  "returns the node pointed to by the url of the \"(next 200)\" link"
  [node]
  (follow-url (next-link-url node)))

(defn all-poems
  [starting-node]
  (loop
      [node starting-node poem-links nil]
    (. Thread (sleep 200))
    (if (nil? (next-link-url node))
      (concat poem-links (all-poem-nodes node))
      (recur (next-node node) (concat poem-links (all-poem-nodes node))))))

(ns poems
  (:import (java.net URL)
	   (java.io BufferedReader InputStreamReader)))

;(def parsed (clojure.xml/parse ""))

(defn isa-node?
  "Returns true if 'node' is a node, nil otherwise"
  [node]
  (and (= (type node) clojure.lang.PersistentStructMap)
       (reduce 'and (map (fn [x] (contains? node x))
			 '(:tag :content :attrs)))))

(defn flat?
  "Returns true if seq contains no sequences"
  [seq]
  (not-any? (fn [x] (isa? (type x) java.util.List)) seq))

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
    (let [matching-child-nodes (filter not-empty (map (fn [x] (get-elements-by-tag x tag-name)) (:content root-node)))]
      (if (not-empty matching-child-nodes)
	(flatten matching-child-nodes)
	(if (= (:tag root-node) tag-name)
	  root-node)))))
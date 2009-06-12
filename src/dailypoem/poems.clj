(ns dailypoem.poems
  (:use (clojure.contrib seq-utils))
  (:require (wikisource)))


(def *poems* (ref nil))

(defn refresh-poems
  "refreshes the *poems* variable to all poems in wikisource and returns list of all poems in wikisource"
  []
;;   (dosync
;;    (alter *poems* #(all-poem-nodes))))
  (= *poems* (wikisource/all-poems)))

(defn random-poem-node
  "selects a poem at random from the *poems* variable"
  []
  (if *poems*
    (rand-elt *poems*)
    (rand-elt (refresh-poems))))

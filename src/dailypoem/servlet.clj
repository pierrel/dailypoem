(ns dailypoem.servlet
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use compojure.http compojure.html)
  (:require (dailypoem poems)))

(defroutes dailypoem-app
  (GET "/"
       (html [:body 
	      [:h1 "Hello, World!"]
	      [:ul
	       (let [nodes (dailypoem.poems/all-poem-nodes)]
		 (reduce html
			 (map (fn [node] [:li (dailypoem.poems/poem-node-title node)]) nodes)))]])))

(defservice dailypoem-app)
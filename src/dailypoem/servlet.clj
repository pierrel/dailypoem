(ns dailypoem.servlet
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use compojure.http compojure.html))

(defroutes dailypoem-app
  (GET "/"
       (html [:h1 "Hello, World!"])))

(defservice dailypoem-app)
(def test-html "test/test-html.html")

(defmacro test-function
  [the-function & body]
  `(let [function the-function]
     ~@body))

(defmacro with-test-node
  [& body]
  `(let [test-node (clojure.xml/parse test-html)]
     ~@body))
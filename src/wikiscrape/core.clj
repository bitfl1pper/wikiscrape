(ns wikiscrape.core
  [net.cgrand.enlive-html :as html]
  [clojure.java.io :as io])

(defn parse-web-html
  [h]
  (html/html-resource (io/as-url h)))

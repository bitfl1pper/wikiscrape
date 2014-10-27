(ns wikiscrape.core
  (:require
   [net.cgrand.enlive-html :as html]
   [clojure.java.io :as io]))

(defn parse-web-html
  [h]
  (html/html-resource (io/as-url h)))

(defn get-element
  "Pull out text by specific html tag.
   e.g. :p for p tag, :h1 for h1, etc."
  [element h]
  (map html/text (html/select (parse-web-html h) [element])))

(defn wikipage-bodytext
  [title]
  (let [url (str "https://en.wikipedia.org/wiki/" title)]
    (apply str (get-element :p url))))

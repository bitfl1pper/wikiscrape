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

;;;;; Example Usage ;;;;;

;;; Here's a simple script to get all text from (e.g.) one of Wikipedia's 'featured'
;;; areas.
;;; In this example we are using the physics section because the Wikipedia community
;;; has decided that it is exemplary of Wikipedia at its best.
;;;
;;; the physics category:
;;; it has a basic tree structure with physics at the top, four or so sub categories,
;;; and each topic in each category at the bottom
;;;
;;; we will naively represent this ontology(?) with a simple data structure called a
;;; 'plex' (placeholder name)
;;; Make a 'plex' data structure, the name of the plex is the
;;; overall category, each key is a sub category and is paired with a vector
;;; containing strings corresponding to topics in that sub category

(def physics-plex
  {:fundamental-concepts ["Space" "Time" "Matter" "Energy" "Electromagnetism" "Strong_interaction" "Weak_interaction" "Gravitational_Field" "Entropy"]
   :classical-physics ["Classical_Mechanics" "Electromagnetism" "States_of_Matter" "Thermodynamics" "Solid_Mechanics" "Fluid_Mechanics" "Acoustics" "Optics"]
   :modern-physics ["Special_Relativity" "General_Relativity" "Quantum_Mechanics" "Quantum_Field_Theory" "Quantum_Gravity" "Condensed_Matter_Physics" "Atomic_Physics" "Nuclear_Physics" "Particle_Physics" "Plasma_Physics"]
   :cross-discipline-topics ["Mathematical_Physics" "Astronomy" "Chemical_Physics"  "Materials_Science" "Geophysics" "Biophysics" "Nanotechnology"]})

(defn flat-plex
  "This flattens the plex (and removes redundant topics between subcategories) so that
   it can be processed by other functions. It does not destroy or mutate the plex,
   so that it can maintain the hierarchical information, for further use."
  [plex]
  (distinct (flatten (map #(% plex) (keys plex)))))

(defn scrape-wiki-plex
  "This maps the bodytext scrape fn through a plex, it returns a lot of text."
  [plex]
  (map #(wikipage-bodytext %) (flat-plex plex)))

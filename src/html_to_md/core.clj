(ns html-to-md.core
    (:require [html-to-md.transformer :refer [transform process]]
              [html-to-md.html-to-md :refer [markdown-dispatcher]]))

(defn html-to-md
    "Transform the HTML document referenced by `url` into Markdown, and write
    it to `output`, if supplied."
    ([url]
     (apply str (transform url markdown-dispatcher)))
    ([url output]
     (spit output (html-to-md url))))

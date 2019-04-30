(ns html-to-md.core
    (:require [html-to-md.transformer :refer [transform process]]
              [html-to-md.html-to-md :refer [markdown-dispatcher]]
              [html-to-md.blogger-to-md :refer [blogger-dispatcher]]))

(defn html-to-md
    "Transform the HTML document referenced by `url` into Markdown, and write
    it to `output`, if supplied."
    ([url]
     (apply str (transform url markdown-dispatcher)))
    ([url output]
     (spit output (html-to-md url))))

(defn blogger-to-md
    "Transform the Blogger post referenced by `url` into Markdown, and write
    it to `output`, if supplied. *NOTE:* This was written to scrape *my*
    blogger pages, yours may be different!"
    ([url]
     (apply str (transform url blogger-dispatcher)))
    ([url output]
     (spit output (blogger-to-md url))))

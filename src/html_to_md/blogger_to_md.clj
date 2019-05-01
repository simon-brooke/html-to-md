(ns html-to-md.blogger-to-md
    "Convert blogger posts to Markdown format, omitting all the Blogger chrome
    and navigation."
    (:require [clojure.string :as s]
              [html-to-md.html-to-md :refer [markdown-dispatcher markdown-header]]
              [html-to-md.transformer :refer [process]]
              [net.cgrand.enlive-html :as html]))

(defn blogger-scraper
    "Processor which scrapes the actual post content out of a blogger page.
    *NOTE:* This was written to scrape *my* blogger pages, yours may be
    different!"
    [e d]
    (let [title (first (html/select e [:h3.post-title]))
          content (html/select e [:div.post-body])]
        (if (and title content)
            (apply
                str
                (cons
                    (markdown-header title d 1)
                    (process content d))))))

(defn image-table-processor
    "Blogger's horrible tag soup wraps images in tables. Is this table such
    a table? If so extract the image from it and process it to markdown;
    otherwise, fall back on what `markdown-dispatcher` would do with the
    table (which is currently nothing, but that will change)."
    [e d]
    (let [caption (process (first (html/select e [:td.tr-caption])) d)
          alt (if caption (s/trim (apply str caption)))
          image (first (html/select e [:img]))
          src (if image (-> image :attrs :src))]
        (if image
            (str "![image: " alt "](" src ")")
            (process e markdown-dispatcher))))


(def blogger-dispatcher
    "Adaptation of `markdown-dispatcher`, q.v., with the `:table` and
    `:html` dispatches overridden."
    (assoc markdown-dispatcher
        :html blogger-scraper
        :table image-table-processor))

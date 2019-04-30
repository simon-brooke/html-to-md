(ns html-to-md.html-to-md
    (:require
        [clojure.string :as s]
        [net.cgrand.enlive-html :as html]
        [html-to-md.transformer :refer [process]]))

(defn markdown-a
    "Process the anchor element `e` into markdown, using dispatcher `d`."
    [e d]
    (apply
        str
        (flatten
            (list
                "["
                (map #(process % d) (:content e))
                "]("
                (-> e :attrs :href)
                ")"))))

(defn markdown-code
    "Process the code or samp `e` into markdown, using dispatcher `d`."
    [e d]
    (str
        "`"
        (s/trim (apply str (map #(process % d) (:content e))))
        "`"))

(defn markdown-default
    "Process an element `e` for which we have no other function into markdown,
    using dispatcher `d`."
    [e d]
    (apply str (map #(process % d) (:content e))))

(defn markdown-div
    "Process the division element `e` into markdown, using dispatcher `d`."
    [e d]
    (apply
        str
        (flatten
            (list "\n" (map #(process % d) (:content e)) "\n"))))

(defn markdown-em
    "Process the emphasis element `e` into markdown, using dispatcher `d`."
    [e d]
    (str
        "*"
        (s/trim (apply str (map #(process % d) (:content e))))
        "*"))

(defn markdown-header
    "Process the header element `e` into markdown, with level `level`,
    using dispatcher `d`."
    [e d level]
    (apply
        str
        (flatten
            (list
                "\n"
            (take level (repeat "#"))
                " "
            (map #(process % d) (:content e))
                "\n"))))

(defn markdown-h1
    "Process the header element `e` into markdown, with level 1, using
    dispatcher `d`."
    [e d]
    (markdown-header e d 1))

(defn markdown-h2
    "Process the header element `e` into markdown, with level 2, using
    dispatcher `d`."
    [e d]
    (markdown-header e d 2))

(defn markdown-h3
    "Process the header element `e` into markdown, with level 3, using
    dispatcher `d`."
    [e d]
    (markdown-header e d 3))

(defn markdown-h4
    "Process the header element `e` into markdown, with level 4, using
    dispatcher `d`."
    [e d]
    (markdown-header e d 4))

(defn markdown-h5
    "Process the header element `e` into markdown, with level 5, using
    dispatcher `d`."
    [e d]
    (markdown-header e d 5))

(defn markdown-h6
    "Process the header element `e` into markdown, with level 6, using
    dispatcher `d`."
    [e d]
    (markdown-header e d 6))

(defn markdown-html
    "Process this HTML element `e` into markdown, using dispatcher `d`."
    [e d]
    (apply str (process (first (html/select e [:body])) d) ))

(defn markdown-img
    "Process this image element `e` into markdown, using dispatcher `d`."
    [e d]
    (str "![" (-> e :attrs :alt) "](" (-> e :attrs :src) ")"))

(defn markdown-ol
    "Process this ordered list element `e` into markdown, using dispatcher
    `d`."
    [e d]
    (str
        "\n"
        (apply str
               (doall
                   (map
                       #(apply
                            str
                            (flatten
                                (list "\n" (inc %2) ". " (process %1 d))))
                       (:content e)
                       (range))))
        "\n\n"))

(defn markdown-pre
    "Process the preformatted emphasis element `e` into markdown, using
    dispatcher `d`."
    [e d]
    (str
        "\n```\n"
        (s/trim (apply str (map #(process % d) (:content e))))
        "\n```\n"))

(defn markdown-strong
    "Process the strong emphasis element `e` into markdown, using dispatcher
    `d`."
    [e d]
    (str
        "**"
        (s/trim (apply str (map #(process % d) (:content e))))
        "**"))

(defn markdown-ul
    "Process this unordered list element `e` into markdown, using dispatcher
    `d`."
    [e d]
    (str
        "\n"
        (apply str
               (doall
                   (map
                       #(apply
                            str
                            (flatten
                                (list "\n* " (process % d))))
                       (:content e))))
        "\n\n"))


(def markdown-dispatcher
    {:a markdown-a
     :b markdown-strong
     :code markdown-code
     :body markdown-default
     :div markdown-div
     :em markdown-em
     :h1 markdown-h1
     :h2 markdown-h2
     :h3 markdown-h3
     :h4 markdown-h4
     :h5 markdown-h5
     :h6 markdown-h6
     :html markdown-html
     :i markdown-em
     :img markdown-img
     :ol markdown-ol
     :p markdown-div
     :pre markdown-pre
     :samp markdown-code
     :span markdown-default
     :strong markdown-strong
     :ul markdown-ul
     })


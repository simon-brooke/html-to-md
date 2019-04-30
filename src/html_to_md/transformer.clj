(ns html-to-md.transformer
  (:require
      [clojure.string :as s]
      [net.cgrand.enlive-html :as html]
      [net.cgrand.tagsoup :as tagsoup]))

(declare process)

(defn markdown-a
    "Process the anchor element `e` into markdown"
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

(defn markdown-strong
    [e d]
    ;; same as `:strong`, q.v.
    (str
        "**"
        (s/trim (apply str (map #(process % d) (:content e))))
        "**"))

(defn markdown-div
    [e d]
    (apply
        str
        (flatten
            (list "\n" (map #(process % d) (:content e)) "\n"))))


(def markdown-dispatcher
    {:a markdown-a
     :b markdown-strong
     :div markdown-div
     :em (fn [e d]
             ;; same as `:i`, q.v.
             (str
                 "*"
                 (s/trim (apply str (map #(process % d) (:content e))))
                 "*"))
     :h1 (fn [e d]
             (apply
                 str
                 (flatten
                     (list "\n# " (map #(process % d) (:content e)) "\n"))))
     :h2 (fn [e d]
             (apply
                 str
                 (flatten
                     (list "\n## " (map #(process % d) (:content e)) "\n"))))
     :h3 (fn [e d]
             (apply
                 str
                 (flatten
                     (list "\n### " (map #(process % d) (:content e)) "\n"))))
     :h4 (fn [e d]
             (apply
                 str
                 (flatten
                     (list
                         "\n#### "
                         (map #(process % d) (:content e))
                         "\n"))))
     :h5 (fn [e d]
             (apply
                 str (flatten (list "\n##### " (map #(process % d) (:content e)) "\n"))))
     :h6 (fn [e d] (apply str (flatten (list "\n###### " (map #(process % d) (:content e)) "\n"))))
     :html (fn [e d] (apply str (process (html/select e [:body]) d) ))
     :i (fn [e d] (str "*" (s/trim (apply str (map #(process % d) (:content e)))) "*"))
     :img (fn [e d] (str "![" (-> e :attrs :alt) "](" (-> e :attrs :src) ")"))
     :strong (fn [e d]
                 (str
                     "**"
                     (s/trim (apply str (map #(process % d) (:content e))))
                     "**"))
     })

(defn process
    "Process this `element`, assumed to be a [HT|SG|X]ML element in Enlive
    encoding, using this `dispatcher`, assumed to be a function (or more
    probably a map) which takes one argument, the tag of the element as
    keyword, and returns a function which processes elements with that tag.

    Such a function should take two arguments, the `element` itself and a
    dispatcher which will normally (but not necessarily) be the `dispatcher`
    supplied to this function.

    If the dispatcher returns `nil`, the default behaviour is that `process`
    is mapped over the content of the element.

    If `element` is not an [HT|SG|X]ML element in Enlive encoding or else a
    string, returns `nil`. Strings are returned unaltered."
    [element dispatcher]
    (cond
        (:tag element)
        (let [processor (apply dispatcher (list (:tag element)))]
            (if processor
                (apply processor (list element dispatcher))
                (map #(process % dispatcher) (:content element))))
        (string? element) element))

(defmulti transform
    "Transform the `obj` which is my first argument using the `dispatcher`
    which is my second argument."
    (fn [obj dispatcher] (type obj)) :default :default)

(defmethod transform :default [obj dispatcher]
    (process obj dispatcher))

(defmethod transform java.net.URI [uri dispatcher]
    (process (html/html-resource uri) dispatcher))

(defmethod transform java.net.URL [url dispatcher]
    (transform (.toURI url) dispatcher))

(defmethod transform String [s dispatcher]
    (let [url (try (java.net.URL. s) (catch Exception any))]
        (if url (transform url dispatcher)
            ;; otherwise, if s is not a URL, consider it as an HTML fragment,
            ;; parse and process it
            (process (tagsoup/parser (java.io.StringReader s)) dispatcher)
            )))

(process {:tag :h1 :content ["Hello dere!"]} markdown-dispatcher)


(transform "<h1>Hello dere!</h1>" markdown-despatcher)

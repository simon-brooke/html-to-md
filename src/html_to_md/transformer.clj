(ns html-to-md.transformer
  (:require
      [net.cgrand.enlive-html :as html]
      [net.cgrand.tagsoup :as tagsoup]))


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

        (string? element) element
        (or (seq? element) (vector? element))
        (doall (map #(process % dispatcher) element))))

(defn- transformer-dispatch
    [a _]
    (class a))

(defmulti transform
    "Transform the `obj` which is my first argument using the `dispatcher`
    which is my second argument."
    #'transformer-dispatch
     :default :default)

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


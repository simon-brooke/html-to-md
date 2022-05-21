(ns html-to-md.transformer
    "The actual transformation engine, which is actually far more general
    than just something to generate
    [Markdown](https://daringfireball.net/projects/markdown/). It isn't as
    general as [XSL-T](https://www.w3.org/standards/xml/transformation) but
    can nevertheless do a great deal of transformation on [HT|SG|X]ML
    documents.

    ## Terminology

    In this documentation the following terminology is used:

    * **dispatcher**: a `dispatcher` is a function (or more
    probably a map) which takes one argument, the tag of the element as a
    keyword, and returns a `processor`, q.v.
    * **processor**: a `processor` is a function of two arguments, an
    [Enlive](https://github.com/cgrand/enlive) encoded (X)HTML element and
    a `dispatcher` as described above, which processes elements into the
    desired format.

    ## Generality

    **NOTE** that while `processors` within the `html-to-md` package generally
    process elements into strings (since Markdown is a text format), when
    processing into an XML format it will generally be preferable that
    `processors` should return Enlive style elements."
  (:require
      [net.cgrand.enlive-html :as html]
      [net.cgrand.tagsoup :as tagsoup]))


(defn process
    "Process this `element`, assumed to be a [HT|SG|X]ML element in
    [Enlive](https://github.com/cgrand/enlive)
    encoding, using this `dispatcher`,

    Such a function should take two arguments, the `element` itself and a
    dispatcher which will normally (but not necessarily) be the `dispatcher`
    supplied to this function.

    If the dispatcher returns `nil`, the default behaviour is that `process`
    is mapped over the content of the element.

    If `element` is not an [HT|SG|X]ML element in Enlive encoding as descibed
    above, then

    1. if the `element` is a string, returns that string unaltered;
    2. if the `element` is a sequence or vector, maps `process` across the
    members of the sequence;
    3. otherwise, returns `nil`."
    [element dispatcher]
    (cond
        (:tag element)
        (let [processor (apply dispatcher (list (:tag element)))]
            (if processor
                (apply processor (list element dispatcher))
                (map #(process % dispatcher) (:content element))))

        (string? element) element
        (or (seq? element) (vector? element))
        (remove nil? (map #(process % dispatcher) element))))

(defn- transformer-dispatch
    "Hack to get dispatch on just the first argument to the `transform`
    multi-method."
    [a _]
    (class a))

(defmulti transform
    "Transform the `obj` which is my first argument using the `dispatcher`
    which is my second argument. `obj` can be:

    1. A URL or URI;
    2. A string representation of a URL or URI;
    3. A string representation of an (X)HTML fragment;
    4. An [Enlive](https://github.com/cgrand/enlive) encoded (X)HTML element;
    5. A sequence of [Enlive](https://github.com/cgrand/enlive) encoded
    (X)HTML elements."
    #'transformer-dispatch
     :default :default)

(defmethod transform :default [obj dispatcher]
    (process obj dispatcher))

(defmethod transform java.net.URI [uri dispatcher]
    (remove nil? (process (html/html-resource uri) dispatcher)))

(defmethod transform java.net.URL [url dispatcher]
    (transform (.toURI url) dispatcher))

(defmethod transform String [s dispatcher]
    (let [url (try (java.net.URL. s) (catch Exception any))]
        (if url (transform url dispatcher)
            ;; otherwise, if s is not a URL, consider it as an HTML fragment,
            ;; parse and process it
            (process (tagsoup/parser (java.io.StringReader. s)) dispatcher))))

# Introduction to html-to-md

The itch I'm trying to scratch at present is to transform
[Blogger.com](http://www.blogger.com)'s dreadful tag-soup markup into markdown;
but my architecture for doing this is to build a completely general [HT|SG|X]ML
transformation framework and then specialise it.

**WARNING:** this is presently alpha-quality code, although it does have fair
unit test coverage.

## Usage

To use this library in your project, add the following leiningen dependency:

    [org.clojars.simon_brooke/html-to-md "0.1.0"]

To use it in your namespace, require:

    [html-to-md.core :refer [html-to-md]]

For default usage, that's all you need. To play more sophisticated tricks,
consider:

    [html-to-md.transformer :refer [transform process]]
    [html-to-md.html-to-md :refer [markdown-dispatcher]]

The intended usage is as follows:

```clojure
(require '[html-to-md.core :refer [html-to-md]])

(html-to-md url output-file)
```

This will read (X)HTML from `url` and write Markdown to `output-file`. If
`output-file` is not supplied, it will return the markdown as a string:

```clojure
(require '[html-to-md.core :refer [html-to-md]])

(def md (html-to-md url))
```

If you are specifically scraping [blogger.com](https://www.blogger.com/")
pages, you may *try* the following recipe:

```clojure
(require '[html-to-md.core :refer [blogger-to-md]])

(blogger-to-md url output-file)
```

It works for my blogger pages. However, I'm not sure to what extent the
skinning of blogger pages is pure CSS (in which case my recipe should work
for yours) and to what extent it's HTML templating (in which case it
probably won't). Results not guaranteed, if it doesn't work you get to
keep all the pieces.

## Extending the transformer

In principle, the transformer can transform any [HT|SG|X]ML markup into any
other, or into any textual form. To extend it to do something other than
markdown, supply a **dispatcher**. A dispatcher is essentially a function of one
argument, a [HT|SG|X]ML tag represented as a Clojure keyword, which returns
a **processor,** which should be a function of two arguments, an element assumed
to have that tag, and a dispatcher. The processor should return the value that
you want elements of that tag transformed into.

Thus the `html-to-md.html-to-md` namespace comprises a number of *processor*
functions, such as this one:

```clojure
(defn markdown-a
    "Process the anchor element `e` into markdown, using dispatcher `d`."
    [e d]
    (str
        "["
        (s/trim (apply str (process (:content e) d)))
        "]("
        (-> e :attrs :href)
        ")"))
```

and a *dispatcher* map:

```clojure
(def markdown-dispatcher
    "A despatcher for transforming (X)HTML into Markdown."
    {:a markdown-a
     :b markdown-strong
     :br markdown-br
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
     :script markdown-omit
     :span markdown-default
     :strong markdown-strong
     :style markdown-omit
     :ul markdown-ul
     })
```

Obviously it is convenient to write dispatchers as maps, but it isn't required
that you do so: anything which, given a keyword, will return a processor, will
work.

## License

Copyright © 2019 Simon Brooke <simon@journeyman.cc>

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.


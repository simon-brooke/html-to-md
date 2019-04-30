# html-to-md

A Clojure library designed to convert (Enlivened) HTML to markdown; but, more
generally, a framework for [HT|SG|X]ML transformation.

## Introduction

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

## Extending the transformer

In principle, the transformer can transform any [HT|SG|X]ML markup into any
other, or into any textual form. To extend it to do something other than
markdown, supply a **dispatcher**. A dispatcher is essentially a function of one
argument, a [HT|SG|X]ML tag represented as a Clojure keyword, which returns
a **processor,** which should be a function of two arguments, an element assumed
to have that tag, and a dispatcher. The processor should return the value that
you want elements of that tag transformed into.

Obviously it is convenient to write dispatchers as maps, but it isn't required
that you do so: anything which, given a keyword, will return a processor, will
work.

## License

Copyright © 2019 Simon Brooke <simon@journeyman.cc>

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.


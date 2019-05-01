(defproject html-to-md "0.4.0-SNAPSHOT"
    :description "Convert (Enlivened) HTML to markdown; but, more generally, a framework for [HT|SG|X]ML transformation."
    :url "https://github.com/simon-brooke/html-to-md"
    :license {:name "Eclipse Public License"
              :url "http://www.eclipse.org/legal/epl-v10.html"}
    :codox {:metadata {:doc "**TODO**: write docs"
                       :doc/format :markdown}
            :output-path "docs"
            :source-uri "https://github.com/simon-brooke/html-to-md/blob/master/{filepath}#L{line}"}
    :dependencies [[org.clojure/clojure "1.8.0"]
                   [enlive "1.1.6"]]
    :plugins [[lein-codox "0.10.3"]
              [lein-release "1.0.5"]]
    :lein-release {:deploy-via :clojars}
    :signing {:gpg-key "Simon Brooke (Stultus in monte) <simon@journeyman.cc>"})

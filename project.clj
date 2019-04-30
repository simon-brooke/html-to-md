(defproject html-to-md "0.1.0"
    :description "Convert (Enlivened) HTML to markdown; but, more generally, a framework for [HT|SG|X]ML transformation."
    :url "https://github.com/simon-brooke/html-to-md"
    :license {:name "Eclipse Public License"
              :url "http://www.eclipse.org/legal/epl-v10.html"}
    :dependencies [[org.clojure/clojure "1.8.0"]
                   [enlive "1.1.6"]]
    :plugins [[lein-codox "0.10.3"]]
    :lein-release {:deploy-via :clojars}
    :signing {:gpg-key "Simon Brooke (Stultus in monte) <simon@journeyman.cc>"})

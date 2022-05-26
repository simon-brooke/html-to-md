(ns html-to-md.transformer-test
  (:require
   [clojure.test :as t :refer [deftest is testing]]
   [html-to-md.html-to-md :refer [markdown-dispatcher]]
   [html-to-md.transformer :refer [transform]]))

(deftest transform-payload
  (testing "String `obj` for: 3. A string representation of an (X)HTML fragment;"
    (is (= '("\n# This is a header\n")
           (transform "<h1>This is a header</h1>" markdown-dispatcher)))))

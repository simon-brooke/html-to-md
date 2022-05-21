(ns html-to-md.transformer-test
  (:require
   [clojure.string :as str]
   [clojure.test :as t :refer [deftest is testing]]
   [html-to-md.html-to-md :refer [markdown-dispatcher]]
   [html-to-md.transformer :refer [transform]]))

(deftest transform-payload
  (testing "String `obj` for: 3. A string representation of an (X)HTML fragment;"
    (is (= "# This is a header"
           (str/trim (-> "<h1>This is a header"
                         (transform markdown-dispatcher)
                         (first)))))))

(ns html-to-md.html-to-md-test
    (:require [clojure.test :refer :all]
              [html-to-md.transformer :refer [process]]
              [html-to-md.html-to-md :refer :all]))

(deftest a-test
    (testing "Anchor tag."
        (let [expected "[Hello dere!](http://foo.bar)"
              actual (process {:tag :a :attrs {:href "http://foo.bar"} :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest b-test
    (testing "Bold tag."
        (let [expected "**Hello dere!**"
              actual (process {:tag :b :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual))))
    (testing "STRONG emphasis tag."
        (let [expected "**Hello dere!**"
              actual (process {:tag :strong :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest div-test
    (testing "DIVision tag."
        (let [expected "\nHello dere!\n"
              actual (process {:tag :div :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest em-test
    (testing "EMphasis tag."
        (let [expected "*Hello dere!*"
              actual (process {:tag :em :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual))))
    (testing "Italics tag"
        (let [expected "*Hello dere!*"
              actual (process {:tag :i :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest h1-test
    (testing "Level 1 header tag."
        (let [expected "\n# Hello dere!\n"
              actual (process {:tag :h1 :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest h2-test
    (testing "Level 2 header tag."
        (let [expected "\n## Hello dere!\n"
              actual (process {:tag :h2 :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest h3-test
    (testing "Level 3 header tag."
        (let [expected "\n### Hello dere!\n"
              actual (process {:tag :h3 :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest h4-test
    (testing "Level 4 header tag."
        (let [expected "\n#### Hello dere!\n"
              actual (process {:tag :h4 :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest h5-test
    (testing "Level 5 header tag."
        (let [expected "\n##### Hello dere!\n"
              actual (process {:tag :h5 :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest h6-test
    (testing "Level 6 header tag."
        (let [expected "\n###### Hello dere!\n"
              actual (process {:tag :h6 :content ["Hello dere!"]} markdown-dispatcher)]
            (is (= expected actual)))))

(deftest img-test
    (testing "Image tag."
        (let [expected "![Hello dere!](http://foo.bar/image.png)"
              actual (process
                         {:tag :img
                          :attrs {:src "http://foo.bar/image.png"
                                  :alt "Hello dere!"}}
                         markdown-dispatcher)]
            (is (= expected actual)))))

(deftest list-test
    (testing "ordered list tag."
        (let [expected "\n\n1. foo\n2. bar\n3. ban\n\n"
              actual (process
                         {:tag :ol
                          :content
                          [{:tag :li :content ["foo"]}
                           {:tag :li :content ["bar"]}
                           {:tag :li :content ["ban"]}]}
                         markdown-dispatcher)]
            (is (= expected actual))))
    (testing "umordered list tag."
        (let [expected "\n\n* foo\n* bar\n* ban\n\n"
              actual (process
                         {:tag :ul
                          :content
                          [{:tag :li :content ["foo"]}
                           {:tag :li :content ["bar"]}
                           {:tag :li :content ["ban"]}]}
                         markdown-dispatcher)]
            (is (= expected actual)))))


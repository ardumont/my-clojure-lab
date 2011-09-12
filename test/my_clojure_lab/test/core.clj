(ns my-clojure-lab.test.core
  (:use [my-clojure-lab.core])
  (:use [clojure.test])
  (:use [midje.sweet])
  (:use [clojure.contrib.repl-utils :only [show]])
  (:use [clojure.pprint             :only [pprint]])
  (:use [clojure.walk               :only [macroexpand-all]]))


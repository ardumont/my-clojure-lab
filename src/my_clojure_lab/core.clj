(ns my-clojure-lab.core
   (:use [clojure.test               :only [run-tests]])
   (:use [midje.sweet])
   (:use [clojure.contrib.repl-utils :only [show]])
   (:use [clojure.pprint             :only [pprint]])
   (:use [clojure.walk               :only [macroexpand-all]]))
)

;.;. There's a certain satisfaction in a little bit of pain. -- Madonna
(fact (+ 1 1) => 2)


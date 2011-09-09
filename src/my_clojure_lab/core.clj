(ns my-clojure-lab.core
  (:use midje.sweet))

;.;. Of course the universe *is* out to get us, but it's not going to do it
;.;. by passing a null to one of our methods. -- Ron Jeffries
(fact (+ 1 1) => 2)


(ns my-clojure-lab.test.core
  (:use [my-clojure-lab.core])
  (:use [clojure.test])
  (:use [midje.sweet])
  (:use [clojure.contrib.repl-utils :only [show]])
  (:use [clojure.pprint             :only [pprint]])
  (:use [clojure.walk               :only [macroexpand-all]]))

                                       ; simple fact to present the midje framework
(fact (+ 1 1) => 2)
(fact (* 1 1) => 1)

                                        ; example on how to declare a function

(fact (my-mult 10 2) => 20)

                                        ; another way of declaring functions

(fact (sq 0) => 0)
(fact (sq 5) => 25)

(fact (doc sq) => nil)

                                        ; another one with multiple implementations
                                        ; depending on the number of arguments (fixed)

(fact (square-or-multiply) => 0)
(fact (square-or-multiply 5) => 25)
(fact (square-or-multiply 5 6) => 30)

                                        ; this time with an indefinite number of arguments


(fact (add-arg-count 5) => 5)
(fact (add-arg-count 5 0) => 6)
(fact (add-arg-count 5 0 1 2 3) => 9)

                                        ; square function with shorthand function declaration
(fact (sq2 0) => 0)
(fact (sq2 5) => 25)

                                        ; shorthand function declaration
(fact (my-mult2 10 2) => 20)
(fact (my-mult 1 2) => 2)

                                        ; conditional expressions
                                        ; if

(fact (if (= 1 1) "Maths still work!") => "Maths still work!")
(fact (if (= 1 2) "Maths is broken!" "Maths still work!") => "Maths still work!")

(fact (if-not (= 1 1) "Maths is broken!" "Maths still work!") => "Maths still work!")

                                        ; cond


(fact (weather-judge -10) => "extremely cold!")
(fact (weather-judge 10) => "cold!")
(fact (weather-judge 20) => "nice!")
(fact (weather-judge 30) => "too hot!")

                                        ; Local bindings

(fact (let [ a 2 b 3] (+ a b)) => 5)

                                        ; convert minutes to seconds

(fact (minutes-to-seconds 5) => 300)
(fact (minutes-to-seconds 10) => 600)

                                        ; convert seconds to weeks


; 7 days 
(fact (seconds-to-weeks 604800) => 1)


(fact (seconds-to-weeks2 604800) => 1)
(fact (seconds-to-weeks2 1209600) => 2)

                                        ; Looping and recursion

                                        ; absolute value of a number

(fact (abs -10) => 10)
(fact (abs 10) => 10)

                                        ; average number of two arguments

(fact (avg 10 5) => 15/2)
(fact (avg 10 6) => 8)

                                        ; is the result close enough
                                        ; of the square root of a number

(fact (good-enough? 25 5) => true)
(fact (good-enough? 25 4) => false)
(fact (good-enough? 25 4.9999999) => true)

                                        ; square root

(fact (good-enough? 25 (sqrt 25)) => true)
(fact (good-enough? 16 (sqrt 16)) => true)
(fact (sqrt 1) => 1)
(fact (sqrt 25) => (roughly 5))
;.;. The sum of wisdom is that time is never lost that is devoted to
;.;. work. -- Emerson
(fact (sqrt 16) => (roughly 4))
(fact (sqrt 100) => (roughly 10))

                                        ; exponent

(fact (pow 1 1) => 1)
(fact (pow 1 0) => 1)
(fact (pow 2 3) => 8)
(fact (pow 2 10) => 1024)

                                        ; zero? -> exists in clojure.core/zero?
;(fact (zero? 0) => true)
;(fact (zero? 1) => false)


(fact (pow2 1 1) => 1)
(fact (pow2 1 0) => 1)
(fact (pow2 2 3) => 8)
(fact (pow2 2 10) => 1024)

                                        ; add-up add up all the number to a given limit

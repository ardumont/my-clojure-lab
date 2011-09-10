(ns my-clojure-lab.core
   (:use [clojure.test               :only [run-tests]])
   (:use [midje.sweet])
   (:use [clojure.contrib.repl-utils :only [show]])
   (:use [clojure.pprint             :only [pprint]])
   (:use [clojure.walk               :only [macroexpand-all]]))

                                        ; simple fact to present the midje framework
(fact (+ 1 1) => 2)

                                        ; example on how to declare a function
(def my-mult (fn [x y] (* x y)))

(fact (my-mult 10 2) => 20)


                                        ; another way of declaring functions
(defn sq
  "squares the provided argument"
  [x]
  (* x x))

(fact (sq 0) => 0)
(fact (sq 5) => 25)

(fact (doc sq) => nil)

                                        ; another one with multiple implementations
                                        ; depending on the number of arguments (fixed)
(defn square-or-multiply
  "squares the simple provided argument, multiply the 2 arguments provided"
  ([] 0)
  ([x] (* x x))
  ([x y] (* x y)))

(fact (square-or-multiply) => 0)
(fact (square-or-multiply 5) => 25)
(fact (square-or-multiply 5 6) => 30)

                                        ; this time with an indefinite number of arguments

(defn add-arg-count
  "Returns the sum of the first argument and the number additionals arguments"
  [first & more]
  (+ first (count more)))

(fact (add-arg-count 5) => 5)
(fact (add-arg-count 5 0) => 6)
(fact (add-arg-count 5 0 1 2 3) => 9)

                                        ; square function with shorthand function declaration
(def sq2 #(* % %))
(fact (sq2 0) => 0)
(fact (sq2 5) => 25)

                                        ; shorthand function declaration
(def my-mult2 #(* %1 %2))
(fact (my-mult2 10 2) => 20)
(fact (my-mult 1 2) => 2)

                                        ; conditional expressions
                                        ; if

(fact (if (= 1 1) "Maths still work!") => "Maths still work!")
(fact (if (= 1 2) "Maths is broken!" "Maths still work!") => "Maths still work!")

(fact (if-not (= 1 1) "Maths is broken!" "Maths still work!") => "Maths still work!")

                                        ; cond

(defn weather-judge
  "given a temperature in degree centigrade, it will comment on the weather's quality"
  [x]
  (cond
   (<= x 0) "extremely cold!"
   (<= x 10) "cold!"
   (<= x 20) "nice!"
   :else "too hot!")
  )

(fact (weather-judge -10) => "extremely cold!")
(fact (weather-judge 10) => "cold!")
(fact (weather-judge 20) => "nice!")
(fact (weather-judge 30) => "too hot!")

                                        ; Local bindings

(fact (let [ a 2 b 3] (+ a b)) => 5)

                                        ; convert minutes to seconds
(defn minutes-to-seconds
  "Converts minutes to seconds"
  [nb-minutes]
  (let [nb-seconds-in-one-minutes 60]
    (* nb-seconds-in-one-minutes nb-minutes)
    ))

(fact (minutes-to-seconds 5) => 300)
(fact (minutes-to-seconds 10) => 600)

                                        ; convert seconds to weeks

; first version
(defn seconds-to-weeks "Converts seconds to weeks"
  [nb-seconds]
  (let [nb-minutes (/ 60)
        nb-hours (/ 60)
        nb-days  (/ 24)
        nb-weeks (/ 7)
       ]
       (* nb-seconds nb-minutes nb-hours nb-days nb-weeks)
       )
  )

; 7 days 
(fact (seconds-to-weeks 604800) => 1)

; second version
(defn seconds-to-weeks2 "Converts seconds to weeks"
  [nb-seconds]
  (let [nb-minutes (/ nb-seconds 60)
        nb-hours (/ nb-minutes 60)
        nb-days (/ nb-hours 24)
        nb-weeks (/ nb-days 7)]
    nb-weeks))

(fact (seconds-to-weeks2 604800) => 1)
(fact (seconds-to-weeks2 1209600) => 2)

                                        ; Looping and recursion

                                        ; absolute value of a number

(defn abs "Compute the absolute value of a number"
  [x]
  (if (< x 0) (* x -1) x))

(fact (abs -10) => 10)
(fact (abs 10) => 10)

                                        ; average number of two arguments

(defn avg "average number of two arguments"
  [x y]
  (/ (+ x y) 2))

(fact (avg 10 5) => 15/2)
(fact (avg 10 6) => 8)

                                        ; is the result close enough
                                        ; of the square root of a number

(defn good-enough? "Is the result close enough of the square root of the number"
  [number guess]
  (let [diff (- (* guess guess) number) epsi 0.0001]
    (if (< ( abs diff) epsi)
      true
      false)
    )
  )

(fact (good-enough? 25 5) => true)
(fact (good-enough? 25 4) => false)
(fact (good-enough? 25 4.9999999) => true)

                                        ; square root

(defn sqrt "Square root"
  ([number] (sqrt number 1.0) )
  ([number guess]
     (if (good-enough? number guess)
       guess
       (sqrt number (avg guess (/ number guess)))))
  )

;.;. This is the future you were hoping for. -- @Vaguery
(fact (good-enough? 25 (sqrt 25)) => true)
(fact (good-enough? 16 (sqrt 16)) => true)
(fact (sqrt 1) => 1)





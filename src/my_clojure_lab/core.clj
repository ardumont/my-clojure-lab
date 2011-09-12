(ns my-clojure-lab.core
  (:use [clojure.test               :only [run-tests]])
  (:use [midje.sweet])
  (:use [clojure.contrib.repl-utils :only [show]])
  (:use [clojure.pprint             :only [pprint]])
  (:use [clojure.walk               :only [macroexpand-all]]))

                                        ; example on how to declare a function
(def my-mult (fn [x y] (* x y)))

                                        ; another way of declaring functions
(defn sq
  "squares the provided argument"
  [x]
  (* x x))
                                        ; another one with multiple implementations
                                        ; depending on the number of arguments (fixed)
(defn square-or-multiply
  "squares the simple provided argument, multiply the 2 arguments provided"
  ([] 0)
  ([x] (* x x))
  ([x y] (* x y)))

                                        ; this time with an indefinite number of arguments

(defn add-arg-count
  "Returns the sum of the first argument and the number additionals arguments"
  [first & more]
  (+ first (count more)))

                                        ; square function with shorthand function declaration
(def sq2 #(* % %))

                                        ; shorthand function declaration
(def my-mult2 #(* %1 %2))

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
                                        ; Local bindings

(fact (let [ a 2 b 3] (+ a b)) => 5)

                                        ; convert minutes to seconds
(defn minutes-to-seconds
  "Converts minutes to seconds"
  [nb-minutes]
  (let [nb-seconds-in-one-minutes 60]
    (* nb-seconds-in-one-minutes nb-minutes)
    ))

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

; second version
(defn seconds-to-weeks2 "Converts seconds to weeks"
  [nb-seconds]
  (let [nb-minutes (/ nb-seconds 60)
        nb-hours (/ nb-minutes 60)
        nb-days (/ nb-hours 24)
        nb-weeks (/ nb-days 7)]
    nb-weeks))

                                        ; Looping and recursion

                                        ; absolute value of a number

(defn abs "Compute the absolute value of a number"
  [x]
  (if (< x 0) (* x -1) x))

                                        ; average number of two arguments

(defn avg "average number of two arguments"
  [x y]
  (/ (+ x y) 2))

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

                                        ; square root

                                        ; tail recursion because, for
                                        ; the resolution, it only
                                        ; returns the result
(defn sqrt "Square root"
  ([number] (sqrt number 1.0) )
  ([number guess]
     (if (good-enough? number guess)
       guess
       (sqrt number (avg guess (/ number guess)))))
  )

                                        ; exponent

                                        ; no tail recursion because there is computing
                                        ; at the end
(defn
  pow "Calculates a number to a provided exponent"
  ([x y]
     (if (<= (- y 1) 0)
       x
       (* x (pow x (- y 1))))
     )
  )

                                        ; zero?

; Is the parameter provided zero?
;(def
;  zero?
; #(= 0 %))

; may be missing a lib because i think zero? is not known
(defn
  pow2
  "Calculates a number to a provided exponent"
  [x y]
  (if (zero? y)
    1
    (* x (pow2 x (- y 1))))
  )

                                        ; add-up: add up all the number to a given limit

; naive add-up, no tail recursion
(defn
  add-up
  "Add up all the number to a given limit"
  [limit]
  (if (zero? limit)
    limit
    (+ limit (add-up (- limit 1))))
  )

(fact (add-up 4) => 10)
(fact (add-up 5) => 15)
(fact (add-up 6) => 21)
(fact (add-up 500) => 125250)

                                        ; less naive version without
                                        ; tail recursion but prepared to use it

(defn
  add-up2
  "Add up all the number to a given limit"
  ([limit] (add-up2 limit 0 0))
  ([limit count sum]
     (if (< limit count)
       sum
       (add-up2 limit (+ count 1) (+ sum count))))
  )

(fact (add-up2 4) => 10)
(fact (add-up2 5) => 15)
(fact (add-up2 6) => 21)
(fact (add-up2 500) => 125250)


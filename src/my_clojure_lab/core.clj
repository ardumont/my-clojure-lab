(ns my-clojure-lab.core
  (:use [clojure.test               :only [run-tests]])
  (:use [midje.sweet])
  (:use [clojure.contrib.repl-utils :only [show]])
  (:use [clojure.pprint             :only [pprint]])
  (:use [clojure.walk               :only [macroexpand-all]]))

(fact (+ 1 1) => 2)
(fact (* 1 1) => 1)

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

(fact (seconds-to-weeks 604800) => 1)

; second version
(defn seconds-to-weeks2 "Converts seconds to weeks"
  [nb-seconds]
  (let [nb-minutes (/ nb-seconds 60)
        nb-hours (/ nb-minutes 60)
        nb-days (/ nb-hours 24)
        nb-weeks (/ nb-days 7)]
    nb-weeks))

; 7 days 
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

(fact (good-enough? 25 (sqrt 25)) => true)
(fact (good-enough? 16 (sqrt 16)) => true)
(fact (sqrt 1) => 1)
(fact (sqrt 25) => (roughly 5))
;.;. The sum of wisdom is that time is never lost that is devoted to
;.;. work. -- Emerson
(fact (sqrt 16) => (roughly 4))
(fact (sqrt 100) => (roughly 10))

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

(fact (pow 1 1) => 1)
(fact (pow 1 0) => 1)
(fact (pow 2 3) => 8)
(fact (pow 2 10) => 1024)

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

(fact (pow2 1 1) => 1)
(fact (pow2 1 0) => 1)
(fact (pow2 2 3) => 8)
(fact (pow2 2 10) => 1024)

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
; this one cannot pass
;(fact (add-up 50000) => 1250025000)

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
; this one cannot pass
;(fact (add-up2 50000) => 1250025000)

                                        ; with tail recursion but prepared to use it

(defn
  add-up3
  "Add up all the number to a given limit"
  ([limit] (add-up3 limit 0 0))
  ([limit count sum]
     (if (< limit count)
       sum
       (recur limit (+ count 1) (+ sum count))))
  )

(fact (add-up3 4) => 10)
(fact (add-up3 5) => 15)
(fact (add-up3 6) => 21)
(fact (add-up3 500) => 125250)
(fact (add-up3 50000) => 1250025000)

                                        ; loop

(loop [i 0]
  (if (= i 10)
    i
    (recur (+ i 1))) )

                                        ; loop-sqrt

(defn loop-sqrt
  "returns the square root of the supplied number"
  [number]
  (loop [guess 1.0]
    (if (good-enough? number guess)
      guess
      (recur (avg guess (/ number guess))))))

(fact (loop-sqrt 1) => 1)
(fact (loop-sqrt 4) => (roughly 2))
(fact (loop-sqrt 25) => (roughly 5))
(fact (loop-sqrt 16) => (roughly 4))
(fact (loop-sqrt 100) => (roughly 10))

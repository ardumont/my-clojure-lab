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

;(fact (doc sq) => nil)

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

                                        ; do

;(do
;  (println "hello")
;  (println "from")  
;  (println "side effects")
;  (+ 5 5))

                                        ; high order function
(defn arg-switch
  "Applies the supplied function to the arguments in both possible orders. "
  [fun arg1 arg2]
  (list (fun arg1 arg2) (fun arg2 arg1)))

(fact (arg-switch / 1 2) => [1/2 2]) 
(fact (arg-switch + 1 2) => [3 3])

                                        ; Producing first class functions

                                        ; mine
(defn rangechecker
  "Returns a function that determines if a number is in a provided range."
  [min max]
  (fn
    [num]
    (not (or (< num min) (> num max))))
  )

(def myrange-5-10 (rangechecker 5 10))

(fact (myrange-5-10 4)=> false)
(fact (myrange-5-10 5)=> true)
(fact (myrange-5-10 6)=> true)
(fact (myrange-5-10 7)=> true)
(fact (myrange-5-10 8)=> true)
(fact (myrange-5-10 9)=> true)
(fact (myrange-5-10 10)=> true)
(fact (myrange-5-10 11)=> false)

; theirs
(defn rangechecker2
  "Returns a function that determines if a number is in a provided range."
  [min max]
  (fn
    [num]
    (and (<= min num) (<= num max)))
  )

(def myrange-1-2 (rangechecker2 1 2))

(fact (myrange-1-2 0)=> false)
(fact (myrange-1-2 1)=> true)
(fact (myrange-1-2 2)=> true)
(fact (myrange-1-2 3)=> false)

                                        ; Using partial to curry functions

(def times-pi (partial * 3.14))

(fact (times-pi 1) => 3.14)
(fact (times-pi 2) => 6.28)

                                        ; Using comp to compose functions

(def my-fn (comp - *))

(fact (my-fn 5 3) => -15)
(fact (my-fn 1 1) => -1)

(def my-fn2 (comp * -))

;(fact (my-fn2 5 3) => 30)

                                        ; common numeric functions

                                        ; /
(fact (/ 0 10) => 0) 
(fact (/ 10) => 1/10)
(fact (/ 1.0 10) => 0.1)

                                        ; +
(fact (+ 1 2) => 3)
(fact (+ 0 1) => 1)
(fact (+ 1 2 3) => 6)

                                        ; -

(fact (- 1 2 3) => -4)
(fact (- 5 2 3) => 0)

                                        ; *

(fact (* 1 2 3) => 6)
(fact (* 0 1 2 3 4) => 0)

                                        ; inc

(fact (inc 0) => 1)

                                        ; dec
(fact (dec 0) => -1)
(fact (dec 10) => 9)

                                        ; quot -> quotient

(fact (quot 5 2 ) => 2)
(fact (quot 10 2 ) => 5)
(fact (quot 13 2) => 6)

                                        ; rem -> remainder -> modulus

(fact (rem 5 2) => 1)
(fact (rem 10 2) => 0)
(fact (rem 128 3) => 2)

                                        ; min

(fact (min 10 20 1) => 1)
(fact (min 1 0 10) => 0)
(fact (min 9 200 199) => 9)

                                        ; max

(fact (max 10 20 1) => 20)
(fact (max 1 0 10) => 10)
(fact (max 9 200 199) => 200)

                                        ; equals

(fact (== 0 0) => true)
(fact (== 1 0) => false)
(fact (== 10.0 10) => true)

                                        ; <

(fact (< 10 20) => true)
(fact (< -1 0 ) => true)
(fact (< 1 0) => false)

(fact (< 10 2 0) => false)
(fact (< 10 20 30) => true)
(fact (< 10 10 30) => false)

                                        ; <=

(fact (<= 10 10 30) => true)
(fact (<= 10 5 30) => false)

                                        ; etc...

                                        ; zero? -> is the value equals
                                        ; to 0

(fact (zero? 0) => true)
(fact (zero? 1) => false)

                                        ; pos? -> positive

(fact (pos? 10) => true)
(fact (pos? -10) => false)
(fact (pos? 0) => false)

                                        ; neg? -> negative

(fact (neg? 0) => false)
(fact (neg? 10) => false)
(fact (neg? -10) => true)

                                        ; number?

(fact (number? 10) => true)
(fact (number? "hello de lu!") => false)

                                        ; string

                                        ; str -> concatenate
(fact (str) => "")
(fact (str "Hello" ", " "I'm Tony") => "Hello, I'm Tony")
(fact (str "I have " 1 " apple.") => "I have 1 apple.")

                                        ; subs -> substring
(fact (subs "first is a string" 6) => "is a string" )
(fact (subs "first is a string" 6 8) => "is" )

                                        ; string?

(fact (string? "toto") => true)
(fact (string? 10) => false)
(fact (string? 0) => false)

                                        ; Regular expressions functions

                                        ; re-pattern, to define a regexp
(re-pattern " [A-Za-z]*")
                                        ; other syntax -> macro
#" [A-Za-z]*"

                                        ; re-matches -> to check a
                                        ; string with a regexp
(fact (re-matches #"[A-Za-z]*" "test") => "test")
(fact (re-matches #"[A-Za-z]*" "test123") => nil)
(fact (re-matches #"[A-Za-z0-9]*" "test123") => "test123")

                                        ; re-matcher, to reuse a pattern
(def my-matcher (re-matcher #"[A-Za-z]*" "test"))

                                        ; re-find
(fact (re-find my-matcher) => "test")
(fact (re-find my-matcher) => "")
(fact (re-find my-matcher) => nil)

                                        ; re-seq

(fact (re-seq #"[a-z]" "test") => ["t" "e" "s" "t"])

                                        ; collections

                                        ; lists
; to create a list
(fact (list 12 3) => '(12 3))
(fact '(1 2 3) => '(1 2 3))

(fact (= '(1 2) '(1 2)) => true)
(fact (= '(1 2) '(1 2 3)) => false)
(fact (= '(1 2) '(2 1)) => false)

; peek - return the first elt in the list
(fact (peek '(1 2 3)) => 1)

                                        ; pop

;.;. Whoever wants to reach a distant goal must take small steps. --
;.;. fortune cookie
(fact (pop '(1 2 3)) => '(2 3))
(fact (comp (pop (pop '(1 2 3)))) => '(3))

                                        ; list?

(fact (list? 10) => false)
(fact (list? '(1 2)) => true)

                                        ; vector
(def vectorx [10 9 8])

(fact (vectorx 0) => 10)
(fact (vectorx 1) => 9)
(fact (vectorx 2) => 8)

                                        ; vector
(def nums (vector 1 3 2))

(fact (nums 0) => 1)
(fact (nums 1) => 3)
(fact (nums 2) => 2)

                                        ; vec -> convert any form
                                        ; passed to the method into a
                                        ; new vector with the same content

(fact (vec '(1 2 3)) => [1 2 3])

                                        ; get

(fact (get ["one" "two" "three"] 1) => "two")

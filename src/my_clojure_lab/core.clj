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


                                        ; peek, for a vector, takes
                                        ; the last arg of the vector
                                        ; because it always adresses
                                        ; the most efficient access

(fact (peek [1 2 3]) => 3)
(fact (peek [1 2 30]) => 30)

                                        ;vector?

(fact (vector? [1]) => true)
(fact (vector? 1) => false)

                                        ; conjoin -> ~union of sets
                                        ; conj, vector then any nuber of arguments

(fact (conj [1 2 3] 6 5 4) => [1 2 3 6 5 4])

                                        ; assoc -> associate to a
                                        ; vector a new value at the given index

(fact (assoc [1 2 3] 1 "new value") => [1 "new value" 3])

(def vectory [3 2 1])

                                        ; as vectory is immutable, we can call
                                        ; multiple times the assoc
                                        ; method and have the same result

(fact (assoc vectory 0 "test immutable") => ["test immutable" 2 1])
(fact (assoc vectory 0 "test immutable") => ["test immutable" 2 1])

                                        ; pop
(fact (pop [3 2 1 0]) => [3 2 1])
(fact (comp (pop (pop [3 2 1 0]))) => [3 2])

                                        ; subvec -> subvector

                                        ; by default, if only one
                                        ; index, the second index is
                                        ; the last

; no second inde
(fact (subvec [0 1 2 3 4 5] 2) => [2 3 4 5])
; same version with the last index
(fact (subvec [0 1 2 3 4 5] 2 6) => [2 3 4 5])
; only one entry is taken to create the new vector
(fact (subvec [0 1 2 3 4 5] 2 3) => [2])
; empty vector
(fact (subvec [0 1 2 3 4 5] 0 0) => [])

                                        ; map

(def maptest {:a 10 :b "toto"})

(fact (maptest :a) => 10)
(fact (maptest :b) => "toto")

(fact (hash-map :a 1 :b 2 :c "test") => {:a 1 :b 2 :c "test"})
(fact (sorted-map :a 1 :b 2 :c "test") => {:a 1 :b 2 :c "test"})

                                        ; struct-map

(defstruct person :first-name :last-name)

(def person1 (struct-map person :first-name "antoine" :last-name "dumont"))
(def person2 (struct-map person :first-name "denis" :last-name "labaye"))

(fact (person1 :first-name) => "antoine")

                                        ; accessor for struct-map

(def get-first-name (accessor person :first-name))

(fact (get-first-name person1) => "antoine")

                                        ; assoc -> association

(fact (assoc {:a 1 :b 2 :c 3} :d 4) => {:a 1 :b 2 :c 3 :d 4})
(fact (assoc {:a 1 :b 2 :c 3} :d 4 :e 5) => {:a 1 :b 2 :c 3 :d 4 :e 5})

                                        ; dissoc -> dissociation

(fact (dissoc {:a 1 :b 2 :c 3 :d 4} :d :c) => {:a 1 :b 2})

                                        ; conj

; conj with map
(fact (conj {:a 10 :b "toto"} {:a 150 :etiq "tata"}) => {:a 150 :b "toto" :etiq "tata"})

; with vectors
(fact (conj {:a 10 :b "toto"} [:e 150] [:et "tata"]) => {:a 10 :b "toto" :e 150 :et "tata"})

                                        ; merge

(fact (merge {:a 10 :b 20} {:c 150 :toto 400}) => {:a 10 :b 20 :c 150 :toto 400})

                                        ; merge-with

(fact (merge-with + {:a 10 :b 150} {:a 20 :c 3}) => {:a 30 :b 150 :c 3})

                                        ; get

(fact (get {:q 1 :w 2 :e 3} :q) => 1)
(fact (get {:q 1 :w 2 :e 3} :a) => nil)
(fact (get {:q 1 :w 2 :e 3} :a 0) => 0)

                                        ; contains? Does the map
                                        ; contains the keyword

(fact (contains? {:a 10 :b 20} :a) => true)
(fact (contains? {:a 10 :b 20} :c) => false)

                                        ; map? -> is the argument a map?

(fact (map? {:a 10}) => true)
(fact (map? 10) => false)

                                        ; keys -> return all the keys of the map

(fact (keys {:a 0 :b 2 :c 4}) => '(:a :b :c))

                                        ; vals -> return all the values of the map

(fact (vals {:a 0 :b 2 :c 4}) => '(0 2 4))

                                        ; sets

(def languages-string #{"c++" "c" "java" "clojure"})
(def languages-keyword #{:c++ :c :java :clojure})

                                        ; define a hashset
(def languages-string (hash-set "c++" "c" "clojure" "java"))

                                        ; define a sorted set
(def languages-string (sorted-set "c++" "c" "clojure" "java"))

; access to a data in the set
(fact (languages-keyword :c++) => :c++)
(fact (languages-string "clojure") => "clojure")
(fact (languages-string "inexistent language") => nil)

                                        ; common set functions

                                        ; union
(fact (clojure.set/union #{:a :b} #{:c :d} #{:last}) => #{:a :b :c :d :last})
(fact (clojure.set/union #{:a :b} #{}) => #{:a :b})

                                        ; intersection
(fact (clojure.set/intersection #{:a :b} #{:b :c :d}) => #{:b})
(fact (clojure.set/intersection #{:a :b} #{:c :d}) => #{})
(fact (clojure.set/intersection #{:a :b} #{:b :c :d} #{"test"}) => #{})
(fact (clojure.set/intersection #{:a :b} #{:b :c :d} #{"test" :b}) => #{:b})

                                        ; difference -> first set -
                                        ; other sets
(fact (clojure.set/difference #{:a :b} #{:b :c :d}) => #{:a})
(fact (clojure.set/difference #{:a :b} #{:b :c :d} #{:last :a}) => #{})

                                        ; sequence - abstractiun layer
                                        ; for collections
                                        ; first works for all collections
; list
(fact (first '(1 3 2)) => 1)
; vector
(fact (first [10 20 30]) => 10)
; set
(fact (first #{100 200 300}) => 100)
; map
(fact (first { :a 100 :efg 3 :cdh 2}) => [:a 100])

                                        ; rest -> all but the first element of the collection

; list
(fact (rest '(1 3 2)) => '(3 2))
; vector
(fact (rest [10 20 30]) => [20 30])
; set
(fact (rest #{100 200 300}) => [200 300])
; map
(fact (rest {:a 100 :efg 3 :cdh 2}) => '([ :efg 3] [ :cdh 2]))

; rest and first use
(defn printall [s]
  (if (not (empty? s))
    (do
      (println "Item:" (first s))
      (recur (rest s))
      )
    )
  )

(printall '(1 3 4 5))
(printall ["hello" "the" "vector" 10])
(printall {:1 "esrtsd" :3 4 :5 100})
(printall "hello you")

                                        ; construct sequence

; cons - construct a sequence
(fact (cons 4 '(1 2 3)) => '(4 1 2 3))

; conj
(fact (conj '(1 2 3) 4) => '(4 1 2 3))
(fact (conj [1 2 3] 4) => [1 2 3 4])

(defn make-int-seq
  [max]
  (loop [acc nil cnt max]
    (if (zero? cnt)
      acc
      (recur (cons cnt acc) (dec cnt)))
    ))

(fact (make-int-seq 5) => '(1 2 3 4 5))

                                        ; lazy sequence manually

(defn lazy-counter [base increment]
  (lazy-seq
   (cons base (lazy-counter (+ base increment) increment))))

(fact (take 3 (lazy-counter 0 2)) => '(0 2 4))
(fact (nth (lazy-counter 2 3) 1000000) => 3000002)

                                        ; lazy sequence with sequence generator

(def integers-from-1 (iterate inc 1))

(fact (take 3 integers-from-1) => '(1 2 3))

(def integers-from-0 (iterate inc 0))

(fact (take 3 integers-from-0) => '(0 1 2))

(defn lazy-counter-iterate [base increment]
  (iterate #(+ % increment) base)
  )

(fact (take 3 (lazy-counter-iterate 0 2)) => '(0 2 4))
(fact (nth (lazy-counter-iterate 2 3) 1000000) => 3000002)


                                        ; seq

(fact (seq '(1 2 3)) => '(1 2 3))
(fact (seq {:a 1 :b 2 :c 3}) => '([:a 1] [:b 2] [:c 3]))

                                        ; vals

(fact (vals {:a 1 :b 2 :c 3}) => '(1 2 3))

; keys

(fact (keys {:a 1 :b 2 :c 3}) => '(:a :b :c))

                                        ; rseq -> reverse the sequence

(fact (rseq [ 1 2 3]) => [ 3 2 1])

                                        ; lazy-seq -> to construct lazy sequence

                                        ; repeatedly

(fact (take 3 (repeatedly (fn [] "hi")) ) => '( "hi" "hi" "hi") )

; (rand-int 5) => return a random number between 0 and 4

; iterate -> take a one argument function with a value
; return an infinite sequence

(fact (take 3 (iterate inc 0) ) => '(0 1 2))

                                        ; repeat -> return a lazy sequence

(fact (take 5 (repeat "hello")) => '("hello" "hello" "hello" "hello" "hello"))

(fact (repeat 5 "hello") => '("hello" "hello" "hello" "hello" "hello"))

                                        ; range

(fact (range 5) => '(0 1 2 3 4))
(fact (range 5 10) => '(5 6 7 8 9))

(fact (range 5 10 2) => '(5 7 9))

                                        ; distinct

(fact (distinct '(3 3 3 1 1 1 2 2 2 2 2 3 3 3 4 5 6 7 7 7 1)) => '(3 1 2 4 5 6 7))

                                        ; filter -> filter with the
                                        ; predicate function passed as
                                        ; arguments

(fact (filter #(= % true) '(1 2 3 true false true "test")) => '(true true))
(fact (filter
       (fn [s] (= \b (first s)))
       '("abe" "bees" "boy" "false" "book"))
  => '("bees" "boy" "book"))
(fact (filter
       #(= \b (first %))
       '("abe" "bees" "boy" "false" "book"))
  => '("bees" "boy" "book"))

                                        ; remove -> lazy sequence
                                        ; complements of filter

(fact (remove #(= % true) '(1 2 3 true false true "test"))
  => '(1 2 3 false "test"))
(fact (remove
       (fn [s] (= \b (first s)))
       '("abe" "bees" "boy" "false" "book"))
  => '("abe" "false"))
(fact (remove
       #(= \b (first %))
       '("abe" "bees" "boy" "false" "book"))
  => '("abe" "false"))

                                        ; cons

(fact (cons 1 [2 3 4]) => [1 2 3 4])

                                        ; concat

(fact (concat [1 2 3] '(9 8 74) [100]) => [1 2 3 9 8 74 100])

                                        ; lazy-cat <=> lazy concat

(fact (lazy-cat [1 2 3] '(9 8 74) [100]) => [1 2 3 9 8 74 100])

                                        ; mapcat

; map returns list of results in list
(fact (map #(repeat % %) '(1 2 3)) => ['(1) '(2 2) '(3 3 3)]) 

; mapcat returns the merge of the results into a single list
(fact (mapcat #(repeat % %) '(1 2 3)) => '(1 2 2 3 3 3))

                                        ; cycle

(fact (take 3 (cycle [1 2 3])) => [1 2 3])
(fact (take 5 (cycle [1 2 3])) => [1 2 3 1 2])
(fact (take 10 (cycle [10 9])) => [10 9 10 9 10 9 10 9 10 9])

                                        ; interleave

(fact (interleave [1 2 3] '(:a :b :c)) => '(1 :a 2 :b 3 :c))
(fact (interleave [1 2] '(:a :b :c)) => '(1 :a 2 :b))
(fact (interleave [1 2 3] '(:a :c)) => '(1 :a 2 :c))

(fact (interleave
       '("qa" "faq" "test")
       [1 2]
       [:test :an false]) => ["qa" 1 :test "faq" 2 :an])

                                        ; interpose

(fact (interpose "test" '(1 2 3)) => '(1 "test" 2 "test" 3))

                                        ; rest

(fact (rest [1 2 3]) => [2 3])
(fact (rest (rest [1 2 3])) => [3])
(fact (rest []) => '())

                                        ; next

(fact (next '(1 2 3)) => '(2 3))
(fact (next (next '(1 2 3))) => '(3))
(fact (next []) => nil)

                                        ; drop

(fact (drop 2 [1 2 3]) => [3])
;.;. One of the symptoms of an approaching nervous breakdown is the belief
;.;. that one's work is terribly important. -- Russell
(fact (drop 10 [1 2 3]) => [])

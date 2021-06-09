(ns com.phronmophobic.mobiletest)

(defn clj_sub [a b]
  (- a b))

(defn clj_add [a b]
  (+ a b))


(defn compile-interface-class
  ([]
   (compile-interface-class nil))
  ([opts]
   (with-bindings {#'*compile-path* "library/classes"}
     ((requiring-resolve 'tech.v3.datatype.ffi.graalvm/expose-clojure-functions)
      ;;name conflict - initialize is too general
      { ;;#'initialize-avclj {:rettype :int64}
       #'clj_sub {:rettype :int64
                  :argtypes [['a :int64]
                             ['b :int64]
                             ]}

       #'clj_add {:rettype :int64
                 :argtypes [['a :int64]
                            ['b :int64]]}

       }
      'com.phonemophobic/MobileTest nil)))
  )


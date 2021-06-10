(ns com.phronmophobic.mobiletest
  (:require [tech.v3.datatype.ffi :as dt-ffi]
            [sci.core :as sci])
  (:gen-class))



(defn clj_sub [a b]
  (- a b))

(defn clj_add [a b]
  (+ a b))

(defn clj_print [bs]
  (when bs
    (prn (dt-ffi/c->string bs))))

(defn clj_eval [bs]
  (when bs
    (prn (sci/eval-string (dt-ffi/c->string bs)))))

(defn clj_print_hi []
  (println "hi"))


(defn -main [& args]
  )

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

       #'clj_print {:rettype :void
                    :argtypes [['bs :pointer]]}

       #'clj_eval {:rettype :void
                    :argtypes [['bs :pointer]]}

       #'clj_print_hi {:rettype :void
                       :argtypes []}

       }
      'com.phonemophobic/MobileTest nil)))
  )


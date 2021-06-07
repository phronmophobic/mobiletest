(ns com.phronmophobic.mobiletest)

(defn clj-sub [a b]
  (- a b))


(comment
  (do
    (require '[tech.v3.datatype.ffi.graalvm :as graalvm])
    (with-bindings {#'*compile-path* "library/classes"}
      (graalvm/expose-clojure-functions
       ;;name conflict - initialize is too general
       {;;#'initialize-avclj {:rettype :int64}
        #'clj-sub {:rettype :int64
                   :argtypes [['a :int64]
                              ['b :int64]
                              ;['out-fname :pointer]
                              ;['input-pixfmt :pointer]
                              ]}
        ;; #'is-avclj-initialized {:rettype :int64}
        ;; #'make-h264-encoder {:rettype :int64
        ;;                      :argtypes [['height :int64]
        ;;                                 ['width :int64]
        ;;                                 ['out-fname :pointer]
        ;;                                 ['input-pixfmt :pointer]]}
        ;; #'encode-frame {:rettype :int64
        ;;                 :argtypes [['encoder :int64]
        ;;                            ['frame-data :pointer]
        ;;                            ['frame-data-len :int64]]}
        ;; #'close-encoder {:rettype :int64
        ;;                  :argtypes [['encoder :int64]]}
        } 
       'com.phonemophobic/MobileTest nil)))
  )

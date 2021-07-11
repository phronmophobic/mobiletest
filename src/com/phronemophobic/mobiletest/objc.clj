(ns com.phronemophobic.mobiletest.objc
  (:require [tech.v3.datatype.ffi :as dt-ffi]
            [tech.v3.datatype :as dtype]
            tech.v3.datatype.ffi.graalvm-runtime
            [tech.v3.datatype.native-buffer :as native-buffer])
  
  (:import org.graalvm.nativeimage.c.function.CEntryPointLiteral)
  (:gen-class))

;; https://developer.apple.com/documentation/objectivec/objective-c_runtime?language=objc
;; https://developer.apple.com/tutorials/data/documentation/objectivec/objective-c_runtime.json?language=objc

;; blocks
;; https://www.galloway.me.uk/2012/10/a-look-inside-blocks-episode-1/

;; id objc_getClass(const char *name);

;; id class_createInstance(Class cls, size_t extraBytes);
;; Class NSClassFromString(NSString *aClassName);

(set! *warn-on-reflection* true)

(def objclib-fns
  {:objc_msgSend {:rettype :int64
                  :argtypes [['obj :pointer]
                             ['sel :pointer]]}

   :objc_make_selector {:rettype :pointer
                        :argtypes [['sel :pointer]]}
   :objc_make_string {:rettype :pointer
                      :argtypes [['s :pointer]]}

   :call_clj_fn {:rettype :void
                 :argtypes [['fptr :pointer]]}
   ,})

(defonce ^:private lib (dt-ffi/library-singleton #'objclib-fns))
(defn set-library-instance!
  [lib-instance]
  (dt-ffi/library-singleton-set-instance! lib lib-instance))

(dt-ffi/library-singleton-reset! lib)

(defn- find-fn
  [fn-kwd]
  (dt-ffi/library-singleton-find-fn lib fn-kwd))

(defmacro check-error
  [fn-def & body]
  `(let [error-val# (long (do ~@body))]
     (errors/when-not-errorf
      (>= error-val# 0)
      "Exception calling: (%d) - \"%s\""
      error-val# (if-let [err-name#  (get av-error/value->error-map error-val#)]
                   err-name#
                   (str-error error-val#)))
     error-val#))


(dt-ffi/define-library-functions com.phronemophobic.mobiletest.objc/objclib-fns find-fn check-error)

(defmacro if-class
  ([class-name then]
   `(if-class ~class-name
      ~then
      nil))
  ([class-name then else?]
   (let [class-exists (try
                        (Class/forName (name class-name))
                        true
                        (catch ClassNotFoundException e
                          false))]
     (println "class exists: " class-name class-exists)
     (if class-exists
       then
       else?))))

(def initialized?* (atom false))

(defn initialize-objc
  []
  (println "initializing")
  (if-class com.phronemophobic.objc.Bindings
    (if (first (swap-vals!
                initialized?*
                (fn [init]
                  (when-not init
                    (set-library-instance! (com.phronemophobic.objc.Bindings.))
                    (println "instance set!")
                    true))))
      (do (println "initialized")
          1)
      (do
        (println "initialize failed")
        0))))

(defn compile-bindings [& args]
  ;;(require '[tech.v3.datatype.ffi.graalvm :as graalvm])
  (with-bindings {#'*compile-path* "generated_classes"}
    ((requiring-resolve 'tech.v3.datatype.ffi.graalvm/define-library)
     objclib-fns
     nil
     { ;;:header-files ["<skia.h>"]
      :libraries ["@rpath/libcljbridge.so"]
      :classname 'com.phronemophobic.objc.Bindings})))



(comment

  ;; experimenting with potential
  ;; objc interop syntax
  (def CMMotionManager (class 'CMMotionManager))
  (objc NSString +stringWithFormat:)

  (let [motion ((alloc CMMotionManager) :init)]
    (when (motion :accelerometerAvailable)
      )))



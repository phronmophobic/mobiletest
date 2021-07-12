(ns com.phronemophobic.mobiletest.membrane
  (:require membrane.ios
            [membrane.ui :as ui]
            babashka.nrepl.server
            [sci.core :as sci]
            [sci.addons :as addons]
            [tech.v3.datatype.ffi :as dt-ffi]
            [tech.v3.datatype :as dtype])
  (:import java.net.NetworkInterface)
  (:gen-class))

(set! *warn-on-reflection* true)

(def main-view (atom nil))
(defonce debug-view (atom nil))
(defonce debug-log (atom []))

(defn sleep [msecs]
  (Thread/sleep msecs))

(defn ns->ns-map [ns-name]
  (let [fns (sci/create-ns ns-name nil)]
    {ns-name
     (reduce (fn [ns-map [var-name var]]
               (let [m (meta var)
                     no-doc (:no-doc m)
                     doc (:doc m)
                     arglists (:arglists m)]
                 (if no-doc ns-map
                     (assoc ns-map var-name
                            (sci/new-var (symbol var-name) @var
                                         (cond-> {:ns fns
                                                  :name (:name m)}
                                           (:macro m) (assoc :macro true)
                                           doc (assoc :doc doc)
                                           arglists (assoc :arglists arglists)))))))
             {}
             (ns-publics ns-name))}))

(def opts (addons/future
            {:namespaces
             (merge (let [ns-name 'com.phronemophobic.mobiletest.membrane
                          fns (sci/create-ns ns-name nil)]
                      {ns-name {'main-view (sci/copy-var main-view fns)
                                'debug-view (sci/copy-var debug-view fns)
                                'debug-log (sci/copy-var debug-log fns)}})

                    (let [ns-name 'clojure.core
                          fns (sci/create-ns ns-name nil)]
                      {ns-name {'sleep (sci/copy-var sleep fns)
                                'main-view (sci/copy-var main-view fns)
                                'debug-view (sci/copy-var debug-view fns)
                                'debug-log (sci/copy-var debug-log fns)}})

                    (ns->ns-map 'membrane.ui))}))

(def sci-ctx (sci/init opts))

(defonce old-eval-msg babashka.nrepl.impl.server/eval-msg)


(defonce clear-future (atom nil))

(defn show-msg [msg]

  (when-let [fut @clear-future]
    (future-cancel fut)
    (reset! clear-future nil))

  (reset! debug-view
          (let [body (ui/padding
                      5 5
                      (ui/translate 10 60
                                    (ui/label (:code msg))))
                [w h] (ui/bounds body)]
            [(ui/with-color [1 1 1 0.8]
               (ui/rectangle w h))
             body]))

  (reset! clear-future
          (future
            (Thread/sleep 2000)
            (reset! debug-view nil))))

(comment
  (def server (babashka.nrepl.server/start-server! sci-ctx {:host "0.0.0.0" :port 23456}))
  (.close (:socket server))

  (require '[membrane.java2d :as backend])
  (backend/run #(deref debug-view))

  ,
)


(defn clj_init []
  (membrane.ios/initialize-ios)
  (babashka.nrepl.server/start-server! sci-ctx {:host "0.0.0.0" :port 23456
                                                :show-msg show-msg})
  (println "addresses: \n"
           (->> (NetworkInterface/getNetworkInterfaces)
                enumeration-seq
                (map #(.getInetAddresses ^NetworkInterface %))
                (mapcat enumeration-seq)
                (map #(str "\t" % "\n"))
                (clojure.string/join))))



(defn clj_draw [ctx]
  (try
    (membrane.ios/skia_clear ctx)
    (membrane.ios/draw! ctx @main-view)
    (membrane.ios/draw! ctx @debug-view)
    (catch Exception e
      (prn e))))

(defn clj_touch_ended [x y]
  (try
    (ui/mouse-down @main-view [x y])
    (catch Exception e
      (prn e))))

(defn clj_insert_text [ptr]
  (let [s (dt-ffi/c->string ptr)]
    (try 
      (ui/key-press @main-view s)
      (catch Exception e
        (prn e)))))

(defn clj_delete_backward []
  (try
    (ui/key-press @main-view :backspace)
    (catch Exception e
      (prn e))))


(defn -main [& args])

(defn compile-interface-class [& args]
  (with-bindings {#'*compile-path* "library/classes"}
    ((requiring-resolve 'tech.v3.datatype.ffi.graalvm/expose-clojure-functions)
     {#'clj_init {:rettype :void}
      #'clj_draw {:rettype :void
                       :argtypes [['skia-resource :pointer]]}
      #'clj_touch_ended {:rettype :void
                         :argtypes [['x :float64]
                                    ['y :float64]]}
      #'clj_delete_backward {:rettype :void}
      #'clj_insert_text {:rettype :void
                             :argtypes [['s :pointer]]}}
     
     
     'com.phronemophobic.mobiletest.membrane.interface nil)))

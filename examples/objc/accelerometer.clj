(ns accelerometer
  (:require [com.phronemophobic.clj-objc :as objc]
            [membrane.ui :as ui]))


(def motion (-> (objc/string->class "CMMotionManager")
                (objc/call-objc "alloc" :pointer)
                (objc/call-objc "init" :pointer)))

;;   [motion setAccelerometerUpdateInterval:updateInterval];
(objc/call-objc motion "setAccelerometerUpdateInterval:"
                :void
                :float64 0.5)

(def accelerations (atom []))

(def handler (objc/make-block
              (fn [data error]
                (swap! accelerations conj (acceleration->map data)))
              :void
              :pointer :pointer))

;; main_queue = [NSOperationQueue mainQueue];
(def main-queue (-> (objc/string->class "NSOperationQueue")
                    (objc/call-objc "mainQueue" :pointer)))

;; [motion startAccelerometerUpdatesToQueue:main_queue withHandler:handler];
(objc/call-objc motion "startAccelerometerUpdatesToQueue:withHandler:"
                :void
                :pointer main-queue
                :pointer handler)

(def scale 100)

(defn abs [x]
  (if (pos? x)
    x
    (- x)))

(defn show-accelerations [accelerations]
  (apply
   ui/vertical-layout
   (for [{:keys [x y z]} (take-last 90 accelerations)]
     (let [body (apply
                 ui/horizontal-layout
                 (for [[a color] [[x [1 0 0]]
                                  [y [0 1 0]]
                                  [z [0 0 1]]]]
                   (ui/filled-rectangle color
                                        (* (abs a) scale) 5)))
           [w h] (ui/bounds body)]
       body))))

(add-watch accelerations ::update-view
           (fn [_ _ old accelerations]
             (reset! main-view
                     (ui/translate 25 100
                                   (show-accelerations accelerations)))))

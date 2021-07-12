(ns gol)

(require '[membrane.ui :as ui])
(require '[com.phronemophobic.mobiletest.membrane :refer
           [main-view]])


(def state-atm
  (atom
   {:board #{[1 0] [1 1] [1 2]}}))

(defn neighbours [[x y]]
  (for [dx [-1 0 1] dy (if (zero? dx) [-1 1] [-1 0 1])] 
    [(+ dx x) (+ dy y)]))

(defn step [cells]
  (set (for [[loc n] (frequencies (mapcat neighbours cells))
             :when (or (= n 3) (and (= n 2) (cells loc)))]
         loc)))

(def grid-size 20)

(defn view [{:keys [board] :as state}]
  (ui/translate
   30 30
   (ui/on
    :mouse-down
    (fn [[x y]]
      (swap! state-atm update :board
             conj [(int (/ x grid-size))
                   (int (/ y grid-size))])
      nil
      )
    [(ui/spacer 600 600)
     (into []
           (map (fn [[x y]]
                  (ui/translate (* x grid-size) (* y grid-size)
                                (ui/rectangle grid-size grid-size))))
           board)]))
  )



(add-watch state-atm ::update-view (fn [k ref old updated]
                                     (reset! main-view (view updated))))

(swap! state-atm identity)

(defn run-gol [nsteps]
  (dotimes [i nsteps]
    (swap! state-atm
           update :board step)
    (sleep 500))
  )

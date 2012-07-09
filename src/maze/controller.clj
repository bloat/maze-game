(ns maze.controller
  (require [maze.core :as mz]))

(def solvers (atom {}))

(defn process-solver [name code]
  (swap! solvers assoc name {:fn (eval (read-string code))
                             :score (ref {:w 0 :l 0 :d 0})})
  ;; read errors, etc.
  ;; allowing more than one function
  (.toString @solvers))

(defn battle [s1 s2]
  (let [maze (mz/maze-gen)
        _ (mz/draw-maze maze)
        [_ path1] (mz/play-maze maze s1 0 5000 [])
        [_ path2] (mz/play-maze maze s2 0 5000 [])]
    (cond (= (count path1) (count path2)) 0
          (< (count path1) (count path2)) -1
          :else 1)))

(defn pick-player [players]
  (rand-nth (into [] players)))

(defn inc-win [stat] (update-in stat [:w] inc))
(defn inc-lose [stat] (update-in stat [:l] inc))
(defn inc-draw [stat] (update-in stat [:d] inc))

(defn choose-and-battle-and-update-stats! []
  (let [players @solvers]
    (when (not (empty? players))
      (let [[n1 {p1 :fn s1 :score}] (pick-player players)
            [n2 {p2 :fn s2 :score}] (pick-player players)]
        (when (not (= n1 n2))
          (let [result (battle p1 p2)]
            (cond
              (= 0 result)
              (dosync
               (commute s1 inc-draw)
               (commute s2 inc-draw))
              (= -1 result)
              (dosync
               (commute s1 inc-win)
               (commute s2 inc-lose))
              :else
              (dosync
               (commute s1 inc-lose)
               (commute s2 inc-win)))))))))

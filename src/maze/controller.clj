(ns maze.controller
  (require [maze.core :as mz]))

(def solvers (atom {}))

(defn process-solver [name code]
  (swap! solvers assoc name (eval (read-string code)))
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

(defn choose-and-battle [stats]
  (let [players @solvers]
    (if (empty? players)
      stats
      (let [[n1 p1] (pick-player players)
            [n2 p2] (pick-player players)]
        (if (= n1 n2)
          stats
          (let [result (battle p1 p2)]
            (cond
              (= 0 result)
              (update-in (update-in stats [n1 :d] inc) [n2 :d] inc)
              (= -1 result)
              (update-in (update-in stats [n1 :w] inc) [n2 :l] inc)
              :else
              (update-in (update-in stats [n2 :w] inc) [n1 :l] inc))))))))

      
  
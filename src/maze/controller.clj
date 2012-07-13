(ns maze.controller
  (use [maze play gen util])
  (import [java.util.concurrent Executors TimeUnit]))

(def solvers (atom {}))

(defn process-solver [name code]
  (try
    (let [to-eval (read-string code)]
      (try
        (let [new-solver (eval to-eval)]
          (try
            (battle new-solver new-solver)
            (swap! solvers assoc name {:fn (eval to-eval)
                                       :score (ref {:w 0 :l 0 :d 0})})
            [:success name to-eval]
            (catch Exception e [:test-error name to-eval e])))
        (catch Exception e [:eval-error name to-eval e])))
    (catch Exception e [:read-error name code e])))

(defn battle [s1 s2]
  (let [maze (maze-gen)
        [_ path1] (play-maze maze s1 0 5000 [])
        [_ path2] (play-maze maze s2 0 5000 [])]
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
            (condp = result
              0 (dosync
                 (commute s1 inc-draw)
                 (commute s2 inc-draw))
              -1 (dosync
                  (commute s1 inc-win)
                  (commute s2 inc-lose))
              1 (dosync
                 (commute s1 inc-lose)
                 (commute s2 inc-win)))))))))

(def no-threads 2)
(def executor (Executors/newFixedThreadPool no-threads))

(defn- run-battle-and-retrigger []
  (choose-and-battle-and-update-stats!)
  (.execute executor run-battle-and-retrigger))

(defn start []
  (dotimes [count no-threads]
    (.execute executor run-battle-and-retrigger)))

(defn stop []
  (.shutdown executor))
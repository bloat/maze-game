(ns maze.controller
  (use [maze play gen util])
  (import [java.util.concurrent Executors TimeUnit]))

(def solvers (atom {}))

(defn battle [s1 s2]
  (let [maze (maze-gen)
        [_ path1] (play-maze maze s1 0 5000 [])
        [_ path2] (play-maze maze s2 0 5000 [])]
    (cond (= (count path1) (count path2)) 0
          (< (count path1) (count path2)) -1
          :else 1)))

(defn get-solver-fn [solvers-val name]
  (:fn (solvers-val name)))

(defn assoc-new-solver [solvers name new-solver]
  (if (contains? solvers name)
    solvers
    (assoc solvers name {:fn new-solver :score (ref {:w 0 :l 0 :d 0})})))

(defn error-vector
  ([name message] [:error name message nil])
  ([name message e] [:error name message e]))

(defn dump-failed-ns
  ([name message] (dump-failed-ns name message nil))
  ([name message e]
     (remove-ns (symbol name))
     (error-vector name message e)))

(defn test-solver-and-stash [name new-solver data error-fn]
  (try
    (battle new-solver new-solver)
    (let [new-named-solver
          (get-solver-fn
           (swap! solvers assoc-new-solver name new-solver)
           name)]
      (if (identical? new-named-solver new-solver)
        [:success name data]
        (error-fn name ["That name is already taken." data])))
    (catch Exception e (error-fn name ["Problem when running your function." data] e))))

(defn process-solver [name code]
  (try
    (let [to-eval (read-string code)]
      (try
        (test-solver-and-stash name (eval to-eval) to-eval error-vector)
        (catch Exception e (error-vector name ["Problem when evaluating your function." to-eval] e))))
    (catch Exception e (error-vector name ["Problem when reading your input." code] e))))

(defn upload-solver [name {file :tempfile}]
  (if (find-ns (symbol name))
    [:error name "That name is already taken"]
    (try
      (load-file (.getAbsolutePath file))
      (let [new-ns (find-ns (symbol name))]
        (if new-ns
          (let [solver-fn (ns-resolve new-ns 'solver)]
            (if solver-fn
              (test-solver-and-stash name solver-fn solver-fn dump-failed-ns)
              (dump-failed-ns name "No function found called solver")))
          (error-vector name ["No namespace found called " name])))
      (catch Exception e (dump-failed-ns name file e)))))

(defn delete-solver [name]
  (swap! solvers dissoc name))

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

(def run-control (atom false))

(def no-threads 2)
(def executor (Executors/newFixedThreadPool no-threads))

(defn- run-battle-and-retrigger []
  (when @run-control
    (choose-and-battle-and-update-stats!)
    (.execute executor run-battle-and-retrigger)))

(defn start []
  (reset! run-control true)
  (dotimes [count no-threads]
    (.execute executor run-battle-and-retrigger)))

(defn stop [] (reset! run-control false))

(defn start-stop [action]
  (if (= :start action)
    (start)
    (stop)))

(defn is-running? [] @run-control)

;; This file is part of Amazing Dojo.

;; Amazing Dojo is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; Amazing Dojo is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with Amazing Dojo. If not, see <http://www.gnu.org/licenses/>.

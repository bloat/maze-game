(ns maze.gen
  (use [maze.util]))

(defn choose-from-c [c]
  (rand-nth (seq c)))

(defn choose-neighbour-from-candidates [cands]
  (when (seq cands)
    (rand-nth cands)))

(defn find-neighbour-not-in-c [cell c complete]
  (choose-neighbour-from-candidates (remove (into c complete) (neighbours cell))))

(defn make-path [grid cell-a cell-b]
  (conj grid [cell-a cell-b]))

;; Let cells be a list with one random cell in it.
;; Choose 1 cell c from cells and one neighbour of c not in cells.
;; if not possible - remove c from cells, 
;; else draw path between cells and add c to cells.
;; repeat until c is empty
(defn maze-gen
  "The growing tree algorithm for maze generation.
paths is a set of pairs of adjacent cells. Each pair indicates a path
between those two cells.  c is a set of cells currently under
consideration. To start with it contains one random cell.  complete is
a vector of cells which are completely specified."
  ([]
     (maze-gen #{} #{(rand-int 100)} []))
  ([paths c complete]
     (if (empty? c)
       paths
       (let [cell-from-c (choose-from-c c)
             neighbour (find-neighbour-not-in-c cell-from-c c complete)]
         (if neighbour
           (recur (make-path paths cell-from-c neighbour) (conj c neighbour) complete)
           (recur paths (disj c cell-from-c) (conj complete cell-from-c)))))))

(ns maze.core)


;; Let C be a list with one random cell in it.
;; Choose 1 cell from C and one neighbout not from C
;; if not possible - remove cell from C
;; else draw path between cells
;; repeat until c is empty

(defn top-neighbour [c]
  (when (> c 9)
    (- c 10)))

(defn bottom-neighbour [c]
  (when (< c 90)
    (+ c 10)))

(defn right-neighbour [c]
  (when (not (= 9 (rem c 10)))
    (inc c)))

(defn left-neighbour [c]
  (when (not (zero? (rem c 10)))
    (dec c)))

(defn neighbours [c]
  (filter identity [(top-neighbour c) (bottom-neighbour c) (right-neighbour c) (left-neighbour c)]))

(defn choose-from-c [c]
  (first c))

(defn choose-neighbour-from-candidates [cands]
  (when (seq cands)
    (first cands)))

(defn find-neighbour-not-in-c [cell c complete]
  (choose-neighbour-from-candidates (remove (into c complete) (neighbours cell))))

(defn make-path [grid cell-a cell-b]
  (conj grid [cell-a cell-b]))

(defn add-to-c [c cell]
  (conj c cell))

(defn remove-from-c [c cell]
  (disj c cell))

(defn maze-gen
  "The growing tree algorithm for maze generation."
  [grid c complete]
  (if (empty? c)
    grid
    (let [cell-from-c (choose-from-c c)
          neighbour  (find-neighbour-not-in-c cell-from-c c complete)]
      (if neighbour
        (recur (make-path grid cell-from-c neighbour) (add-to-c c neighbour) complete)
        (recur grid (remove-from-c c cell-from-c) (conj complete cell-from-c))))))

(defn h-path? [grid cell]
  (or (contains? grid [cell (inc cell)])
      (contains? grid [(inc cell) cell])))

(defn v-path? [grid cell]
  (or (contains? grid [cell (+ 10 cell)])
      (contains? grid [(+ 10 cell) cell])))

(defn draw-maze [grid]
  (println "*-*-*-*-*-*-*-*-*-*-*")
  (doseq [[cell horizontal] (mapcat (fn [r] (concat (map #(vector % true) r) (map #(vector % false) r))) (partition 10 (range 0 100)))]
    (if (zero? (rem cell 10))
      (print (if horizontal "|" "*")))
    (if horizontal
      (print (if (h-path? grid cell) "  " " |"))
      (print (if (v-path? grid cell) " *" "-*")))
    (if (= 9 (rem cell 10))
      (println))))

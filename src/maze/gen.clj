(ns maze.gen
  (use [maze.util]))

(defn choose-from-c [c]
  (rand-nth (seq c)))

(defn choose-neighbour-from-candidates [cands]
  (when (seq cands)
    (rand-nth cands)))

(defn find-neighbour-not-in-c [size cell c complete]
  (choose-neighbour-from-candidates (remove (into c complete) (neighbours size cell))))

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
     (maze-gen (+ 10 (rand-int 11))))
  ([size]
     (maze-gen size #{} #{(rand-int (* size size))} []))
  ([size paths c complete]
     (if (empty? c)
       [size paths]
       (let [cell-from-c (choose-from-c c)
             neighbour (find-neighbour-not-in-c size cell-from-c c complete)]
         (if neighbour
           (recur size (make-path paths cell-from-c neighbour) (conj c neighbour) complete)
           (recur size paths (disj c cell-from-c) (conj complete cell-from-c)))))))

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

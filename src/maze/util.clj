(ns maze.util)

(defn top-neighbour
  "Returns the index of the cell above (north) cell c, or nil if c is
on the top row."
  [c]
  (when (> c 9)
    (- c 10)))

(defn bottom-neighbour
  "Returns the index of the cell below (south) cell c, or nil if c is
on the bottom row."
  [c]  
  (when (< c 90)
    (+ c 10)))

(defn right-neighbour
  "Returns the index of the cell to the right (east) of cell c, or nil
if c is on the rightmost row."
  [c]
  (when (not (= 9 (rem c 10)))
    (inc c)))

(defn left-neighbour
  "Returns the index of the cell to the left (west) of cell c, or nil
if c is on the leftmost row."
    [c]
  (when (not (zero? (rem c 10)))
    (dec c)))

(defn neighbours
  "A sequence of all neighbours of c"
  [c]
  (filter identity [(top-neighbour c)
                    (bottom-neighbour c)
                    (right-neighbour c)
                    (left-neighbour c)]))

(defn path?
  "Returns true if you can move from the given cell
in the direction given by the function mv-fn."
  [maze cell mv-fn]
  (or (contains? maze [cell (mv-fn cell)])
      (contains? maze [(mv-fn cell) cell])))

(def mv-fns
  "A map from a direction keyword to a move function."
  {:n top-neighbour
   :e right-neighbour
   :s bottom-neighbour
   :w left-neighbour})

(defn n-path? "True if there is a path north from the given cell" [maze cell] (path? maze cell (mv-fns :n)))
(defn e-path? "True if there is a path east from the given cell" [maze cell] (path? maze cell (mv-fns :e)))
(defn s-path? "True if there is a path south from the given cell" [maze cell] (path? maze cell (mv-fns :s)))
(defn w-path? "True if there is a path west from the given cell" [maze cell] (path? maze cell (mv-fns :w)))

(defn op-dir
  "Returns the direction opposite to the one given."
  [d]
  (cond (= d :n) :s
        (= d :e) :w
        (= d :s) :n
        (= d :w) :e))

(defn left-dir
  "Returns the direction 90 degrees left of the one given."
  [d]
  (cond (= d :n) :w
        (= d :e) :n
        (= d :s) :e
        (= d :w) :s))

(defn right-dir
  "Returns the direction 90 degrees right of the one given."
  [d]
  (cond (= d :n) :e
        (= d :e) :s
        (= d :s) :w
        (= d :w) :n))

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

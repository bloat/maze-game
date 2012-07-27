(ns maze.util)

(defn top-neighbour
  "Returns the index of the cell above (north) cell c, or nil if c is
on the top row."
  [s c]
  (when (>= c s)
    (- c s)))

(defn bottom-neighbour
  "Returns the index of the cell below (south) cell c, or nil if c is
on the bottom row."
  [s c]  
  (when (< c (* s (dec s)))
    (+ c s)))

(defn right-neighbour
  "Returns the index of the cell to the right (east) of cell c, or nil
if c is on the rightmost column."
  [s c]
  (when (not (= (dec s) (rem c s)))
    (inc c)))

(defn left-neighbour
  "Returns the index of the cell to the left (west) of cell c, or nil
if c is on the leftmost column."
    [s c]
  (when (not (zero? (rem c s)))
    (dec c)))

(defn neighbours
  "A sequence of all neighbours of c"
  [s c]
  (filter identity [(top-neighbour s c)
                    (bottom-neighbour s c)
                    (right-neighbour s c)
                    (left-neighbour s c)]))

(defn path?
  "Returns true if you can move from the given cell
in the direction given by the function mv-fn."
  [[s p] c mv-fn]
  (or (contains? p [c (mv-fn s c)])
      (contains? p [(mv-fn s c) c])))

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

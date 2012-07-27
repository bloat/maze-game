(ns maze.draw
  (use [maze.util]))

(defn- path-to-cells
  "Convert a sequence of direction keywords into a list of cells traversed by that path."
  [size start path]
  (reduce
   (fn [cells d] (conj cells ((mv-fns d) size (last cells))))
   [start]
   path))

(defn draw-maze
  "Print a representation of the maze."
  ([maze] (draw-maze maze []))
  ([maze path]
     (let [size (first maze)
           path-cells (into #{} (path-to-cells size 0 path))]
       (println (apply str "*" (repeat size "-*")))
       (doseq [[cell horizontal] (mapcat (fn [r] (concat (map #(vector % true) r) (map #(vector % false) r))) (partition size (range 0 (* size size))))]
         (if (zero? (rem cell size))
           (print (if horizontal "|" "*")))
         (if horizontal
           (print (if (e-path? maze cell)
                    (if (contains? path-cells cell) "o " "  ")
                    (if (contains? path-cells cell) "o|" " |")))
           (print (if (s-path? maze cell) " *" "-*")))
         (if (= (dec size) (rem cell size))
           (println))))))

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

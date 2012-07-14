(ns maze.play
  (use [maze.util]))

(defn view [pos grid path-pred mv-fn]
  (loop [length 0 cell pos]
    (if (path-pred grid cell)
      (recur (inc length) (mv-fn cell))
      length)))

(defn n-view [pos grid] (view pos grid n-path? (mv-fns :n)))
(defn e-view [pos grid] (view pos grid e-path? (mv-fns :e)))
(defn s-view [pos grid] (view pos grid s-path? (mv-fns :s)))
(defn w-view [pos grid] (view pos grid w-path? (mv-fns :w)))

(def path-preds {:n n-path?
                 :e e-path?
                 :s s-path?
                 :w w-path?})

(defn apply-move [cell move grid]
  ;; check for valid move
  ((mv-fns move) cell))

(defn append-to-path [path move grid]
  (conj path move))

(defn play-maze [grid maze-fn start tries path]
  (cond
    (zero? tries) [:failure path]
    (= 99 start) [:success path]
    :else (let [move (maze-fn (n-view start grid)
                              (e-view start grid)
                              (s-view start grid)
                              (w-view start grid)
                              path)]
            (recur grid
                   maze-fn
                   (apply-move start move grid)
                   (dec tries)
                   (append-to-path path move grid)))))

(def ex-play
  (fn [n-view e-view s-view w-view path]
    (first (rand-nth (remove (fn [[_ v]] (zero? v)) [[:n n-view]
                                                     [:e e-view]
                                                     [:s s-view]
                                                     [:w w-view]])))))

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
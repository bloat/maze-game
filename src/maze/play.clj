(ns maze.play
  (use [maze.util]))

(def path-preds {:n n-path?
                 :e e-path?
                 :s s-path?
                 :w w-path?})

(defn inc-if-can-go [cell maze dir to-inc]
  (if ((path-preds dir) maze cell)
    (inc to-inc)
    to-inc))

(defn view [pos maze dir]
  (loop [length 0 r-paths 0 l-paths 0 cell pos]
    (let [new-r-paths (inc-if-can-go cell maze (right-dir dir) r-paths)
          new-l-paths (inc-if-can-go cell maze (left-dir dir) l-paths)]
      (if ((path-preds dir) maze cell)
        (recur (inc length)
               new-r-paths
               new-l-paths
               ((mv-fns dir) (first maze) cell))
        [length new-r-paths new-l-paths]))))

(defn n-view [pos maze] (view pos maze :n))
(defn e-view [pos maze] (view pos maze :e))
(defn s-view [pos maze] (view pos maze :s))
(defn w-view [pos maze] (view pos maze :w))

(defn apply-move [cell move maze]
  (if ((path-preds move) maze cell)
    ((mv-fns move) (first maze) cell)
    cell))

(defn play-maze [maze maze-fn start tries path]
  (let [target (dec (* (first maze) (first maze)))]
    (cond
      (zero? tries) [:failure path]
      (= target start) [:success path]
      :else (let [move (maze-fn (n-view start maze)
                                (e-view start maze)
                                (s-view start maze)
                                (w-view start maze)
                                path)
                  new-cell (apply-move start move maze)
                  new-path (if (= start new-cell) path (conj path move))]
              (recur maze
                     maze-fn
                     new-cell
                     (dec tries)
                     new-path)))))

(defn ex-play
  [n-view e-view s-view w-view path]
  (first (rand-nth (remove (fn [[_ [v _ _]]] (zero? v)) [[:n n-view]
                                                         [:e e-view]
                                                         [:s s-view]
                                                         [:w w-view]]))))

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

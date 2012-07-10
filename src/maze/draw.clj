(ns maze.draw
  (use [maze.util]))

(defn- path-to-cells
  "Convert a sequence of direction keywords into a list of cells traversed by that path."
  [start path]
  (reduce
   (fn [cells d] (conj cells ((mv-fns d) (last cells))))
   [start]
   path))

(defn draw-maze
  "Print a representation of the maze."
  ([maze] (draw-maze maze []))
  ([maze path]
     (let [path-cells (into #{} (path-to-cells 0 path))] 
       (println "*-*-*-*-*-*-*-*-*-*-*")
       (doseq [[cell horizontal] (mapcat (fn [r] (concat (map #(vector % true) r) (map #(vector % false) r))) (partition 10 (range 0 100)))]
         (if (zero? (rem cell 10))
           (print (if horizontal "|" "*")))
         (if horizontal
           (print (if (e-path? maze cell)
                    (if (contains? path-cells cell) "o " "  ")
                    (if (contains? path-cells cell) "o|" " |")))
           (print (if (s-path? maze cell) " *" "-*")))
         (if (= 9 (rem cell 10))
           (println))))))
(defproject maze "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.0"]
                 [hiccup "1.0.0"]
                 [ring/ring-jetty-adapter "1.1.1"]]
  :plugins [[lein-swank "1.4.4"]
            [lein-ring "0.7.1"]]
  :ring {:handler maze.www/app})
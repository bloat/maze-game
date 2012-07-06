(ns maze.www
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [hiccup.page :as html]
            [hiccup.form :as form]
            [maze.controller :as ctrl]))

(defn submit-html []
  (html/html5 [:head [:title "Submit your maze solver"]]
              [:body
               [:p "Submit your maze solver"]
               [:p "Paste in the text of your maze solver, in the form of an anonymous function literal. The function must take 5 arguments" [:pre "[n-view e-view s-view w-view path]"]]
               (form/form-to [:post "/upload"]
                             [:p (form/label "name" "name")
                              (form/text-field "name")]
                             [:p (form/label "code" "code")
                              (form/text-area "solver")]
                             (form/submit-button "Upload"))]))

(defroutes main-routes
  (GET "/submit" _ (submit-html))
  (POST "/upload" {{solver :solver name :name} :params} (ctrl/process-solver name solver))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))



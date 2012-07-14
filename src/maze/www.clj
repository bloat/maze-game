(ns maze.www
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [hiccup.page :as html]
            [hiccup.form :as form]
            [maze.controller :as ctrl]
            [clojure.pprint :as pp])
  (:import [java.io StringWriter]))

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

(defn results-html []
  (html/html5 [:head [:title "Maze Challenge"]]
              [:body
               [:a {:href "submit"} "Upload a solver"]
               [:div
                [:table
                 [:thead
                  [:tr
                   [:th "Name"] [:th "Won"] [:th "Lost"] [:th "Drawn"]]]
                 [:tbody
                  (for [[solver {score :score}] @ctrl/solvers]
                    (let [{w :w l :l d :d} @score]
                      [:tr [:td solver] [:td w] [:td l] [:td d]]))]]]]))

(defn submit-response [[code name submission exception]]
  (html/html5 [:head [:title "Maze Challenge"]]
              [:body
               (cond (= code :success)
                     [:span [:p "Successfully uploaded function " name]
                      [:pre (with-out-str (pp/pprint submission))]]
                     (= code :eval-error)
                     [:span [:p "Could not evaluate the function " name]
                      [:p (.getMessage exception)]
                      [:pre (with-out-str (pp/pprint submission))]]
                     (= code :read-error)
                     [:span
                      [:p "Could not read the text for the functon " name]
                      [:p (.getMessage exception)]
                      [:pre submission]]
                     (= code :test-error)
                     [:span [:p "Failure when testing function " name]
                      [:p (.getMessage exception)]
                      [:pre (with-out-str (pp/pprint submission))]])
               [:a {:href "/"} "Back"]]))

(defroutes main-routes
  (GET "/" _ (results-html))
  (GET "/submit" _ (submit-html))
  (POST "/upload" {{solver :solver name :name} :params} (submit-response (ctrl/process-solver name solver)))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))


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

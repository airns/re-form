(ns ui.core
  (:require-macros [reagent.ratom :refer [reaction]]
                   [cljs.core.async.macros :refer [go]])
  (:require
   [cljsjs.react]
   [goog.string :as gstring]
   [goog.string.format]
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]
   [reagent.core :as reagent]
   [garden.core :as garden]
   [garden.color :as c]
   [garden.units :as u]
   [re-frame.core :as rf]
   [route-map.core :as route-map]
   [ui.routing]
   [re-form.core :as form]
   [re-form.validators :as valid]
   [clojure.string :as str]
   [ui.file-upload-page :as fup]
   [re-form.inputs :as w]
   [re-form.submit :as s]
   [re-form.collection :as fc]
   [re-form.context :as ctx]))

(defn style [gcss]
  [:style (garden/css gcss)])

(defn index []
  (let [form {:name :example-form
              :value {:name "Mike"}}]
    (fn []
      [form/form form
       [:h1 "Form builder"]

       [:label "Name"]
       [form/field {:form :example-form :path [:name] :input w/text-input}]
       [form/errors-for {:form :example-form :path [:name]}]


       [:label "Gender"]
       #_[form/re-radio-buttons {:form form :name :gender
                                 :label-fn identity
                                 :options-path [:forms :myform :options :gender]}]
       [form/field {:form :example-form :path [:gender] :input w/text-input}]

       #_[:h3 "Collection"]

       #_[form/re-collection
          {:form form :name :address}
          address-form]])))

(defn select-page []
  (let [items [{:name "Nikolai"}
               {:name "Mike"}
               {:name "Max"}
               {:name "Marat"}
               {:name "Tim"}
               {:name "Slava"}]

        form {:name :selects-form
              :value {:owner {:name "Mike"}
                      :other-owner {:name "Marat"}
                      :last-owner {:name "Max"}}}]
    (fn []
      [form/form form
       [:div.row
        [:div.col
         [:h1 "Select widget"]

         [:label "XHR select:"]
         [form/field {:value-fn #(get-in % [:resource :code])
                      :label-fn #(get-in % [:resource :display])
                      :suggest-fn
                      (fn [value] (go (:entry (:body (<! (http/get (gstring/format "https://ml.aidbox.io/$terminology/CodeSystem/$lookup?display=%s&system=http%3A%2F%2Fhl7.org%2Ffhir%2Fsid%2Ficd-10" value)))))))
                      :path [:xhr]
                      :input w/select-xhr-input}]


         [:label "Owner: "]
         [form/field {:items items
                      :label-fn :name
                      :path [:owner]
                      :input w/radio-input}]
         [:br]
         [:br]
         [:label "Horizontal owner:"]
         [form/field {:items items
                      :label-fn :name
                      :path [:other-owner]
                      :input w/button-select-input}]
         [:br]
         [:label "Select"]
         [form/field {:items items
                      :label-fn :name
                      :path [:last-owner]
                      :input w/select-input}]
         [:div.col [form/form-data {:form :selects-form}]]]]])))

(defn switchbox-page []
  (let [form {:name :switches-form
              :value {:admin true
                      :superuser false}}]
    (fn []
      [form/form form
       [:div.row
        [:div.col
         [:h1 "Switch widget"]
         [:label "Is admin?"]
         [form/field {:path [:admin]
                      :label "admin"
                      :input w/switchbox-input}]
         [:label "Is superuser?"]
         [form/field {:path [:superuser]
                      :label "superuser"
                      :input w/switchbox-input}]]
        [:div.col
         [form/form-data {:form :switches-form}]]]])))

#_(defn list-page []
    (let [form {:path [:forms :myform]
                :properties {:roles {:items {:type :string}}}
                :value {:roles ["a", "b"]}}]
      (rf/dispatch [:re-form/init form])
      (let [v (rf/subscribe [:re-form/value [:forms :myform]])]
        (fn []
          [:div "list"]
          [:div.row
           [:div.col
            [:h1 "re-list widget"]
            [:div.form-row
             [:label "Roles"]
             [form/re-list {:form form :name :roles}]]

            [:div.form-row
             [:label "Roles"]
             [form/re-list {:form form :name :roles}]]]

           [:div.col
            [form-data form]]]))))

(defn datetime-page []
  (let [form {:name :calendars-form
              :value {:birthdate-one {:d 5 :m 3 :y 1980}
                      :birthdate-two {:d 17 :m 4 :y 1995}}}]
    (fn []
      [form/form form
       [:div.row
        [:div.col
         [:h1 "Calendar"]
         [:div.form-row
          [:label "Birth Date"]
          [form/field {:form :calendars-form
                       :path [:birthdate-one]
                       :input w/calendar-input}]]
         [:div.form-row
          [:label "Birth Date 2"]
          [form/field {:form :calendars-form
                       :path [:birthdate-two]
                       :input w/calendar-input}]]]
        [:div.col
         [form/form-data {:form :calendars-form}]]]])))

(defn inputs-page []
  (let [form {:name :inputs-form
              :validate-fn (fn [v]
                             (if (not= (:name v) "nicola")
                               {[:name] ["should be nicola"]}
                               {[:name] nil}))

              :value {:name "nicola"
                      :email "niquola@mail.com"
                      :organization {:name "github" :url "github.com"}
                      :groups [{:name "admin"} {:name "physician"}]
                      :telecom [{:system "phone" :value "+7 999 666 55 44"}
                                {:system "email" :value "abcab@aaa.com"}]

                      :cities ["Omsk" "SPb"]}}]

    (fn []
      [form/form form
       [:div
        [:h1 "Select widget"]

        [:hr]
        [:div.row
         [:div.col
          [:div.form-row
           [:label "Name: "]
           [form/field {:path [:name] :input w/text-input}]]

          [:div.form-row
           [:label "Email: "]
           [form/field {:path [:email]
                        :validators [(valid/email :message "email please")]
                        :input w/text-input}]]

          [:label "Password: "]
          [form/field {:path [:password]
                       :validators [(valid/min-count 8 count :message "Too short for a password")]
                       :input w/text-input
                       :type "password"}]

          [:div.form-row
           [:label "Organization.name: "]
           [form/field {:path [:organization :name]
                        :input w/text-input
                        :validators [(valid/regex #".+ GmbH")]}]]

          [:div.form-row
           [:label "Organization.url: "]
           [form/field {:path [:organization :url] :input w/text-input}]]

          [:div.form-row
           [:label "group.0.name: "]
           [form/field {:path [:groups 0 :name]
                        :input w/text-input}]]

          [:div.form-row
           [:label "group.1.name: "]
           [form/field {:path [:groups 1 :name]
                        :input w/text-input}]]

          [:div.form-row
           [:label "Telecom: "]
           [fc/collection {:path [:telecom] :new-item-value {:system "phone"}}
            [form/field {:path [:value] :input w/text-input}]]]

          [:div.form-row
           [:label "Cities: "]
           [fc/collection {:path [:cities] :new-item-value ""}
            [form/field {:path [] :input w/text-input}]]]

          [s/submit-button {:submit-fn #(js/alert (pr-str %))} "Submit!"]]

         [:div.col
          [form/form-data {:form :inputs-form}]]]]])))

(defn checkbox-page []
  (let [items [{:name "Nikolai"}
               {:name "Mike"}
               {:name "Max"}
               {:name "Marat"}
               {:name "Tim"}
               {:name "Slava"}]
        form {:name :checkbox-form
              :value {:multies #{{:name "Mike"}
                                {:name "Marat"}}}}]
    (fn []
      [form/form form
       [:div.row
        [:div.col
         [:h1 "Checkbox group"]

         [:label "Select multiple: "]
         [form/field {:items items
                      :label-fn :name
                      :path [:multies]
                      :input w/checkbox-group-input}]
         [:div.col [form/form-data {:form :checkbox-form}]]]]])))

(defn textarea-page []
  (let [form {:name :textarea-form
              :value {:area-one "Fill me"}}]
    (fn []
      [form/form form
       [:div [:h1 "Text field widget"]
        [:div.row
         [:div.col
          [form/field {:path [:area-one]
                       :input w/textarea-input}]]
         [:div.col
          [form/form-data {:form :textarea-form}]]]]])))

(def pages
  {:index {:title "Form builder"
           :w 1
           :cmp index}

   :inputs {:title "Inputs"
            :w 2
            :cmp inputs-page}

   :select {:title "Select"
            :w 3
            :cmp select-page}

   :checkbox {:title "Checkbox"
                 :w 4
                 :cmp checkbox-page}
   :datetime {:title "Date/Time"
              :w 5
              :cmp datetime-page}
   :switchbox {:title "Switch"
               :w 6
               :cmp switchbox-page}

   ;; :list {:title "List"
   ;;        :w 2
   ;;        :cmp list-page}

   :file_upload {:title "File Upload"
                 :w 2
                 :cmp fup/file-upload-page}
   :textarea {:title "Text Area"
              :w 7
              :cmp textarea-page}})

(def routes (reduce (fn [acc [k v]] (assoc acc (name k) {:. (assoc (dissoc v :cmp) :id k)})) {:. :index} pages))

(defn href
  [& parts]
  (let [url (str "/" (str/join "/" (map (fn [x] (if (keyword? x) (name x) (str x))) parts)))]
    (when-not  (route-map/match [:. url] routes)
      (.error js/console (str url " is not matches routes")))
    (str "#" url)))

(defn current-page []
  (let [current-route (rf/subscribe [:route-map/current-route])]
    (fn []
      (let [{page :match params :params} @current-route]
        (if page
          (if-let [cmp (:cmp (get pages (:id page)))]
            [:div [cmp params]]
            [:div.not-found (str "Page not found [" (str page) "]" )])
          [:div.not-found (str "Route not found ")])))))

(defn navigation []
  (let [current-route (rf/subscribe [:route-map/current-route])]
    (fn []
      [:div.navigation
       (style [:.navigation {:padding {:top (u/px 20)
                                       :right (u/px 20)}
                             :background-color "#f1f1f1"}
               [:a.navitem {:display "block"
                            :color "#888"
                            :border-left "6px solid #ddd"
                            :font-family "lato"
                            :padding {:top (u/px 10)
                                      :bottom (u/px 10)
                                      :left (u/px 20)}}
                [:&.active {:color "#007bff"
                            :background-color "#eee"
                            :border-color "#007bff"}]]])
       (doall
        (for [[i p] (sort-by (fn [[_ x]] (:w x)) pages)]
          [:a.navitem {:key i
                       :class (when (= i (get-in @current-route [:match :id]))
                                "active")
                       :href (href (name i))} (:title p)]))])))

(defn root-component []
  [:div
   (style
    (let [nav-width 300]
      [:body
       [:.topnav {:background-color "#3F51B5"
                  :color "white"}
        [:.brand {:display "inline-block"
                  :font-size (u/px 30)
                  :font-weight "bold"
                  :margin {:left (u/px 20)}
                  :font-family "lato"
                  :padding (u/px 10)}]]
       [:.navigation {:width (u/px nav-width)
                      :position "absolute"
                      :top (u/px 67)
                      :bottom 0
                      :left 0}]
       [:.pane {:margin {:left (u/px (+ nav-width 20))
                         :top (u/px 20)
                         :right (u/px 40)}
                :padding (u/px 40)}]
       [:h1 {:margin-bottom (u/px 30)}]
       form/form-style
       [:.form-row {:padding "5px 0px"}]
       [:pre {:background-color "#f1f1f1" :padding "20px" :border "1px solid #ddd"} ]
       [:label {:width "10em"
                :vertical-align "top"
                :color "#888"
                :display "inline-block" :text-align "right" :padding-right "10px"}]
       [:.errors {:color "red" :margin-left "10em"}]]))

   [:div.topnav [:a.brand "re-form"]]
   [navigation]
   [:div.pane [current-page]]])

(rf/reg-event-fx
 ::initialize
 (fn [cofx]
   {:dispatch-n [[:route-map/init routes]]}))

(defn dispatch-routes [_]
  (let [fragment (.. js/window -location -hash)]
    (rf/dispatch [:fragment-changed fragment])))


(defn mount-root []
  (reagent/render [root-component] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch [::initialize])
  (mount-root))

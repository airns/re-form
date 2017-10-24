(ns re-form.inputs.button-select-input-impl
  (:require [reagent.core :as r]
            [garden.units :as u]))

(def h 16)
(def h2 24)
(def h3 38)
(def selection-bg-color "#007bff")
(def hover-bg-color "#f1f1f1")
(def border "1px solid #ddd")

(def button-select-style
  [:div.re-radio-buttons
   {:display "inline-block"
    :border border
    :border-radius (u/px 2)}
   [:div.option {:cursor "pointer"
                 :transition "background-color 200ms, color 100ms"
                 :background-color "white"
                 :border-right border
                 :line-height (u/px- h3 2)
                 :padding-left (u/px h)
                 :padding-right (u/px h)
                 :display "inline-block"}

    [:&:hover {:background-color hover-bg-color}]
    [:&:first-child {:border-radius "2px 0px 0px 2px"}]
    [:&:last-child {:border-radius "0px 2px 2px 0px"
                    :border-right "none"}]
    [:&.active {:background-color selection-bg-color
                :color "white"}]]])

(defn button-select-input [_]
  (fn [{:keys [value on-change label-fn items]}]
    [:div.re-radio-buttons
     (doall
      (for [i items]
        [:div.option
         {:key (pr-str i)
          :class (when (= i value) "active")
          :on-click (partial on-change i)}
         [:span.value (label-fn i)]]))]))

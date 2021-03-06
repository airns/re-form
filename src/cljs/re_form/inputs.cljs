(ns re-form.inputs
  (:require [re-form.inputs.file-upload-impl :as fupl-impl]
            [re-form.inputs.textarea-impl :as ta-impl]
            [re-form.inputs.text-input-impl :as tei-impl]
            [re-form.inputs.radio-input-impl :as rad-impl]
            [re-form.inputs.csv-input-impl :as csv-impl]
            [re-form.inputs.calendar-impl :as cal-impl]
            [re-form.inputs.switchbox-impl :as sw-impl]
            [re-form.inputs.select-input-impl :as se-impl]
            [re-form.inputs.button-select-input-impl :as bs-impl]
            [re-form.inputs.checkbox-group-impl :as ch-impl]
            [re-form.inputs.select-xhr-impl :as xh-impl]
            [re-form.inputs.re-select-input-impl :as re-sel-impl]
            [re-form.inputs.select-impl :as select-impl]
            [re-form.inputs.codemirror-impl :as cm-impl]
            [re-form.inputs.date-input-impl :as date-impl]))

;; Widgets

(def file-upload-input fupl-impl/file-upload)
(def textarea-input ta-impl/textarea)
(def date-input date-impl/date-input)
(def date-time-input date-impl/date-time-input)
(def time-input date-impl/time-input)
(def csv-input csv-impl/csv-input)
(def text-input tei-impl/text-input)
(def radio-input rad-impl/radio-input)
(def calendar-input cal-impl/re-calendar)
(def switchbox-input sw-impl/switch-box)
(def button-select-input bs-impl/button-select-input)
(def select-input se-impl/select-input)
(def checkbox-group-input ch-impl/checkbox-group-input)
(def select-xhr-input xh-impl/select-xhr-input)
(def codemirror-input cm-impl/codemirror-input)
(def re-select-input re-sel-impl/re-select-input)
(def select select-impl/select)

;; Styles

(def file-upload-style fupl-impl/file-upload-style)
(def textarea-style ta-impl/textarea-style)
(def date-input-style date-impl/date-input-style)
(def radio-input-style rad-impl/radio-input-styles)
(def calendar-style cal-impl/re-calendar-style)
(def switchbox-style sw-impl/re-switch-box-style)
(def button-select-style bs-impl/button-select-style)
(def checkbox-group-style ch-impl/checkbox-group-style)
(def codemirror-style cm-impl/codemirror-style)
(def text-input-style tei-impl/text-input-style)
(def select-input-style se-impl/select-input-style)
(def select-xhr-style xh-impl/select-xhr-style)
(def re-select-input-style re-sel-impl/re-select-input-style)
(def select-style select-impl/select-style)

(ns subthreshold.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :angle 0
   :diameter 25
   :change-rate 0.1})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (println state)
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)
   :diameter (+ (:diameter state) (:change-rate state))
   :change-rate (:change-rate state)})


(defn draw-circle [diameter]
  (let [x 0 y 0 width diameter height diameter]
    (q/arc x y    ; x, y center position
           width height    ; width, height
           (* -0.25 q/PI) (* 1.25 q/PI))
    (q/arc x y (* 0.5 width) (* 0.5 height) 0 (* 2 q/PI)) ; circle
    (let [distance-x (* width (Math/sin (* 0.25 q/PI)))
          distance-y (* (* -1 height) (Math/cos (* 0.25 q/PI)))]
      (q/arc (+ x distance-x) (+ y distance-y) width height (* 0.75 q/PI) (* 2.25 q/PI))
      (q/arc (+ x distance-x) (+ y distance-y) (* 0.5 width) (* 0.5 height) 0 (* 2 q/PI)))))

(defn draw-state [state]
  ; Clear the sketch once at the beginning
  (q/background 255)
  (q/camera 0 0 300 0 0 0 0 1 0)
  (q/no-fill)
  (q/color (:color state) 255 255)
  (q/translate (* -1 (:change-rate state)) 0)
  (let [diameter (:diameter state)]
    (dotimes [_ (/ (q/height) diameter)]
      (q/push-matrix)
      (draw-circle diameter)
      (dotimes [_ (/ (q/width) diameter)]
        (q/translate (* 2 diameter (Math/sin (* 0.25 q/PI))) 0)
        (draw-circle diameter))
      (q/pop-matrix)
      (q/translate 0 (+ diameter (* 0.75 diameter))))))


(q/defsketch subthreshold
  :title "Subthreshold visuals"
  :size [500 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])

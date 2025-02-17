(use 'overtone.live)
(use 'overtone.midi)

(defn noteify
  ([pitch] (noteify pitch 1/4))
  ([pitch dur] (noteify pitch dur 0))
  ([pitch dur channel]
   (let [midinote (if (keyword? pitch)
                    (note pitch)
                    pitch)]
     {:midinote midinote
      :dur dur
      :channel channel})))

(defn derive [rules key-fn coll]
  (flatten
   (map-indexed
    (fn [i e]
      (let [rule (rules (key-fn e))]
        (if (some? rule)
          (rule i coll)
          e)))
    coll)))

(defn derive-iterate [coll rules key-fn iterations]
  (nth
   (iterate (partial derive rules key-fn) coll)
   iterations))

(defn derive-midi [coll rules iterations]
  (derive-iterate
   coll
   rules
   #(:midinote %)
   iterations))

;; Replace with the name of your software MIDI router.
;; This is the default one for Mac
(def midi-dest
  (midi-out "IAC Driver Bus 1"))

(defn note-on [note]
  (midi-note-on midi-dest (:midinote note) 100 (:channel note)))

(defn note-off [note]
  (midi-note-off midi-dest (:midinote note) (:channel note)))

(defn note-off-all []
  (doseq [note (range 128)
          ch (range 16)]
    (midi-note-off midi-dest note ch)))

(defn play-note-seq [notes time bpm]
  (when (seq notes)
    (let [note (first notes)
          note-len (* 60000 (/ 1 bpm) (:dur note) 4)
          next-time (+ time note-len)]
      (note-on note)
      (apply-at
       next-time
       #'note-off
       [note])
      (apply-at
       next-time
       #'play-note-seq
       [(next notes) next-time bpm]))))

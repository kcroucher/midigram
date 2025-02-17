;; All examples in this file don't produce sound,
;; they only send MIDI.
;; You'll need to provide your own instruments listening on
;; the given MIDI channels.

;; Create a melody line on channel 0
(let [channel 0
      noteify (fn [pitch dur] (noteify pitch dur channel))]
  (do
    (def melody
      (derive-midi
       [(noteify :c4 1/4)]
       {(note :c4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :e4 1/4)
                        (noteify :g4 1/2)]))
        (note :e4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :b4 1/4)
                        (noteify :g4 1/4)]))
        (note :f4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :d4 1/2)]))
        (note :g4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :c4 1/4)
                        (noteify :f4 1/2)
                        (noteify :g4 1/4)]))
        (note :b4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :g4 dur)
                        (noteify :e4 dur)]))}
       4))
    (play-note-seq melody (now) 300)))

;; Create a bass line on channel 1
(let [channel 1
      noteify (fn [pitch dur] (noteify pitch dur channel))]
  (do
    (def bass
      (derive-midi
       [(noteify :g2 1)]
       {(note :c2) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :c2 1)
                        (noteify :g2 1)]))
        (note :g2) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(if (zero? (rand-int 2))
                          (noteify :f2 1)
                          (noteify :g2 1))
                        (if (zero? (rand-int 2))
                          (noteify :c2 1)
                          (noteify :g2 1))]))}
       3))
    (play-note-seq bass (now) 300)))

;; Play both lines together, slower
(let [bpm 120]
  (do
    (play-note-seq melody (now) bpm)
    (play-note-seq bass (now) bpm)))

;; Set this one to a low (~3) vs high (~9) number of iterations.
;; Low produces a reasonably interesting line, whereas
;; high gets repetitive
(let [channel 0
      noteify (fn [pitch dur] (noteify pitch dur channel))]
  (do
    (def melody-iteration-test
      (derive-midi
       [(noteify :c4 1/4) (noteify :f4 1/4)]
       {(note :c4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :b4 1/4)
                        (noteify :g4 1/2)]))
        (note :e4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :d5 1/4)
                        (noteify :e4 1/4)]))
        (note :f4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :d4 1/2)]))
        (note :g4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :c4 1/4)
                        (noteify :b4 1/2)
                        (noteify :e4 1/4)]))
        (note :b4) (fn [i coll]
                     (let [dur (:dur (nth coll i))]
                       [(noteify :a#4 dur)
                        (noteify :f4 dur)]))}
       3))
    (play-note-seq melody-iteration-test (now) 300)))

;; 4 line generated drum pattern
;; channel 2 = kick
;; channel 3 = snare
;; channel 4 = hihat
;; channel 5 = clap
(do
  (def kick-snare-pattern
    (derive-midi
     [(noteify :c4 1/4 2) (noteify :f4 1/4 2)]
     {(note :c4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :b4 3/4 2)
                      (noteify :g4 1/2 2)
                      (noteify :b4 1/8 3)
                      (noteify :b4 1/8 3)]))
      (note :e4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :d4 1/2 3)
                      (noteify :e4 1/4 2)
                      (noteify :d4 1/2 3)]))
      (note :f4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :d4 1/4 2)]))
      (note :g4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :c4 1/8 3)
                      (noteify :b4 1/2 3)
                      (noteify :e4 1/4 3)]))
      (note :b4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :a#4 dur 2)
                      (noteify :f4 dur 3)]))}
     4))
  (def hat-clap-pattern
    (derive-midi
     [(noteify :c4 1/4 4) (noteify :f4 1/4 4)]
     {(note :c4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :c4 1/8 4) 
                      (noteify :c4 1/8 4)
                      (noteify :g4 1/2 5)]))
      (note :e4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :e4 1/2 4)
                      (noteify :c4 1/2 4)]))
      (note :f4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :e4 1/4 4)]))
      (note :g4) (fn [i coll]
                   (let [dur (:dur (nth coll i))
                         ch (:channel (nth coll i))]
                     [(noteify :f4 1/4 5)
                      (noteify :g4 1/2 5)]))}
     3))
  (let [bpm 240]
    (do
      (play-note-seq kick-snare-pattern (now) bpm)
      (play-note-seq hat-clap-pattern (now) bpm))))

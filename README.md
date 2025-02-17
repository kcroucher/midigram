# What's this?

A set of functions intended to be used with [Overtone](https://github.com/overtone/overtone), the Clojure interface into Supercollider. It uses the concept of a formal grammar to generate MIDI sequences from simple starting patterns. This library does NOT generate sound, only MIDI data. You'll need to route it to your own MIDI host for that.

`derive-midi` is the main function used to generate MIDI data. It takes an initial sequence of notes, a set of rules, and a number of iterations. Each iteration, the rules are applied to each note in the sequence to produce one to many notes that are then included in the new sequence. This new sequence is the input to the next iteration.

Once a sequence has been generated, the `play-note-seq` function can be used to send the MIDI data to the appropriate channels.

This project was created at the [Recurse Center](https://www.recurse.com/).
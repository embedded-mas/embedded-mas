// Agent saying hello in Morse code using a blinking LED

// H = ....
+!hello
   <- .print("H");
      !dot; !dot; !dot; !dot;
      .wait(400);
      !letter_e.

// E = .
+!letter_e
   <- .print("E");
      !dot;
      .wait(400);
      !letter_l1.

// L = .-..
+!letter_l1
   <- .print("L");
      !dot; !dash; !dot; !dot;
      .wait(400);
      !letter_l2.

// L = .-..
+!letter_l2
   <- .print("L");
      !dot; !dash; !dot; !dot;
      .wait(400);
      !letter_o.

// O = ---
+!letter_o
   <- .print("O");
      !dash; !dash; !dash;
      .print("HELLO finished.").

// Ponto: LED ligado por 200 ms
+!dot
   <- .blink(true);
      .wait(200);
      .blink(false);
      .wait(200).

// Traço: LED ligado por 600 ms
+!dash
   <- .blink(true);
      .wait(600);
      .blink(false);
      .wait(200).

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }

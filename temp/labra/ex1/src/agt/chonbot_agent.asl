// ============================================================
// Robot test - ROS 2 + embedded-mas + Javino
// ============================================================

!test_robot.

+!test_robot <-
    .print("Starting robot test...");

    // Motor speed
    .print("Testing motor speed...");
    .motorSpeed("low");
    .wait(2000);
    .motorSpeed("default");
    .wait(2000);
    .motorSpeed("high");
    .wait(2000);
    .motorSpeed("default");

    // Motor direction
    .print("Testing motor directions...");
    !test_motor;

    // Buzzer
    .print("Testing buzzer...");
    .buzzer("on");
    .wait(2000);
    .buzzer("high");
    .wait(2000);
    .buzzer("low");
    .wait(2000);
    .buzzer("off");

    // Headlight
    .print("Testing headlight...");
    .headlight("on");
    .wait(2000);
    .headlight("high");
    .wait(2000);
    .headlight("low");
    .wait(2000);
    .headlight("off");

    // Turn signals
    .print("Testing turn signals...");
    .turnSignal("left");
    .wait(2000);
    .turnSignal("right");
    .wait(2000);
    .turnSignal("alert");
    .wait(2000);
    .turnSignal("off");

    // Brake light
    .print("Testing brake light...");
    .brakeLight(true);
    .wait(2000);
    .brakeLight(false);

    .print("Actuator tests finished.");
    !test_sensors;

    .print("Robot test finished!").


// ============================================================
// Motor movement test
// ============================================================

+!test_motor <-
    .motor("forward");
    .wait(2000);
    .motor("stop");
    .wait(500);

    .motor("backward");
    .wait(2000);
    .motor("stop");
    .wait(500);

    .motor("left");
    .wait(2000);
    .motor("stop");
    .wait(500);

    .motor("right");
    .wait(2000);
    .motor("stop").



// ============================================================
// Sensor test
// ============================================================

+!test_sensors <-
    .print("Reading sensor values...");
    .wait(2000);
    !print_sensors.

+!print_sensors
    : luminosity(L) & distance(D) &
      line_left(LL) & line_right(LR)
    <-
    .print("Luminosity: ", L);
    .print("Distance: ", D);
    .print("Line sensor left: ", LL);
    .print("Line sensor right: ", LR).

+!print_sensors
    : not (luminosity(_) & distance(_) &
           line_left(_) & line_right(_))
    <-
    .print("Some sensor readings are unavailable.").


{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }

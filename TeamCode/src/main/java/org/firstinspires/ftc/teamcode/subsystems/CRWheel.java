package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Intake.kCRWheelName;
import static org.firstinspires.ftc.teamcode.Constants.Intake.kWheelBackward;
import static org.firstinspires.ftc.teamcode.Constants.Intake.kWheelForward;
import static org.firstinspires.ftc.teamcode.Constants.Intake.kWheelStill;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.shplib.commands.Subsystem;
public class CRWheel extends Subsystem {
    // Declare devices
    // Example:
    private final CRServo cWheel;
    //private final Servo pixelThing; // Need better name
    public enum State {
        FORWARD, BACKWARD, STILL
//        STILL, PIXELON, PIXELOFF
    }

    private State state;

    public CRWheel(HardwareMap hardwareMap) {
        cWheel = hardwareMap.get(CRServo.class, kCRWheelName);
//        pixelThing = hardwareMap.get(Servo.class, kPixelThingName);
        // Set initial state
        // Example:
        setState(State.STILL);
    }

    public void setState(State state) {
        this.state = state;
    }

    // Add control methods
    // Example:
    // private void setPower(double power) { motor.setPower(power); }

    @Override
    public void periodic(Telemetry telemetry) {
        // Add logging if needed
        // Example:
        // telemetry.addData("Motor Encoder: ", motor.getPosition(MotorUnit.TICKS));

        switch (state) {
            case FORWARD:
                cWheel.setPower(kWheelForward);
                break;
            case BACKWARD:
                cWheel.setPower(kWheelForward);
                break;
            case STILL:
                cWheel.setPower(kWheelStill);
//            case PIXELOFF:
//                pixelThing.setPosition(kPixelDisengaged);
//            case PIXELON:
//                pixelThing.setPosition(kPixelEngaged);


        }
//        }

        // OR

//        if (state == State.ENABLED) {
//            setPower(1.0);
//        } else if (state == State.DISABLED) {
//            setPower(0.0);
//        }
    }
}
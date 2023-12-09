package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Intake.kPositionBottom;
import static org.firstinspires.ftc.teamcode.Constants.Intake.kPositionMiddle;
import static org.firstinspires.ftc.teamcode.Constants.Intake.kPositionTop;
import static org.firstinspires.ftc.teamcode.Constants.Intake.kPracticeLeftArmServoName;
import static org.firstinspires.ftc.teamcode.Constants.Intake.kPracticeRightArmServoName;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.shplib.commands.Subsystem;

public class PracticeArmServo extends Subsystem {
    // Declare devices
    // Example:
    private final Servo practiceLeftArm;
    private final Servo practiceRightArm;
    public enum State {
        // Define states
        // Example:
        UP, DOWN,HALFWAY
    }

    private State state;

    public PracticeArmServo(HardwareMap hardwareMap) {
        practiceLeftArm = hardwareMap.get(Servo.class, kPracticeLeftArmServoName);
        practiceRightArm = hardwareMap.get(Servo.class, kPracticeRightArmServoName);
        state = State.DOWN;
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
            case UP:
                practiceLeftArm.setPosition(1-kPositionBottom);
                practiceRightArm.setPosition(kPositionBottom);
                break;
            case DOWN:
                practiceRightArm.setPosition(kPositionTop);
                practiceLeftArm.setPosition(1-kPositionTop);
                break;
            case HALFWAY:
                practiceRightArm.setPosition(kPositionMiddle);
                practiceLeftArm.setPosition(1-kPositionMiddle);
                break;
        }
        telemetry.addData("Right Arm Posiiton ", practiceRightArm.getPosition());
        telemetry.addData("Left Arm Position: ", practiceLeftArm.getPosition());
    }
}

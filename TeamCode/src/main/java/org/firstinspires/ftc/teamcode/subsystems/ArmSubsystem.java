package org.firstinspires.ftc.teamcode.subsystems;

//import static org.firstinspires.ftc.teamcode.Constants.Arm.kClawClosed;
//import static org.firstinspires.ftc.teamcode.Constants.Arm.kClawName;
//import static org.firstinspires.ftc.teamcode.Constants.Arm.kClawOpen;
//import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideHub;
//import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideLow;
//import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideMiddle;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kLeftSlideName;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kRightSlideName;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideBottom;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideBottomClimb;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideClimb;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideD;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideFinishClimb;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideG;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideHigh;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideMiddle;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideP;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideS;
import static org.firstinspires.ftc.teamcode.Constants.Arm.kSlideTolerance;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.shplib.commands.Subsystem;
import org.firstinspires.ftc.teamcode.shplib.controllers.ElevatorFFController;
import org.firstinspires.ftc.teamcode.shplib.controllers.PositionPID;
import org.firstinspires.ftc.teamcode.shplib.hardware.SHPMotor;
import org.firstinspires.ftc.teamcode.shplib.hardware.units.MotorUnit;

public class ArmSubsystem extends Subsystem {
    private final SHPMotor leftSlide;
    private final SHPMotor rightSlide;
    public enum State {
        BOTTOM, MIDDLE, HIGH, CLIMB, BOTTOMCLIMB, FINISHCLIMB
    }
    private State state;
    public ArmSubsystem(HardwareMap hardwareMap) {


        leftSlide = new SHPMotor(hardwareMap, kLeftSlideName);
        leftSlide.reverseDirection();
        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftSlide.enablePositionPID(new PositionPID(kSlideP, 0.0, kSlideD));
        leftSlide.resetEncoder();
        leftSlide.setPositionErrorTolerance(kSlideTolerance);
        leftSlide.enableFF(new ElevatorFFController(kSlideS, kSlideG));

        rightSlide = new SHPMotor(hardwareMap, kRightSlideName);
//        rightSlide.reverseDirection();
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightSlide.enablePositionPID(new PositionPID(kSlideP, 0.0, kSlideD));
        rightSlide.resetEncoder();
        rightSlide.setPositionErrorTolerance(kSlideTolerance);
        rightSlide.enableFF(new ElevatorFFController(kSlideS, kSlideG));

        setState(State.BOTTOM);
    }

    public void setState(State state) {
        this.state = state;
    }
    public State getState() {
        return state;
    }
    public void goUP(){
        if(state==State.BOTTOM){
            state=State.MIDDLE;
        }
        else{
            state=State.HIGH;
        }
    }

    public double getDriveBias() {
        return Math.abs(getSlidePosition(MotorUnit.TICKS) / kSlideHigh - 1.0);
    }

    public boolean atSetpoint() {
        return leftSlide.atPositionSetpoint() && rightSlide.atPositionSetpoint();
    }

    public double getSlidePosition(MotorUnit unit) {
        return (leftSlide.getPosition(unit) + rightSlide.getPosition(unit)) / 2.0;
    }

    private double processState() {
        switch (state) {
            case BOTTOM:
//                telemetry.addData("Power: ", slide.setPosition(10.0))
//                  leftSlide.setPosition(kSlideBottom);
                rightSlide.setPosition(kSlideBottom);
                return leftSlide.setPosition(kSlideBottom);
//                break;
            case MIDDLE:
                rightSlide.setPosition(kSlideMiddle);
                return leftSlide.setPosition(kSlideMiddle);
            case HIGH:
                rightSlide.setPosition(kSlideHigh);
                return leftSlide.setPosition(kSlideHigh);
            case CLIMB:
                rightSlide.setPosition(kSlideClimb);
                return leftSlide.setPosition(kSlideClimb);
            case BOTTOMCLIMB:
                rightSlide.setPosition(kSlideBottomClimb);
                return leftSlide.setPosition(kSlideBottomClimb);
            case FINISHCLIMB:
                rightSlide.setPosition(kSlideFinishClimb);
                return leftSlide.setPosition(kSlideFinishClimb);
                                                                                                                                            //
//                break;
        }
        return 0.0;
    }

    @Override
    public void periodic(Telemetry telemetry) {
        //things to test
        //get rid of right slide set positions and only do left
        //get at set point
        //switch right and left slide in the configuration
        telemetry.addData("State: ", state);
        telemetry.addData("Left Slide Position: ", leftSlide.getPosition(MotorUnit.TICKS));
        telemetry.addData("Right Slide Position: ", rightSlide.getPosition(MotorUnit.TICKS));
        telemetry.addData("Right Slide PID Output: ", processState());
    }
}

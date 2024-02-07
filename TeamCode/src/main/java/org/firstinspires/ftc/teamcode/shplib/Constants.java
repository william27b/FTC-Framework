package org.firstinspires.ftc.teamcode.shplib;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.shplib.controllers.FFController;

@Config
public class Constants {
    // Target voltage for voltage compensation
    public static final double kNominalVoltage = 12.0;
    public static double offset = 0;
    public static final class Drive {
        public static final String[] kMotorNames = new String[]{
                "leftFront",
                "leftRear",
                "rightFront",
                "rightRear"
        };
        public static final FFController[] kFFs = new FFController[]{
                new FFController(0.07),
                new FFController(0.07), //TODO: ??
                new FFController(0.07),
                new FFController(0.045)
        };
        public static final double kMinimumBias = 0.3;
        public static final double kMaximumBias = 0.6;
    }

    public static final class Vision {
        public static final double kTagsizeMeters = 0.0475;
    }

    public static final class Intake{
//        public static final String kLeftAdjust = "LeftAxon";
//        public static final String kRightAdjust = "RightAxon";
        public static final double kPositionBottom = 0.615;
        public static final double kPositionTop = 0.05; //0.05
        public static final double kPositionMiddle = 0.3;
        public static final String kPixelServo = "MiniServo";
        public static final String kPlaneServo = "plane";

        public static final String kAdjustHolder = "35kg";

        public static final String kSpinningIntakeName = "SpinningIntake";
        public static final String kCRWheelName = "CRWheel";
        public static final double kWheelForward = 1.0;
        public static final double kWheelBackward = -1.0;
        public static final double kWheelStill = 0.0;

        // Flap Constants
        public static final String kFlapName = "Flap";
        public static final double kFlapOpen = 1.0;
        public static final double kFlapClose = -1.0;
        public static final double kFlapNeutral = 0.0;

        // Hook Servo Constants
        public static final String kHookServo1Name = "Hook1";
        public static final String kHookServo2Name = "Hook2";
        public static final double kHookLeftEngaged = 0.85;
        public static final double kHookLeftDisengaged = 0.5;
        public static final double kHookRightEngaged = 0.0;
        public static final double kHookRightDisengaged = 0.5;

        public static final String kPracticeLeftArmServoName = "LeftAxon";
        public static final String kPracticeRightArmServoName = "RightAxon";
//        public static final String kPixelThingName = "Pixel Thing Name";
//        public static final double kPixelEngaged = 0.75;
//        public static final double kPixelDisengaged = 0.25;

    }

    public static final class Arm {

        public static final String kLeftSlideName = "leftSlide";
        public static final String kRightSlideName = "rightSlide";
        public static final double kSlideClimb = 3300.0;
        public static final double kSlideBottomClimb = 10;
        public static final double kSlideFinishClimb = 200;

        public static final double kSlideSafety = -860;
        public static final double kSlideConeStack = 1000.0;

        public static final double kSlideBottom = 0.0;
        public static final double kSlideExtended = 250.0;

        public static final double kSlideMiddle = 500.0;
        public static final double kSlideMidHigh = 1600.0;
        public static final double kSlideHigh = 2100.0;
        public static final double kMaxHeight = 4000.0;

        public static final double kSlideStackDistance = 150.0;
        //0.002
        public static final double kSlideP = 0.001;
        //0.0025
        public static final double kSlideD = 0;
        public static final double kSlideTolerance = 2;

        public static final double kSlideS = 0.035; // static friction
        public static final double kSlideG = 0.07; // gravity
        public static final double kPixelHeight = 500;

        //used in template subsystem
        public static final double kSlideHub = 200.0;
        public static final double kSlideLow = 1000.0;
//        public static final double kSlideMiddle = 2500.0;
//        public static final String kClawName = "claw";
//        public static final double kClawOpen = 0.4;
//        public static final double kClawClosed = 0.7;



    }
}
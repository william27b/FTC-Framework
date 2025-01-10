package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.teamcode.SlideSubsystem.SlideState.ABOVE_HIGH_RUNG;
import static org.firstinspires.ftc.teamcode.SlideSubsystem.SlideState.BELOW_HIGH_RUNG;
import static org.firstinspires.ftc.teamcode.SlideSubsystem.SlideState.INTAKE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.shprobotics.pestocore.algorithms.PID;
import com.shprobotics.pestocore.drivebases.DeterministicTracker;
import com.shprobotics.pestocore.drivebases.MecanumController;
import com.shprobotics.pestocore.drivebases.TeleOpController;
import com.shprobotics.pestocore.drivebases.ThreeWheelOdometryTracker;
import com.shprobotics.pestocore.geometries.PathContainer;
import com.shprobotics.pestocore.geometries.PathFollower;
import com.shprobotics.pestocore.geometries.Vector2D;

@Config
public class PestoFTCConfig {
    public static double ODOMETRY_TICKS_PER_INCH = 505.316944316;
    public static double FORWARD_OFFSET = 0.5;
    public static double ODOMETRY_WIDTH = 11.25;

    // TODO: tune these
    // distance traveled / (2 * velocity)
    public static double DECELERATION = 0.28147257117*4;
    public static double MAX_VELOCITY = 52;

    public static final DcMotorSimple.Direction leftEncoderDirection = REVERSE;
    public static final DcMotorSimple.Direction centerEncoderDirection = REVERSE;
    public static final DcMotorSimple.Direction rightEncoderDirection = FORWARD;

    public static String leftName = "backLeft";
    public static String centerName = "frontRight";
    public static String rightName = "backRight";

    public static MecanumController getMecanumController(HardwareMap hardwareMap) {
        MecanumController mecanumController = new MecanumController(hardwareMap, new String[] {
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight"
        });

        mecanumController.configureMotorDirections(new DcMotorSimple.Direction[]{
                FORWARD,
                REVERSE,
                FORWARD,
                REVERSE
        });

        Vector2D frontLeftPower = new Vector2D(45, 55);

        // TODO: get power vectors
        mecanumController.setPowerVectors(new Vector2D[]{
                Vector2D.scale(Vector2D.multiply(frontLeftPower, new Vector2D(+1, 1)), 1/frontLeftPower.getMagnitude()),
                Vector2D.scale(Vector2D.multiply(frontLeftPower, new Vector2D(-1, 1)), 1/frontLeftPower.getMagnitude()),
                Vector2D.scale(Vector2D.multiply(frontLeftPower, new Vector2D(-1, 1)), 1/frontLeftPower.getMagnitude()),
                Vector2D.scale(Vector2D.multiply(frontLeftPower, new Vector2D(+1, 1)), 1/frontLeftPower.getMagnitude()),
        });

        mecanumController.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        return mecanumController;
    }

    public static TeleOpController getTeleOpController(MecanumController mecanumController, DeterministicTracker tracker, HardwareMap hardwareMap) {
        TeleOpController teleOpController = new TeleOpController(mecanumController, hardwareMap);

        teleOpController.configureIMU(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
        );

        teleOpController.useTrackerIMU(tracker);

        teleOpController.setSpeedController((gamepad) -> {
            if (gamepad.left_stick_button) {
                return 1.0;
            }
            return 0.6;
        });

        // TODO: tune max velo
        teleOpController.counteractCentripetalForce(tracker, MAX_VELOCITY);

        return teleOpController;
    }

    public static DeterministicTracker getTracker(HardwareMap hardwareMap) {
        return new ThreeWheelOdometryTracker.TrackerBuilder(
                hardwareMap,
                ODOMETRY_TICKS_PER_INCH,
                FORWARD_OFFSET,
                ODOMETRY_WIDTH,
                leftName,
                centerName,
                rightName,
                leftEncoderDirection,
                centerEncoderDirection,
                rightEncoderDirection
        ).build();
    }

    public static PathFollower.PathFollowerBuilder generatePathFollower(PathContainer pathContainer, MecanumController mecanumController, DeterministicTracker tracker) {
        return new PathFollower.PathFollowerBuilder(mecanumController, tracker, pathContainer)
                .setEndpointPID(new PID(0.002,0, 0))
                .setHeadingPID(new PID(1.0, 0, 0))
                .setDeceleration(DECELERATION)
                .setSpeed(1.0)

                .setEndTolerance(0.4, Math.toRadians(2))
                .setEndVelocityTolerance(4);
    }

    public static final Runnable GRAB_SPECIMEN = () -> {};

    public static Runnable getPlaceSpecimen(DeterministicTracker tracker, SlideSubsystem slideSubsystem, ClawSubsystem clawSubsystem, ElapsedTime elapsedTime, LinearOpMode linearOpMode) {
        return () -> {
            elapsedTime.reset();
            while (linearOpMode.opModeIsActive() && elapsedTime.seconds() < 0.5) {
                slideSubsystem.update();
                tracker.update();
            }
            slideSubsystem.setState(BELOW_HIGH_RUNG);
            elapsedTime.reset();
            while (linearOpMode.opModeIsActive() && elapsedTime.seconds() < 1.0) {
                slideSubsystem.update();
                tracker.update();
            }

            elapsedTime.reset();
            clawSubsystem.setState(ClawSubsystem.ClawState.OPEN);

            while (linearOpMode.opModeIsActive() && elapsedTime.seconds() < 0.5) {
                clawSubsystem.update();
                tracker.update();
            }

            slideSubsystem.setState(INTAKE);
        };
    }

    public static Runnable getGrabSpecimen(DeterministicTracker tracker, SlideSubsystem slideSubsystem, ClawSubsystem clawSubsystem, ElapsedTime elapsedTime, LinearOpMode linearOpMode) {
        return () -> {
            clawSubsystem.setState(ClawSubsystem.ClawState.CLOSE);
            clawSubsystem.update();

            elapsedTime.reset();

            while (linearOpMode.opModeIsActive() && elapsedTime.seconds() < 0.2) {
                tracker.update();
            }

            slideSubsystem.setState(ABOVE_HIGH_RUNG);

            elapsedTime.reset();
            while (linearOpMode.opModeIsActive() && elapsedTime.seconds() < 0.5) {
                tracker.update();
                slideSubsystem.update();
            }
        };
    }
}

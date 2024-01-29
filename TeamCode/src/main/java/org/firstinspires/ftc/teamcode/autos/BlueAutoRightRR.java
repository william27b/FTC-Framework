package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.teamcode.debug.config.Constants;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.shplib.vision.ElementDetectionPipelineBlue;
import org.firstinspires.ftc.teamcode.subsystems.VisionSubsystem;

import java.io.File;

@Autonomous(preselectTeleOp = "CenterStage Field Oriented")
public class BlueAutoRightRR extends LinearOpMode {
    private final String soundPath = "/sdcard/FIRST/blocks/sounds";
    private final File soundFile = new File(soundPath + "/Holy Moley.wav");

    public enum State {
        GEN_FORWARD,
        GEN_TURN,
        UNKNOWN,

        TURN_TO_PIXEL_1,
        FORWARD_1,
        BACK_1,
        TURN_TO_BACKDROP_1,
        TO_BACKDROP_1,

        FORWARD_2,
        TURN_TO_PIXEL_2,
        BACKING_UP_2,
        TURN_TO_BACKDROP_2,
        TO_BACKDROP_2,

        FORWARD_3,
        TO_BACKDROP_3,

        TO_PARKING,
        IDLE
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive sampleMecanumDrive = new SampleMecanumDrive(hardwareMap);
        VisionSubsystem visionSubsystem;
        State currentState;
        ElementDetectionPipelineBlue.LocationPosition location;

        Servo cameraServo = hardwareMap.get(Servo.class, "cameraServo");
        cameraServo.setDirection(Servo.Direction.REVERSE);
        cameraServo.setPosition(Constants.CameraMode.FACING_TEAM_PROP.getPosition());

        Servo claw = hardwareMap.get(Servo.class, "claw");
        claw.setPosition(Constants.CLAW_CLOSE);

        Servo outtake = hardwareMap.get(Servo.class, "outtake");
        outtake.setDirection(Servo.Direction.REVERSE);
        outtake.setPosition(Constants.OUTTAKE_STARTING);

        visionSubsystem = new VisionSubsystem(hardwareMap,"blue");
        location = visionSubsystem.getLocationBlue();
        telemetry.addLine("Trajectory Sequence Ready");
        telemetry.addData("Location: ", location);
        telemetry.update();
        while (opModeInInit() && !isStopRequested()) {
            location = visionSubsystem.getLocationBlue();
            telemetry.addLine("Trajectory Sequence Ready");
            telemetry.addData("Location: ", location);
            telemetry.addData("height", visionSubsystem.detectorBlue.getMaxHeightReadable());
            telemetry.addData("mass", visionSubsystem.detectorBlue.totalValue);
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        if (location == ElementDetectionPipelineBlue.LocationPosition.RIGHT) {
            Trajectory forwardTwo = sampleMecanumDrive.trajectoryBuilder(sampleMecanumDrive.getPoseEstimate())
                    .lineToLinearHeading(new Pose2d(46, 0, Math.toRadians(0)))
                    .build();

            sampleMecanumDrive.followTrajectoryAsync(forwardTwo);
            currentState = State.FORWARD_2;
        } else {
            Trajectory genForward = sampleMecanumDrive.trajectoryBuilder(sampleMecanumDrive.getPoseEstimate())
                    .lineToLinearHeading(new Pose2d(28, 0, Math.toRadians(0)))
                    .build();

            sampleMecanumDrive.followTrajectoryAsync(genForward);
            currentState = State.GEN_FORWARD;
        }

        while (opModeIsActive() && !isStopRequested()) {
            sampleMecanumDrive.update();

            telemetry.addData("Current State", currentState);
            telemetry.addData("max height", visionSubsystem.detectorBlue.getMaxHeightReadable());
            telemetry.update();

            switch (currentState) {
                case GEN_FORWARD:
                    if (!sampleMecanumDrive.isBusy()) {
                        sampleMecanumDrive.turnAsync(Math.toRadians(-90));

                        currentState = State.GEN_TURN;
                    }
                    break;
                case GEN_TURN:
                    if (!sampleMecanumDrive.isBusy()) {
                        Trajectory genLeft = sampleMecanumDrive.trajectoryBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(28, 7, Math.toRadians(-90)))
                                .build();

                        sampleMecanumDrive.followTrajectoryAsync(genLeft);

                        currentState = State.UNKNOWN;
                    }
                    break;
                case UNKNOWN:
                    if (!sampleMecanumDrive.isBusy()) {
                        sleep(500);

                        if (visionSubsystem.detectorBlue.getMaxHeightReadable() < 170) {
                            sampleMecanumDrive.turnAsync(Math.toRadians(180));

                            currentState = State.TURN_TO_PIXEL_1;
                        } else {
                            Trajectory unknownForwardThree = sampleMecanumDrive.trajectoryBuilder(sampleMecanumDrive.getPoseEstimate())
                                    .lineToLinearHeading(new Pose2d(28, -6, Math.toRadians(-90)))
                                    .build();

                            sampleMecanumDrive.followTrajectoryAsync(unknownForwardThree);

                            currentState = State.FORWARD_3;
                        }
                    }
                    break;

                // Path series 1

                case TURN_TO_PIXEL_1:
                    if (!sampleMecanumDrive.isBusy()) {
                        outtake.setPosition(Constants.OUTTAKE_NEUTRAL);
                        sleep(2000);

                        Trajectory forwardOne = sampleMecanumDrive.trajectoryBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(28, 9.75, Math.toRadians(90)))
                                .build();

                        sampleMecanumDrive.followTrajectoryAsync(forwardOne);

                        currentState = State.FORWARD_1;
                    }
                    break;

                case FORWARD_1:
                    if (!sampleMecanumDrive.isBusy()) {
                        claw.setPosition(Constants.CLAW_OPEN);
                        sleep(1000);

                        Trajectory backOne = sampleMecanumDrive.trajectoryBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(28, 0, Math.toRadians(90)))
                                .build();

                        sampleMecanumDrive.followTrajectoryAsync(backOne);

                        currentState = State.BACK_1;
                    }
                    break;

                case BACK_1:
                    if (!sampleMecanumDrive.isBusy()) {
                        sampleMecanumDrive.turnAsync(Math.toRadians(180));

                        currentState = State.TURN_TO_BACKDROP_1;
                    }
                    break;

                case TURN_TO_BACKDROP_1:
                    if (!sampleMecanumDrive.isBusy()) {
                        outtake.setPosition(Constants.OUTTAKE_LOWERED);

                        TrajectorySequence toBackdropOne = sampleMecanumDrive.trajectorySequenceBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(28+23.5, 0, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(28+23.5, 36+47, Math.toRadians(-90)))
                                .addSpatialMarker(new Vector2d(28+23.5, 25), () -> outtake.setPosition(Constants.OUTTAKE_NEUTRAL))
                                .lineToLinearHeading(new Pose2d(20, 36+47, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(20, 42+47, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(20, 42+47.5, Math.toRadians(-90)))
                                .build();

                        sampleMecanumDrive.followTrajectorySequenceAsync(toBackdropOne);

                        currentState = State.TO_BACKDROP_1;
                    }
                    break;

                case TO_BACKDROP_1:
                    if (!sampleMecanumDrive.isBusy()) {
                        outtake.setPosition(Constants.OUTTAKE_ACTIVE);
                        sleep(2000);

                        TrajectorySequence backdropOneToParking = sampleMecanumDrive.trajectorySequenceBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(20, 40+47, Math.toRadians(-90)))
                                .build();

                        sampleMecanumDrive.followTrajectorySequenceAsync(backdropOneToParking);

                        currentState = State.TO_PARKING;
                    }
                    break;

                // Path series 2
                case FORWARD_2:
                    if (!sampleMecanumDrive.isBusy()) {
                        sampleMecanumDrive.turnAsync(Math.toRadians(180));

                        currentState = State.TURN_TO_PIXEL_2;
                    }
                    break;
                case TURN_TO_PIXEL_2:
                    if (!sampleMecanumDrive.isBusy()) {
                        claw.setPosition(Constants.CLAW_OPEN);

                        Trajectory backingUp = sampleMecanumDrive.trajectoryBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(50, 0, Math.toRadians(180)))
                                .build();

                        sampleMecanumDrive.followTrajectoryAsync(backingUp);

                        currentState = State.BACKING_UP_2;
                    }
                    break;
                case BACKING_UP_2:
                    if (!sampleMecanumDrive.isBusy()) {
                        sampleMecanumDrive.turnAsync(Math.toRadians(90));

                        currentState = State.TURN_TO_BACKDROP_2;
                    }
                    break;
                case TURN_TO_BACKDROP_2:
                    if (!sampleMecanumDrive.isBusy()) {
                        outtake.setPosition(Constants.OUTTAKE_LOWERED);

                        TrajectorySequence spikeMarkTwoToBackdrop = sampleMecanumDrive.trajectorySequenceBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(50, 38+47, Math.toRadians(-90)))
                                .addSpatialMarker(new Vector2d(50, 5), () -> outtake.setPosition(Constants.OUTTAKE_LOWERED))
                                .addSpatialMarker(new Vector2d(50, 23), () -> outtake.setPosition(Constants.OUTTAKE_NEUTRAL))
                                .lineToLinearHeading(new Pose2d(28.75, 38+47, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(28.75, 43+47, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(28.75, 44+47, Math.toRadians(-90)))
                                .build();

                        sampleMecanumDrive.followTrajectorySequenceAsync(spikeMarkTwoToBackdrop);

                        currentState = State.TO_BACKDROP_2;
                    }
                    break;
                case TO_BACKDROP_2:
                    if (!sampleMecanumDrive.isBusy()) {
                        outtake.setPosition(Constants.OUTTAKE_ACTIVE);
                        sleep(1000);

                        TrajectorySequence spikeMarkTwoToParking = sampleMecanumDrive.trajectorySequenceBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(28.75, 40+47, Math.toRadians(-90)))
                                .build();

                        sampleMecanumDrive.followTrajectorySequenceAsync(spikeMarkTwoToParking);

                        currentState = State.TO_PARKING;
                    }
                    break;

                // Path series 3
                case FORWARD_3:
                    if (!sampleMecanumDrive.isBusy()) {
                        outtake.setPosition(Constants.OUTTAKE_NEUTRAL);
                        claw.setPosition(Constants.CLAW_OPEN);
                        sleep(1000);

                        TrajectorySequence forwardThreeToBackdropThree = sampleMecanumDrive.trajectorySequenceBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(28, 0, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(28+23.5, 0, Math.toRadians(-90)))
                                .addSpatialMarker(new Vector2d(28+23.5, 0), () -> outtake.setPosition(Constants.OUTTAKE_LOWERED))
                                .lineToLinearHeading(new Pose2d(28+23.5, 38+47, Math.toRadians(-90)))
                                .addSpatialMarker(new Vector2d(28+23.5, 25), () -> outtake.setPosition(Constants.OUTTAKE_NEUTRAL))
                                .lineToLinearHeading(new Pose2d(24, 38+47, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(24, 43+47, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(28, 43+47, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(28.5, 43.5+47, Math.toRadians(-90)))
                                .build();

                        sampleMecanumDrive.followTrajectorySequenceAsync(forwardThreeToBackdropThree);

                        currentState = State.TO_BACKDROP_3;
                    }
                    break;
                case TO_BACKDROP_3:
                    if (!sampleMecanumDrive.isBusy()) {
                        outtake.setPosition(Constants.OUTTAKE_ACTIVE);
                        sleep(1000);

                        TrajectorySequence spikeMarkThreeToParking = sampleMecanumDrive.trajectorySequenceBuilder(sampleMecanumDrive.getPoseEstimate())
                                .lineToLinearHeading(new Pose2d(28.5, 40+47, Math.toRadians(-90)))
                                .build();

                        sampleMecanumDrive.followTrajectorySequenceAsync(spikeMarkThreeToParking);

                        currentState = State.TO_PARKING;
                    }

                    break;

                // Common states

                case TO_PARKING:
                    if (!sampleMecanumDrive.isBusy()) {
                        outtake.setPosition(Constants.OUTTAKE_HIDDEN);

                        currentState = State.IDLE;
                    }

                    break;

                case IDLE:
                    break;
            }
        }
    }
}
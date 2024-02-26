package org.firstinspires.ftc.teamcode.autos.PurePursuit;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.debug.MecanumController;
import org.firstinspires.ftc.teamcode.debug.PurePursuit.Geometry.Position2D;
import org.firstinspires.ftc.teamcode.debug.PurePursuit.PurePursuitFollower;
import org.firstinspires.ftc.teamcode.debug.PurePursuit.PurePursuitPath;
import org.firstinspires.ftc.teamcode.debug.config.Constants;

@Autonomous(preselectTeleOp = "Centerstage Field Oriented")
public class Blue102 extends BaseAuto {
    @Override
    public void runOpMode() throws InterruptedException {
        this.side = Side.BLUE;

        PurePursuitFollower purePursuitFollower = new PurePursuitFollower(hardwareMap);
        MecanumController mecanumController = new MecanumController(hardwareMap);

        Servo outtake = hardwareMap.get(Servo.class, "outtake");
        outtake.setDirection(Servo.Direction.REVERSE);
        outtake.setPosition(Constants.OUTTAKE_STARTING);

        Servo claw = hardwareMap.get(Servo.class, "claw");
        claw.setDirection(Servo.Direction.REVERSE);

        PurePursuitPath path;
        PurePursuitPath pathFar = new PurePursuitPath.PurePursuitPathBuilder()
                .addAction(() -> {
                    claw.setPosition(Constants.CLAW_CLOSE);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .moveTo(new Position2D(-0, 30, Math.toRadians(90)))
                .rotateTo(new Position2D(-0, 30, Math.toRadians(90)), Math.toRadians(0))
                .moveTo(new Position2D(-0, 37, Math.toRadians(0)))
                .addAction(() -> claw.setPosition(Constants.CLAW_OPEN))
                .moveTo(new Position2D(-49, 40, Math.toRadians(0)))
                .addAction(2, () -> {
                    mecanumController.deactivate();
                    outtake.setPosition(Constants.OUTTAKE_LOWERED);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .moveTo(new Position2D(-44, 38, Math.toRadians(0)))
                .addAction(() -> outtake.setPosition(Constants.OUTTAKE_HIDDEN))
                .moveTo(new Position2D(-44, 60, Math.toRadians(0)))
                .moveTo(new Position2D(-52, 60, Math.toRadians(0)))

                .enableRetrace()
                .build();
        PurePursuitPath pathCenter = new PurePursuitPath.PurePursuitPathBuilder()
                .addAction(() -> {
                    claw.setPosition(Constants.CLAW_CLOSE);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .moveTo(new Position2D(-0, 37.5, Math.toRadians(90)))
                .addAction(() -> claw.setPosition(Constants.CLAW_OPEN))
                .moveTo(new Position2D(-0, 29.5, Math.toRadians(90)))
                .rotateTo(new Position2D(-0, 29.5, Math.toRadians(90)), Math.toRadians(0))
                .addAction(() -> outtake.setPosition(Constants.OUTTAKE_NEUTRAL))
                .moveTo(new Position2D(-49, 34, Math.toRadians(0)))
                .addAction(2, () -> {
                    mecanumController.deactivate();
                    outtake.setPosition(Constants.OUTTAKE_LOWERED);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .moveTo(new Position2D(-44, 34.5, Math.toRadians(0)))
                .addAction(() -> outtake.setPosition(Constants.OUTTAKE_HIDDEN))
                .moveTo(new Position2D(-44, 60, Math.toRadians(0)))
                .moveTo(new Position2D(-52, 60, Math.toRadians(0)))

                .enableRetrace()
                .build();
        PurePursuitPath pathClose = new PurePursuitPath.PurePursuitPathBuilder()
                .addAction(() -> {
                    claw.setPosition(Constants.CLAW_CLOSE);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .moveTo(new Position2D(-23.5, 40, Math.toRadians(90)))
                .rotateTo(new Position2D(-23.5, 40, Math.toRadians(90)), Math.toRadians(0))
                .addAction(() -> claw.setPosition(Constants.CLAW_OPEN))
                .moveTo(new Position2D(-49, 28, Math.toRadians(0)))
                .addAction(2, () -> {
                    mecanumController.deactivate();
                    outtake.setPosition(Constants.OUTTAKE_LOWERED);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .moveTo(new Position2D(-44, 28, Math.toRadians(0)))
                .addAction(() -> outtake.setPosition(Constants.OUTTAKE_HIDDEN))
                .moveTo(new Position2D(-44, 60, Math.toRadians(0)))
                .moveTo(new Position2D(-52, 60, Math.toRadians(0)))

                .enableRetrace()
                .build();

        super.runOpMode();
        switch (this.location) {
            case FAR:
                path = pathFar;
                break;
            case CLOSE:
                path = pathClose;
                break;
            default:
                path = pathCenter;
                break;
        }
        path.followAsync(purePursuitFollower, mecanumController);

        while (opModeIsActive() && !isStopRequested()) {
            path.update();

            telemetry.addData("x", purePursuitFollower.getCurrentPosition().getX());
            telemetry.addData("y", purePursuitFollower.getCurrentPosition().getY());
            telemetry.addData("r", purePursuitFollower.getCurrentPosition().getHeadingRadians());
            telemetry.update();
        }

        mecanumController.deactivate();
    }
}
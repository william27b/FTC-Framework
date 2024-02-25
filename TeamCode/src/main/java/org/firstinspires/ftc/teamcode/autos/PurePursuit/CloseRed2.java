package org.firstinspires.ftc.teamcode.autos.PurePursuit;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.debug.MecanumController;
import org.firstinspires.ftc.teamcode.debug.PurePursuit.Geometry.Position2D;
import org.firstinspires.ftc.teamcode.debug.PurePursuit.PurePursuitFollower;
import org.firstinspires.ftc.teamcode.debug.PurePursuit.PurePursuitPath;
import org.firstinspires.ftc.teamcode.debug.config.Constants;
import org.firstinspires.ftc.teamcode.subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElbowSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem;

@Autonomous
public class CloseRed2 extends BaseAuto {
    PurePursuitPath path1;
    PurePursuitPath path2;
    PurePursuitPath path3;

    PurePursuitPath path;

    PurePursuitFollower purePursuitFollower;
    MecanumController mecanumController;

    @Override
    public void init() {
        this.side = Side.RED;

        double tanhPace = 0.5;
        double minimumTanh = Constants.minimumTanh;
        double maximumTanh = 0.75;

        path1 = new PurePursuitPath.PurePursuitPathBuilder()
                .moveTo(new Position2D(0, -23, Math.toRadians(90)))
                .rotateTo(new Position2D(0, -23, Math.toRadians(90)), Math.toRadians(180))
                .moveTo(new Position2D(6, -23, Math.toRadians(180)))
                .moveTo(new Position2D(0, -23, Math.toRadians(180)))
                .rotateTo(new Position2D(-12, -23, Math.toRadians(90)), Math.toRadians(0))
                .addAction(() -> {
                    arm.setState(ArmSubsystem.State.EXTENDED);
                    arm.setSlidePos(250);
                    elbow.setState(ElbowSubsystem.State.UP);
                    wrist.setState(WristSubsystem.State.UP);
                })
                .moveTo(new Position2D(-44, -29, Math.toRadians(0)))
                .addAction(2, () -> {
                    mecanumController.deactivate();
                    intake.crWheel.setPower(-1.0);
                    intake.pixelServo.setPosition(0.5);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    intake.crWheel.setPower(0);
                    intake.pixelServo.setPosition(0.9);
                    arm.setState(ArmSubsystem.State.BOTTOM);
                    elbow.setState(ElbowSubsystem.State.DOWN);
                    wrist.setState(WristSubsystem.State.HALFWAY);
                })
                .moveTo(new Position2D(-40, -29, Math.toRadians(0)))
                .moveTo(new Position2D(-40, -1, Math.toRadians(0)))
                .moveTo(new Position2D(-52, -1, Math.toRadians(0)))

                .enableRetrace()
                .enableTanh(tanhPace, minimumTanh, maximumTanh)
                .build();

        path2 = new PurePursuitPath.PurePursuitPathBuilder()
                .moveTo(new Position2D(0, -31.5, Math.toRadians(90)))
                .moveTo(new Position2D(0, -22, Math.toRadians(90)))
                .rotateTo(new Position2D(0, -22, Math.toRadians(90)), Math.toRadians(0))
                .addAction(() -> {
                    arm.setState(ArmSubsystem.State.EXTENDED);
                    arm.setSlidePos(200);
                    elbow.setState(ElbowSubsystem.State.UP);
                    wrist.setState(WristSubsystem.State.UP);
                })
                .moveTo(new Position2D(-44.5, -26, Math.toRadians(0)))
                .addAction(2, () -> {
                    mecanumController.deactivate();
                    intake.crWheel.setPower(-1.0);
                    intake.pixelServo.setPosition(0.5);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    intake.crWheel.setPower(0);
                    intake.pixelServo.setPosition(0.9);
                    arm.setState(ArmSubsystem.State.BOTTOM);
                    elbow.setState(ElbowSubsystem.State.DOWN);
                    wrist.setState(WristSubsystem.State.HALFWAY);
                })
                .moveTo(new Position2D(-40, -24, Math.toRadians(0)))
                .moveTo(new Position2D(-40, -1, Math.toRadians(0)))
                .moveTo(new Position2D(-52, -1, Math.toRadians(0)))

                .enableRetrace()
                .enableTanh(tanhPace, minimumTanh, maximumTanh)
                .build();

        path3 = new PurePursuitPath.PurePursuitPathBuilder()
                .moveTo(new Position2D(-14, -25, Math.toRadians(90)))
                .moveTo(new Position2D(-14, -16, Math.toRadians(90)))
                .rotateTo(new Position2D(-14, -16, Math.toRadians(90)), Math.toRadians(0))
                .addAction(() -> {
                    arm.setState(ArmSubsystem.State.EXTENDED);
                    arm.setSlidePos(250);
                    elbow.setState(ElbowSubsystem.State.UP);
                    wrist.setState(WristSubsystem.State.UP);
                })
                .moveTo(new Position2D(-45, -19, Math.toRadians(0)))
                .addAction(2, () -> {
                    mecanumController.deactivate();
                    intake.crWheel.setPower(-1.0);
                    intake.pixelServo.setPosition(0.5);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    intake.crWheel.setPower(0);
                    intake.pixelServo.setPosition(0.9);
                    arm.setState(ArmSubsystem.State.BOTTOM);
                    elbow.setState(ElbowSubsystem.State.DOWN);
                    wrist.setState(WristSubsystem.State.HALFWAY);
                })
                .moveTo(new Position2D(-40, -19, Math.toRadians(0)))
                .moveTo(new Position2D(-40, -1, Math.toRadians(0)))
                .moveTo(new Position2D(-52, -1, Math.toRadians(0)))

                .enableRetrace()
                .enableTanh(tanhPace, minimumTanh, maximumTanh)
                .build();

        purePursuitFollower = new PurePursuitFollower(hardwareMap);
        mecanumController = new MecanumController(hardwareMap);

        super.init();
    }

    @Override
    public void init_loop() {
        super.init_loop();
    }

    @Override
    public void start() {
        if (this.getLocation() == 1) {
            path = path1;
        } else if (this.getLocation() == 2) {
            path = path2;
        } else {
            path = path3;
        }

        path.followAsync(purePursuitFollower, mecanumController);

        super.start();
    }

    @Override
    public void loop() {
        telemetry.addData("x", purePursuitFollower.getCurrentPosition().getX());
        telemetry.addData("y", purePursuitFollower.getCurrentPosition().getY());
        telemetry.addData("r", purePursuitFollower.getCurrentPosition().getHeadingRadians());
        telemetry.addLine();
        telemetry.addData("finished", path.isFinished());
        telemetry.addData("failed", path.failed());
        telemetry.addLine();
        telemetry.addData("geometries size", path.geometries.size());
        telemetry.addData("current geometry", path.getCurrentGeometry());
        if (path.geometries.size() > path.getCurrentGeometry()) {
            telemetry.addData("", path.geometries.get(path.getCurrentGeometry()));
        }
        telemetry.addLine();

        path.update();

        super.loop();
    }
}
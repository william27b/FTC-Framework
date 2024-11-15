package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.shprobotics.pestocore.drivebases.MecanumController;
import com.shprobotics.pestocore.drivebases.MecanumTracker;
import com.shprobotics.pestocore.drivebases.TeleOpController;
import com.shprobotics.pestocore.geometries.Vector2D;
@TeleOp
public class test extends LinearOpMode {
    boolean slideon=false;
    double wristPos=0.0;
    double clawpos=0.0;
    String mode ="Driving";
    int maxwormgearpos=1000;
    int doublespeed=1;
    int count=0;
    @Override
    public void runOpMode() {
        MecanumController mecanumController = PestoFTCConfig.getMecanumController(hardwareMap);
        MecanumTracker mecanumTracker = PestoFTCConfig.getTracker(hardwareMap);
        TeleOpController teleOpController = PestoFTCConfig.getTeleOpController(mecanumController, mecanumTracker, hardwareMap);

        DcMotor viperslide = hardwareMap.get(DcMotor.class, "ViperSlide");
        viperslide.setDirection(DcMotorSimple.Direction.REVERSE);
        viperslide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Servo wrist = hardwareMap.get(Servo.class, "wrist");

        DcMotor wormgear = hardwareMap.get(DcMotor.class, "WormGear");
        Servo claw = hardwareMap.get(Servo.class, "Claw");

        waitForStart();

        while (opModeIsActive()) {
            mecanumTracker.updateOdometry();
            Vector2D currentPosition = mecanumTracker.getCurrentPosition();
            double heading = mecanumTracker.getCurrentHeading();
            teleOpController.updateSpeed(gamepad1);
            teleOpController.driveRobotCentric(gamepad1.left_stick_y * doublespeed, -gamepad1.left_stick_x * doublespeed, -gamepad1.right_stick_x);

//            teleOpController.driveFieldCentric(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

//            telemetry.addData("x", currentPosition.getX());
//            telemetry.addData("y", currentPosition.getY());
//            telemetry.addData("rotation", heading);
            telemetry.addData("viper", viperslide.getCurrentPosition());
            telemetry.addData("wormgear", wormgear.getCurrentPosition());
            telemetry.addData("claw", clawpos);
            telemetry.addData("wrist", wristPos);
            telemetry.addData("slideon: ", slideon);
            telemetry.addData("moveSpeed: x", doublespeed);

            telemetry.update();
//claw close:0.3
//claw open:0.7
//wrist close:
//wrist open:
            if (gamepad1.left_stick_y >= 0.6) {
                if( (count % 2)==0 ) {
                while (gamepad1.left_stick_y>=0.6) {
                }
                count += 1;
                if (count==4){
                    count=0;
                    if (doublespeed==2) {
                        doublespeed = 1;
                    }else{
                            doublespeed=1;

                        }
                    }

                gamepad1.rumble(1000);
                }
                }
            }
            if (gamepad1.left_stick_y <= -0.6) {
                if( (count % 2)==1 ) {
                    while (gamepad1.left_stick_y<=-0.6) {
                    }
                    count += 1;
                }
            }


            if (gamepad1.left_stick_x >= 0.6 || gamepad1.left_stick_x <= -0.6) {
                count =0;

            }

            if (gamepad1.b) {
                mecanumTracker.reset();
                teleOpController.resetIMU();

            }

            wormgear.setPower((gamepad1.b ? 0.6: 0) - (gamepad1.x ? 0.6: 0));

            if (gamepad1.y) {
                viperslide.setTargetPosition(2150);
                viperslide.setPower(0.4);
                viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            }
//            if (viperslide.getCurrentPosition()<=0){
//                viperslide.setPower(0);
//            }
            if (viperslide.getCurrentPosition()>=2000){
                    viperslide.setPower(0.1);



            }
            if (wormgear.getCurrentPosition()>=maxwormgearpos){
                wormgear.setPower(0);



            }
            if (gamepad1.a) {

                viperslide.setTargetPosition(0);
                viperslide.setPower(0.4);
                viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            }

            if (gamepad2.dpad_down){
                viperslide.setTargetPosition(0);
                viperslide.setPower(0.4);
                viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                wormgear.setTargetPosition(0);
                viperslide.setPower(0.4);
                viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            }


            if (gamepad2.left_bumper){
                while (gamepad2.left_bumper) {
                    // Do nothing, just wait
                }
                if(mode.equals("Driving")){
                    mode ="Intake";
                    viperslide.setTargetPosition(0);
                    viperslide.setPower(0.4);
                    viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    wormgear.setTargetPosition(maxwormgearpos);
                    wormgear.setPower(0.4);
                    wormgear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    claw.setPosition(0.3);
                    wrist.setPosition(1);
                }
                if(mode.equals("Intake")){
                    mode ="Outtake";
                    viperslide.setTargetPosition(2150);
                    viperslide.setPower(0.4);
                    viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    wormgear.setTargetPosition(100);
                    wormgear.setPower(0.4);
                    wormgear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    claw.setPosition(0.3);
                    wrist.setPosition(1);

                }
                if(mode.equals("Outtake")){
                    mode ="Driving";
                    viperslide.setTargetPosition(0);
                    viperslide.setPower(0.4);
                    viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    wormgear.setTargetPosition(0);
                    wormgear.setPower(0.4);
                    wormgear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    claw.setPosition(0.3);
                    wrist.setPosition(0.5);
                }



            }
            if (gamepad1.dpad_left) {

                claw.setPosition(0.3);
                }

            if (gamepad1.dpad_right) {
                claw.setPosition(0.7);
            }

            if (gamepad1.dpad_up) {
                if (wristPos<1) {
                    wristPos+=0.05;

                    wrist.setPosition(wristPos);
                }
            }

            if (gamepad1.dpad_down) {
                if (wristPos>0) {
                    wristPos-=0.05;

                    wrist.setPosition(wristPos);
                }
            }
            }

            //telemetry.addData("Motor 0:",)
        }
    }


package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.TestBaseRobot;
import org.firstinspires.ftc.teamcode.commands.DecrementDownArmCommand;
import org.firstinspires.ftc.teamcode.commands.IncrementUpArmCommand;
import org.firstinspires.ftc.teamcode.commands.LowerArmCommand;
import org.firstinspires.ftc.teamcode.commands.PrepareClimbCommand;
import org.firstinspires.ftc.teamcode.shplib.commands.RunCommand;
import org.firstinspires.ftc.teamcode.shplib.commands.Trigger;
import org.firstinspires.ftc.teamcode.shplib.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.shplib.utility.Clock;
import org.firstinspires.ftc.teamcode.subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PlaneServo;

@TeleOp
public class AndyRandy extends TestBaseRobot {
    private double debounce;
    private double driveBias;

    @Override
    public void init() {
        super.init();

        // Default command runs when no other commands are scheduled for the subsystem
        drive.setDefaultCommand(
                new RunCommand(
                        () -> drive.mecanum(-gamepad1.left_stick_y*driveBias, -gamepad1.left_stick_x*driveBias, -gamepad1.right_stick_x*driveBias)
                )
        );
//        intake.setDefaultCommand(new RunCommand(
//                () -> intake.setState(IntakeSubsystem.State.STILL)
//        ));
//        vision.setDefaultCommand(
//                new RunCommand(
//                        () -> vision.showRes(telemetry)
//                )
//        );
        driveBias = 1;
    }

    @Override
    public void start() {
        super.start();

        debounce = Clock.now();
        // Add anything that needs to be run a single time when the OpMode starts
    }

    @Override
    public void loop() {
        // Allows CommandScheduler.run() to be called - DO NOT DELETE!
        super.loop();
/*
TODO:
- slide up/down incremental - bumpers - DONE
- intake in/out - triggers - DONE
- slide all the way down - x - DONE
- reset IMU - square - DONE
- pixel(?) outtake - triangle - DONE
- toggle speed - left stick button
- climbing - DONE?
- airplane - DONE?

 */

        drive.setDriveBias(arm.getDriveBias());
        ////spinning intake
        new Trigger(gamepad1.left_stick_button, new RunCommand(() -> {
            if (!Clock.hasElapsed(debounce, 0.5)) return;
            if(driveBias == 0.3)
                driveBias = 1.0;
            else
                driveBias = 0.3;
        }));
        new Trigger(gamepad1.left_bumper,
            new LowerArmCommand(arm, wrist, elbow)
        );
//        new Trigger(gamepad1.right_trigger>0.5, new RunCommand(() -> {
//            intake.setState(IntakeSubsystem.State.INTAKE);
//            telemetry.addData("INTAKING", 0);
//
//        }));
        new Trigger((gamepad1.right_trigger<0.5 && gamepad1.left_trigger<0.5&&!gamepad1.triangle), new RunCommand(() -> {
            if(intake.getState() != IntakeSubsystem.State.DEPOSIT1)
                intake.setState(IntakeSubsystem.State.STILL);
        }));
        new Trigger((gamepad1.right_trigger>0.5 && arm.getState() == ArmSubsystem.State.BOTTOM), new RunCommand(() -> {
            intake.setState(IntakeSubsystem.State.INTAKE);
        }));
        new Trigger((gamepad1.left_trigger>0.5 && arm.getState() == ArmSubsystem.State.BOTTOM), new RunCommand(() -> {
            intake.setState(IntakeSubsystem.State.REJECTALL);
        }));
        new Trigger(gamepad1.triangle, new RunCommand(() -> {
//            if (!Clock.hasElapsed(debounce, 0.1)) return;
            if (intake.getState() != IntakeSubsystem.State.STILL) { //2. if no pixels have been released
                intake.setState(IntakeSubsystem.State.DEPOSIT2);       //   release pixel #1
                telemetry.addData("Pixels Deposited: ", 2);
            }
            else {                                                //3. if pixel #1 has been released
                intake.setState(IntakeSubsystem.State.DEPOSIT1);  //   release pixel #2
                telemetry.addData("Pixels Deposited: ", 1);
            }
        }));
        new Trigger(gamepad1.square, new RunCommand(() -> {
            drive.resetIMUAngle();
        }));
        new Trigger(gamepad1.right_bumper,
            new IncrementUpArmCommand(arm,wrist,elbow)
        );
        new Trigger(gamepad1.cross,
            new DecrementDownArmCommand(arm,wrist,elbow)
        );

        new Trigger (gamepad1.dpad_left, new RunCommand(()->{
            if (!Clock.hasElapsed(debounce, 60)) return;//TODO: TEST
            planeServo.setState(PlaneServo.State.OUT);
        })
        );
        new Trigger (gamepad2.x, new RunCommand(()->{
            intake.setState(IntakeSubsystem.State.REJECT);
        })
        );
        new Trigger (gamepad1.dpad_up,
                    new PrepareClimbCommand(arm, wrist, elbow)
        );
        new Trigger (gamepad1.dpad_down,
                new RunCommand(()->{
                    arm.setState(ArmSubsystem.State.FINISHCLIMB);
                })
        );


//        //from left trigger to gamepad1
//        new Trigger(gamepad1.a, new RunCommand(() ->{
//            if(driveBias==1){
//                driveBias=0.5;
//            }
//            else{
//                driveBias=1;
//            }
//        })
//        );
//        new Trigger (gamepad1.left_bumper,
//                new LowerArmCommand(arm,wrist,elbow)
//                        .then(new RunCommand(()->{
//                            intake.putPixelServoBack();
//                        }))
//        );
//        new Trigger ((gamepad1.left_trigger<0.5 && gamepad1.right_trigger<0.5 && gamepad1.y && gamepad1.dpad_down), new RunCommand(()->{
//            if(intake.getState()!=IntakeSubsystem.State.OUTAKE1){
//                intake.setState(IntakeSubsystem.State.STILL);}
//        })
//        );
//        new Trigger (gamepad1.right_trigger>=0.5, new RunCommand(()->{
//            intake.setState(IntakeSubsystem.State.REJECT);
//            offset = 0;
//        })
//        );
//        new Trigger (gamepad1.x,new RunCommand(()->{
//            drive.resetIMUAngle();
//        })
//        );
//
//        // TODO fix servo
//        new Trigger (gamepad1.left_trigger>=0.5, new RunCommand(()->{//1. if arm is at bottom
//            intake.setState(IntakeSubsystem.State.INTAKING);   //   intake pixels
//            telemetry.addData("State1", 0);
//        })
//        );
//
//        new Trigger (gamepad1.y, new RunCommand(()->{
//            if (intake.getState() == IntakeSubsystem.State.STILL) { //2. if no pixels have been released
//                intake.setState(IntakeSubsystem.State.OUTAKE1);       //   release pixel #1
//                telemetry.addData("State2", 1);
//            }
//            else {                                                   //3. if pixel #1 has been released
//                intake.setState(IntakeSubsystem.State.OUTTAKING);  //   release pixel #2
//                telemetry.addData("state3", 2);
//            }
//        })
//        );
//
//        new Trigger (gamepad2.dpad_left, new RunCommand(()->{
//            planeServo.setState(PlaneServo.State.OUT);
//        })
//        );
//
//        new Trigger (gamepad2.dpad_up, new RunCommand(()->{
//            arm.setState(ArmSubsystem.State.CLIMB);
//        })
//        );
//
//        new Trigger (gamepad1.b, new RunCommand(()->{
//            arm.setState(ArmSubsystem.State.SAFETY);
//        })
//        );
//
//        new Trigger (gamepad2.dpad_down,
//                new RunCommand(()->{
//                    arm.setState(ArmSubsystem.State.BOTTOMCLIMB);
//                })
//                        .then(new WaitCommand(2))
//                        .then(new RunCommand(()->{
//                            hook.setState(HookSubsystem.State.ENGAGED);
//                        }))
//                        .then(new WaitCommand(0.5))
//                        .then(new RunCommand(()->{
//                            arm.setState(ArmSubsystem.State.FINISHCLIMB);
//                        }))
//        );
//
//        new Trigger(gamepad1.dpad_down,
//            new RunCommand(()->{
//                intake.setState(IntakeSubsystem.State.OUTNOWHEEL);
//        }));

//        new Trigger (gamepad1.dpad_up, new RunCommand(()->{
//            arm.setState(ArmSubsystem.State.CLIMB);
//        })
////
//        );



//        new Trigger (gamepad1.dpad_right, new RunCommand(()->{
//            planeServo.setState(PlaneServo.State.IN);
//        })
//
//        );


//        new Trigger (gamepad1.b, new RunCommand(()->{
//            rightPlane.setState(HookServo1.State.DISENGAGED);
//            })
//                .then(new RunCommand(()->{
//                    leftPlane.setState(HookServo2.State.DISENGAGED);
//            }))
////
//        );
//        new Trigger (gamepad2.dpad_up, new RunCommand(()->{
//            slideServos.setState(PracticeArmServo.State.UP);
//        }));
//        new Trigger (gamepad2.dpad_down, new RunCommand(()->{
//            slideServos.setState(PracticeArmServo.State.DOWN);
//        }));
//
//        new Trigger (gamepad2.dpad_up, new RunCommand(()->{
//            slideServos.setState(PracticeArmServo.State.UP);
//        }));
//
//        new Trigger (gamepad1.a, new RunCommand(()->{
//            crWheel.setState(CRWheel.State.STILL);
//        }));
//        new Trigger (gamepad1.b, new RunCommand(()->{
//            crWheel.setState(CRWheel.State.FORWARD);
//        }));
//        new Trigger (gamepad1.x, new RunCommand(()->{
//            spinningIntake.setState(SpinningIntake.State.IN);
//        }));
//        new Trigger (gamepad1.y, new RunCommand(()->{
//            spinningIntake.setState(SpinningIntake.State.DISABLE);
//        }));
//        new Trigger (gamepad2.y, new RunCommand(()->{
//            pixelServo.setState(PixelServo.State.IN);
//        }));
//        new Trigger (gamepad2.a, new RunCommand(()->{
//            pixelServo.setState(PixelServo.State.OUT);
//        }));

        /*
        new Trigger (gamepad1.a, new RunCommand(()->{
            if(spinningIntake.getState()==SpinningIntake.State.IN){
                spinningIntake.setState(SpinningIntake.State.DISABLE);
                CommandScheduler.getInstance().scheduleCommand(
                    new RunCommand(() -> {
                        crWheel.setState(CRWheel.State.BACKWARD);
                    }));

            }
            else{
                spinningIntake.setState(SpinningIntake.State.IN);
                  CommandScheduler.getInstance().scheduleCommand(
                    new RunCommand(() -> {
                        crWheel.setState(CRWheel.State.FORWARD);
                    })));
            }
        }));
         */

//        new Trigger(gamepad1.right_trigger>0.5, new RunCommand(()->{
//            arm.setState(ArmSubsystem.State.HIGH);
//        }));
//        new Trigger(gamepad1.left_trigger>0.5, new RunCommand(()->{
//            arm.setState(ArmSubsystem.State.BOTTOM);
//        }));



//        new Trigger(gamepad2.a, new RunCommand(()->{
//            slideServos.setState(PracticeArmServo.State.UP);
//        }));
//        new Trigger(gamepad2.b, new RunCommand(()->{
//            slideServos.setState(PracticeArmServo.State.DOWN);
//        }));

//        new Trigger(gamepad1.a, new RunCommand(() -> {
//            if (!Clock.hasElapsed(debounce, 0.5)) return;
//            if (arm.clawClosed()) {
//                arm.openClaw();
//                if (arm.atHub()) {
//                    arm.setState(ArmSubsystem.State.BOTTOM);
//                }
//            } else {
//                arm.closeClaw();
//                CommandScheduler.getInstance().scheduleCommand(
//                        new WaitCommand(0.5)
//                                .then(new RunCommand(() -> {
//                                    if (arm.atStacks()) arm.setState(ArmSubsystem.State.LOW);
//                                    else arm.setState(ArmSubsystem.State.HUB);
//                                }))
//                );
//            }
//            debounce = Clock.now();
//        }));
//        new Trigger(gamepad2.dpad_up, new RunCommand(()->{
//            spinningIntake.setState(SpinningIntake.State.IN);
//        }));
//        new Trigger(gamepad2.dpad_down, new RunCommand(()->{
//            spinningIntake.setState(SpinningIntake.State.OUT);
//        }));
//        new Trigger(gamepad2.a, new RunCommand(()->{
//            spinningIntake.setState(SpinningIntake.State.DISABLE);
//        }));
// Which buttons to map to? CRWheel
//        new Trigger(gamepad2.dpad_up, new RunCommand(()->{
//            cWheel.setState(CRWheel.State.FORWARD);
//        }));
//        new Trigger(gamepad2.dpad_down, new RunCommand(()->{
//            cWheel.setState(CRWheel.State.BACKWARD);
//        }));
//        new Trigger(gamepad2.a, new RunCommand(()->{
//            cWheel.setState(CRWheel.State.STILL);
//        }));

        // Which buttons to map to? Flap
//        new Trigger(gamepad2.b, new RunCommand(()->{
//            flap.setState(Flap.State.OPEN);
//        }));
//        new Trigger(gamepad2.y, new RunCommand(()->{
//            flap.setState(Flap.State.CLOSE);
//        }));
//        new Trigger(gamepad2.x, new RunCommand(()->{
//            flap.setState(Flap.State.NEUTRAL);
//        }));



//        new Trigger(gamepad1.a, new RunCommand(() -> {
//            if (!Clock.hasElapsed(debounce, 0.5)) return;
//            if (arm.clawClosed()) {
//                arm.openClaw();
//                if (arm.atHub()) {
//                    arm.setState(ArmSubsystem.State.BOTTOM);
//                }
//            } else {
//                arm.closeClaw();
//                CommandScheduler.getInstance().scheduleCommand(
//                        new WaitCommand(0.5)
//                                .then(new RunCommand(() -> {
//                                    if (arm.atStacks()) arm.setState(ArmSubsystem.State.LOW);
//                                    else arm.setState(ArmSubsystem.State.HUB);
//                                }))
//                );
//            }
//            debounce = Clock.now();
//        }));
//
//        new Trigger(gamepad1.b, new DropConeCommand(arm));
//
//        new Trigger(gamepad1.x, new RunCommand(() -> {
//            arm.setState(ArmSubsystem.State.STACK);
//        }));

//
//        new Trigger(gamepad1.dpad_right, new RunCommand(() -> {
//            arm.setState(ArmSubsystem.State.MIDDLE);
//        }));
//
//        new Trigger(gamepad1.dpad_left, new RunCommand(() -> {
//            arm.setState(ArmSubsystem.State.LOW);
//        }));
//
//        new Trigger(gamepad1.dpad_down, new RunCommand(() -> {
//            arm.setState(ArmSubsystem.State.BOTTOM);
//        }));
    }
}
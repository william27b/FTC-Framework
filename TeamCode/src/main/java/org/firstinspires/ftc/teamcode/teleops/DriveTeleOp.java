package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.shplib.Constants.Pivot.kWristPos;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.shplib.BaseRobot;
import org.firstinspires.ftc.teamcode.shplib.commands.RunCommand;
import org.firstinspires.ftc.teamcode.shplib.commands.Trigger;
import org.firstinspires.ftc.teamcode.shplib.utility.Clock;
import org.firstinspires.ftc.teamcode.subsystems.used.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.used.PivotSubsystem;

@TeleOp
public class DriveTeleOp extends BaseRobot {

    private double debounce;
    @Override
    public void init(){
        super.init();
        drive.setDefaultCommand(
                new RunCommand(
                        () -> drive.robotmecanum(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x)
                )
        );

    }
    @Override
    public void start(){
        super.start();

        debounce = Clock.now();
    }

    @Override
    public void loop(){
        super.loop();

        debounce = Clock.now();
    }



}

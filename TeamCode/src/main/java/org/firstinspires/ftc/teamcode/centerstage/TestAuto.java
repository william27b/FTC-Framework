package org.firstinspires.ftc.teamcode.centerstage;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.debug.MecanumController;
import org.firstinspires.ftc.teamcode.debug.config.Constants;

@Autonomous(name = "Test Auto")
public class TestAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumController mecanumController = new MecanumController(hardwareMap);

        waitForStart();

        mecanumController.leftFront.setPower(0.3);
        sleep(1000);
        mecanumController.rightFront.setPower(0.3);
        sleep(1000);
        mecanumController.leftRear.setPower(0.3);
        sleep(1000);
        mecanumController.rightRear.setPower(0.3);
        sleep(1000);
    }
}

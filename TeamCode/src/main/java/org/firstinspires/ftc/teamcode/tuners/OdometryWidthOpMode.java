package org.firstinspires.ftc.teamcode.tuners;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.shprobotics.pestocore.drivebases.DeterministicTracker;
import com.shprobotics.pestocore.drivebases.MecanumController;
import com.shprobotics.pestocore.drivebases.ThreeWheelOdometryTracker;
import com.shprobotics.pestocore.tuners.OdometryWidthTuner;

import org.firstinspires.ftc.teamcode.PestoFTCConfig;

@Disabled
@TeleOp
public class OdometryWidthOpMode extends OdometryWidthTuner {
    @Override
    public void setMecanumController(HardwareMap hardwareMap) {
        mecanumController = PestoFTCConfig.getMecanumController(hardwareMap);
    }

    @Override
    public void setTracker(HardwareMap hardwareMap) {
        tracker = (ThreeWheelOdometryTracker) PestoFTCConfig.getTracker(hardwareMap);
    }

    @Override
    public void setTeleOpController(MecanumController mecanumController, DeterministicTracker deterministicTracker, HardwareMap hardwareMap) {
        teleOpController = PestoFTCConfig.getTeleOpController(mecanumController, deterministicTracker, hardwareMap);
    }
}
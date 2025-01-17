package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;

@Config
public class SlideSubsystem {
    public CachingDcMotorEx slideLeft, slideRight;
    public static double power = 0.0;

    public enum SlideState {
        INTAKE (170),
        LOW (2050),
        HIGH (4250);

        SlideState(int position) {
            this.position = position;
        }

        private final int position;

        int getPosition() {
            return this.position;
        }
    }

    private SlideState state;

    public SlideSubsystem(HardwareMap hardwareMap) {
        this.slideLeft = new CachingDcMotorEx((DcMotorEx) hardwareMap.get("slideLeft"));
        this.slideRight = new CachingDcMotorEx((DcMotorEx) hardwareMap.get("slideRight"));

        this.slideLeft.setDirection(DcMotor.Direction.FORWARD);
        this.slideRight.setDirection(DcMotor.Direction.REVERSE);

        this.state = SlideState.INTAKE;
    }

    public void setMode(DcMotor.RunMode mode) {
        this.slideLeft.setMode(mode);
        this.slideRight.setMode(mode);
    }

    public void setPower(double power) {
        this.slideLeft.setPowerResult(power);
        this.slideRight.setPowerResult(power);
    }

    public double getPosition() {
        return (this.slideLeft.getCurrentPosition() + this.slideRight.getCurrentPosition()) * 1.0 / 2;
    }

    public void setState(SlideState state) {
        this.state = state;
    }

    public SlideState getState() {
        return this.state;
    }

    public boolean isBusy() {
        return this.slideLeft.isBusy() || this.slideRight.isBusy();
    }

    public void init() {
        this.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        update();

        this.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.setPower(power);
    }

    public void update() {
        this.slideLeft.setTargetPosition(state.getPosition());
        this.slideRight.setTargetPosition(state.getPosition());
    }

    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("SpecimenState", this.state);
        telemetry.addData("SpecimenPosition", this.getPosition());
    }
}

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class SlideSubsystem {
    public SparkMiniMotor slide;
    public static double power = 1.0;
    public static int tolerance = 200;
//    private static final double encoderResolution = 537.689839572; // 5203 Series Yellow Jacket Planetary Gear Motor (19.2:1 Ratio, 24mm Length 8mm REX™ Shaft, 312 RPM, 3.3 - 5V Encoder)
//    private static final double inchesPerRotation = 4.724757455393701; // 2mm Pitch GT2 Hub-Mount Timing Belt Pulley (14mm Bore, 60 Tooth)

    public enum SlideState {
        INTAKE (158),
        BELOW_LOW_RUNG (186),
        ABOVE_LOW_RUNG (480),
        BELOW_HIGH_RUNG (1350),
        ABOVE_HIGH_RUNG (1933);

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
        DcMotorEx motor = (DcMotorEx) hardwareMap.get("frontLeft");
        CRServo power = (CRServo) hardwareMap.get("slide");
        this.slide = new SparkMiniMotor(motor.getController(), motor.getPortNumber());
        this.slide.setServo(power);

        this.slide.setDirection(DcMotor.Direction.REVERSE);
        this.state = SlideState.INTAKE;
    }

    public void setPower(double power) {
        this.slide.setPower(power);
    }

    public double getPower() {
        return this.slide.getPower();
    }

    public double getPosition() {
        return this.slide.getCurrentPosition();
    }

    public void setMode(DcMotor.RunMode mode) {
        this.slide.setMode(mode);
    }

    public void setState(SlideState state) {
        this.state = state;
    }

    public void increment() {
        switch (this.state) {
            case INTAKE:
            case BELOW_LOW_RUNG:
                this.state = SlideState.ABOVE_LOW_RUNG;
                break;
            default:
                this.state = SlideState.ABOVE_HIGH_RUNG;
                break;
        }
    }

    public void decrement() {
        switch (this.state) {
            case ABOVE_HIGH_RUNG:
                this.state = SlideState.BELOW_HIGH_RUNG;
                break;
            case ABOVE_LOW_RUNG:
                this.state = SlideState.BELOW_LOW_RUNG;
                break;
            default:
                this.state = SlideState.INTAKE;
                break;
        }
    }

    public SlideState getState() {
        return this.state;
    }

    public void init() {
        this.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void update() {
        double adjustedPower = power * Math.signum(this.getPosition() - this.state.getPosition());
        if (Math.abs(this.getPosition() - this.state.getPosition()) < tolerance)
            adjustedPower *= Math.abs(this.getPosition() - this.state.getPosition()) / tolerance;

        this.setPower(-adjustedPower);
    }

    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("SlideState", this.state);
        telemetry.addData("SlidePosition", this.getPosition());
        telemetry.addData("SlidePower", this.getPower());
    }
}

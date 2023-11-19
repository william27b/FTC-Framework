package org.firstinspires.ftc.teamcode.debug;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.debug.config.DrivingConfiguration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import org.firstinspires.ftc.teamcode.debug.config.*;

public class MecanumController {
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor leftRear;
    public DcMotor rightRear;

    public IMU imu;
    private double imuAngleOffset = 0;

    private SpeedController speedController;
    private double driveSpeed = 1;

    private double positionX = 0;
    private double positionY = 0;

    public void setMotorsRunMode(DcMotor.RunMode runMode) {
        leftFront.setMode(runMode);
        rightFront.setMode(runMode);
        leftRear.setMode(runMode);
        rightRear.setMode(runMode);
    }

    public void initIMU(HardwareMap hardwareMap) {
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logo = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usb = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logo, usb);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    private void init(HardwareMap hardwareMap) {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");

        initIMU(hardwareMap);

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.FORWARD);
    }

    public MecanumController(HardwareMap hardwareMap) {
        init(hardwareMap);
        this.speedController = new SpeedController.SpeedBuilder(SpeedType.NO_CHANGE)
                .build();
    }

    public MecanumController(HardwareMap hardwareMap, SpeedController speed) {
        init(hardwareMap);
        this.speedController = speed;
    }

    public void fakeReset() {
        this.positionX = 0;
        this.positionY = 0;
    }

    public void setDriveSpeed(double speed) {
        this.speedController.setSpeed(speed);
        this.driveSpeed = speed;
    }

    public void calibrateIMUAngleOffset() {
        imuAngleOffset = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public double getCalibratedIMUAngle() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + imuAngleOffset;
    }

    public void drive(Gamepad gamepad) {
        double x = DrivingConfiguration.getValue(gamepad, DrivingConfiguration.STRAFE_RIGHT);
        double y = DrivingConfiguration.getValue(gamepad, DrivingConfiguration.STRAFE_UP);
        double r = DrivingConfiguration.getValue(gamepad, DrivingConfiguration.ROTATE_RIGHT);

        double leftFrontPower = y + x + r;
        double rightFrontPower = y - x - r;
        double leftRearPower = y - x + r;
        double rightRearPower = y + x - r;

        double max = Math.max(Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)), Math.max(Math.abs(leftRearPower), Math.abs(rightRearPower)));

        speedController.updateSpeed(gamepad);
        driveSpeed = speedController.getSpeed();

        if (max < 1) {
            max = 1;
        }

        if (max * driveSpeed < Constants.MINIMUM_VOLTAGE_APPLIED) {
            leftFront.setPower(Constants.MINIMUM_VOLTAGE_APPLIED);
            rightFront.setPower(Constants.MINIMUM_VOLTAGE_APPLIED);
            leftRear.setPower(Constants.MINIMUM_VOLTAGE_APPLIED);
            rightRear.setPower(Constants.MINIMUM_VOLTAGE_APPLIED);
            return;
        }

        leftFront.setPower(leftFrontPower * driveSpeed / max);
        rightFront.setPower(rightFrontPower * driveSpeed / max);
        leftRear.setPower(leftRearPower * driveSpeed / max);
        rightRear.setPower(rightRearPower * driveSpeed / max);
    }

    public void fieldOrientedDrive(Gamepad gamepad) {
        double x = DrivingConfiguration.getValue(gamepad, DrivingConfiguration.STRAFE_RIGHT);
        double y = DrivingConfiguration.getValue(gamepad, DrivingConfiguration.STRAFE_UP);
        double r = DrivingConfiguration.getValue(gamepad, DrivingConfiguration.ROTATE_RIGHT);

        // cos * y = how much right if gamepad forward
        // cos * x = how much right if gamepad right
        // sin * y = how much forward if gamepad forward
        // sin * x = how much forward if gamepad right
        double xOriented = (Math.cos(getCalibratedIMUAngle()) * x) + (Math.sin(getCalibratedIMUAngle()) * y);
        double yOriented = (Math.cos(getCalibratedIMUAngle()) * y) - (Math.sin(getCalibratedIMUAngle()) * x);

        double leftFrontPower = yOriented + xOriented + r;
        double rightFrontPower = yOriented - xOriented - r;
        double leftRearPower = yOriented - xOriented + r;
        double rightRearPower = yOriented + xOriented - r;

        double max = Math.max(Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)), Math.max(Math.abs(leftRearPower), Math.abs(rightRearPower)));

        speedController.updateSpeed(gamepad);
        driveSpeed = speedController.getSpeed();

        if (max < 1) {
            max = 1;
        }

        if (max * driveSpeed < Constants.MINIMUM_VOLTAGE_APPLIED) {
            leftFront.setPower(Constants.MINIMUM_VOLTAGE_APPLIED);
            rightFront.setPower(Constants.MINIMUM_VOLTAGE_APPLIED);
            leftRear.setPower(Constants.MINIMUM_VOLTAGE_APPLIED);
            rightRear.setPower(Constants.MINIMUM_VOLTAGE_APPLIED);
            return;
        }

        leftFront.setPower(leftFrontPower * driveSpeed / max);
        rightFront.setPower(rightFrontPower * driveSpeed / max);
        leftRear.setPower(leftRearPower * driveSpeed / max);
        rightRear.setPower(rightRearPower * driveSpeed / max);
    }

    public boolean isBusy() {
        return leftFront.isBusy() || rightFront.isBusy() || leftRear.isBusy() || rightRear.isBusy();
    }

    public void waitUntilCompletion() {
        while (isBusy()) {}
    }

    public void deactivate() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
    }

    public void rotateToRadian(double targetRadian, double radianTolerance) {
        setMotorsRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        targetRadian = Constants.setToDomain(targetRadian, 0, 2 * Math.PI);
        double currentRadian = Constants.setToDomain(getCalibratedIMUAngle(), 0, 2 * Math.PI);
        while (Math.abs(currentRadian - targetRadian) > radianTolerance) {
            currentRadian = Constants.setToDomain(getCalibratedIMUAngle(), 0, 2 * Math.PI);
            leftFront.setPower(driveSpeed);
            rightFront.setPower(-driveSpeed);
            leftRear.setPower(driveSpeed);
            rightRear.setPower(-driveSpeed);
        }
    }

    public void moveInches(double leftFrontInches, double rightFrontInches, double leftRearInches, double rightRearInches, boolean wait) {
        setMotorsRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        double forward = (leftFrontInches + rightFrontInches) / 2;
        double right = leftFrontInches - forward;

        double leftFrontEncoderTicks = forward * Constants.WHEEL_ENCODER_TICKS_PER_INCH_FORWARD + right * Constants.WHEEL_ENCODER_TICKS_PER_INCH_SIDEWAYS;
        double rightFrontEncoderTicks = forward * Constants.WHEEL_ENCODER_TICKS_PER_INCH_FORWARD - right * Constants.WHEEL_ENCODER_TICKS_PER_INCH_SIDEWAYS;
        double leftRearEncoderTicks = forward * Constants.WHEEL_ENCODER_TICKS_PER_INCH_FORWARD - right * Constants.WHEEL_ENCODER_TICKS_PER_INCH_SIDEWAYS;
        double rightRearEncoderTicks = forward * Constants.WHEEL_ENCODER_TICKS_PER_INCH_FORWARD + right * Constants.WHEEL_ENCODER_TICKS_PER_INCH_SIDEWAYS;

        double max = Math.max(Math.max(Math.abs(leftFrontInches), Math.abs(rightFrontInches)), Math.max(Math.abs(leftRearInches), Math.abs(rightRearInches)));

        leftFront.setTargetPosition((int) leftFrontEncoderTicks);
        rightFront.setTargetPosition((int) rightFrontEncoderTicks);
        leftRear.setTargetPosition((int) leftRearEncoderTicks);
        rightRear.setTargetPosition((int) rightRearEncoderTicks);

        setMotorsRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(driveSpeed * leftFrontInches / max);
        rightFront.setPower(driveSpeed * rightFrontInches / max);
        leftRear.setPower(driveSpeed * leftRearInches / max);
        rightRear.setPower(driveSpeed * rightRearInches / max);

        if (wait) {
            waitUntilCompletion();
            deactivate();
        }
    }

    public void moveToPosition(double inchesX, double inchesY, boolean wait) {
        double x = inchesX - positionX;
        double y = inchesY - positionY;

        double xOriented = x * Math.cos(getCalibratedIMUAngle()) - y * Math.sin(getCalibratedIMUAngle());
        double yOriented = x * Math.sin(getCalibratedIMUAngle()) + y * Math.cos(getCalibratedIMUAngle());

        double leftFrontInches = yOriented + xOriented;
        double rightFrontInches = yOriented - xOriented;
        double leftRearInches = yOriented - xOriented;
        double rightRearInches = yOriented + xOriented;

        moveInches(leftFrontInches, rightFrontInches, leftRearInches, rightRearInches, wait);

        positionX = inchesX;
        positionY = inchesY;
    }
}
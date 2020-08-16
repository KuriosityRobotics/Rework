package org.firstinspires.ftc.teamcode.rework.Robot.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.rework.Robot.Robot;


public class DrivetrainModule implements Module {
    private Robot robot;

    // States
    private double yMovement;
    private double mecanumMovement;
    private double turnMovement;
    private boolean isSlowMode;

    // Constants
    private final static double POWER_SCALE_FACTOR = 0.6;
    private final static double MECANUM_POWER_SCALE_FACTOR = 1.4;
    private final static double SLOW_POWER_SCALE_FACTOR = 0.3;

    // Motors
    private DcMotor fLeft;
    private DcMotor fRight;
    private DcMotor bLeft;
    private DcMotor bRight;

    public DrivetrainModule(Robot robot) {
        this.robot = robot;
    }

    public void init() {
        fLeft = robot.getDcMotor("fLeft");
        fRight = robot.getDcMotor("fRight");
        bLeft = robot.getDcMotor("bLeft");
        bRight = robot.getDcMotor("bRight");

        setDrivetrainDirection();
        setDrivetrainZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Drives robot (moves drivetrain) using states.
     */
    public synchronized void update() {
        drive();
    }

    /**
     * Sets the states of this module. Use update() to execute.
     *
     * @param yMovement Forward/backward movement of robot
     * @param mecanumMovement Sideways movement of robot
     * @param turnMovement Turn movement of robot
     */
    public synchronized void setStates(double yMovement, double mecanumMovement, double turnMovement) {
        this.yMovement = yMovement;
        this.mecanumMovement = mecanumMovement;
        this.turnMovement = turnMovement;
    }

    public void slowMode(boolean slowMode) {
        this.isSlowMode = slowMode;
    }

    /**
     * Calculates and applies powers to the drivetrain motors using the states (movement params)
     * in the module.
     */
    private void drive() {
        double fLPower = ((yMovement) - turnMovement - mecanumMovement * MECANUM_POWER_SCALE_FACTOR);
        double fRPower = ((yMovement) + turnMovement + mecanumMovement * MECANUM_POWER_SCALE_FACTOR);
        double bLPower = ((yMovement) - turnMovement + mecanumMovement * MECANUM_POWER_SCALE_FACTOR);
        double bRPower = ((yMovement) + turnMovement - mecanumMovement * MECANUM_POWER_SCALE_FACTOR);

        double maxPower = Math.abs(fLPower);
        if (Math.abs(fRPower) > maxPower) {
            maxPower = Math.abs(fRPower);
        }
        if (Math.abs(bLPower) > maxPower) {
            maxPower = Math.abs(bLPower);
        }
        if (Math.abs(bRPower) > maxPower) {
            maxPower = Math.abs(bRPower);
        }
        double scaleDown = 1.0;
        if (maxPower > 1.0) {
            scaleDown = 1.0 / maxPower;
        }
        fLPower *= scaleDown;
        fRPower *= scaleDown;
        bLPower *= scaleDown;
        bRPower *= scaleDown;

        fLPower *= POWER_SCALE_FACTOR;
        fRPower *= POWER_SCALE_FACTOR;
        bLPower *= POWER_SCALE_FACTOR;
        bRPower *= POWER_SCALE_FACTOR;

        if (isSlowMode) {
            fLPower *= SLOW_POWER_SCALE_FACTOR;
            fRPower *= SLOW_POWER_SCALE_FACTOR;
            bLPower *= SLOW_POWER_SCALE_FACTOR;
            bRPower *= SLOW_POWER_SCALE_FACTOR;
        }

        setMotorPowers(fLPower, fRPower, bLPower, bRPower);
    }

    private void setMotorPowers(double fLPower, double fRPower, double bLPower, double bRPower) {
        fLeft.setPower(fLPower);
        fRight.setPower(fRPower);
        bLeft.setPower(bLPower);
        bRight.setPower(bRPower);
    }

    private void setDrivetrainZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        fLeft.setZeroPowerBehavior(zeroPowerBehavior);
        fRight.setZeroPowerBehavior(zeroPowerBehavior);
        bLeft.setZeroPowerBehavior(zeroPowerBehavior);
        bRight.setZeroPowerBehavior(zeroPowerBehavior);
    }

    private void setDrivetrainDirection() {
        fLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        fRight.setDirection(DcMotorSimple.Direction.FORWARD);
        bLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        bRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}
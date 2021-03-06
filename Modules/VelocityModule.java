package org.firstinspires.ftc.teamcode.rework.Modules;

import android.os.SystemClock;

import org.firstinspires.ftc.teamcode.rework.ModuleTools.Module;
import org.firstinspires.ftc.teamcode.rework.Robot;

public class VelocityModule implements Module {
    private boolean isOn;

    // in/s and rad/s
    public double xVel;
    public double yVel;
    public double angleVel;

    private long oldUpdateTime;
    private long currentUpdateTime;

    private double oldWorldX = 0;
    private double oldWorldY = 0;
    private double oldWorldAngle = 0;


    private Robot robot;

    public VelocityModule(Robot robot, boolean isOn) {
        this.robot = robot;
        this.isOn = isOn;
    }

    public void init() {
        oldUpdateTime = SystemClock.elapsedRealtime();
    }

    public void update() {
        currentUpdateTime = robot.currentTimeMilli;

        xVel = 1000 * (robot.odometryModule.worldX - oldWorldX) / (currentUpdateTime - oldUpdateTime);
        yVel = 1000 * (robot.odometryModule.worldY - oldWorldY) / (currentUpdateTime - oldUpdateTime);
        angleVel = 1000 * (robot.odometryModule.worldAngleRad - oldWorldAngle) / (currentUpdateTime - oldUpdateTime);

        oldWorldX = robot.odometryModule.worldX;
        oldWorldY = robot.odometryModule.worldY;
        oldWorldAngle = robot.odometryModule.worldAngleRad;
        oldUpdateTime = currentUpdateTime;
    }

    public void telemetry() {
        robot.telemetryDump.addHeader("---VELOCITY---");
        robot.telemetryDump.addData("xVel: ", xVel);
        robot.telemetryDump.addData("yVel: ", yVel);
        robot.telemetryDump.addData("angleVel: ", angleVel);
    }

    public void fileDump(){

    }

    public boolean isOn(){
        return isOn;
    }
}

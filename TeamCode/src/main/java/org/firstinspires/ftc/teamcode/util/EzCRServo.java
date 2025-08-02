package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.CRServo;

// CR-servo helper: ramps speed up/down. Call update() every loop.
public class EzCRServo {

    private final CRServo servo;
    private double targetPct = 0;   // wanted speed (-100...100)
    private double curPct    = 0;   // current output

    private static final double ACCEL = 10;  // % per loop while speeding up
    private static final double DECEL = 20;  // % per loop while slowing down

    public EzCRServo(CRServo s) { servo = s; }

    public void setPercent(double pct) { targetPct = Math.max(-100, Math.min(100, pct)); }
    public void stop()                 { targetPct = 0; }

    public void update() {
        double diff = targetPct - curPct;
        double step = diff > 0 ? ACCEL : DECEL;
        curPct += Math.signum(diff) * Math.min(step, Math.abs(diff));
        servo.setPower(curPct / 100.0);
    }
}
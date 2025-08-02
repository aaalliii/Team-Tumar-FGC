package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class EzServo {
    private final Servo servo;          // use Servo interface for both
    private final ElapsedTime timer = new ElapsedTime();
    private boolean isCR; // continues rotation

    public EzServo(Servo servo, boolean isContinuousRotation) {
        this.servo = servo;
        this.isCR = isContinuousRotation;
    }

    //0°-180° → 0.0-1.0
    public void goToDeg(double deg) {
        servo.setPosition(deg / 180.0);
    }

    public void nudge(double deltaDeg) {
        goToDeg(getDeg() + deltaDeg);
    }

    public double getDeg() { return servo.getPosition() * 180.0; }

    // continues servo - spin <percent> (-100..100) for <seconds>
    public void spinFor(double percent, double seconds) {
        if (!isCR) return;
        ((CRServo) servo).setPower(percent / 100.0);
        timer.reset();
        while (timer.seconds() < seconds) { /* wait */ }
        ((CRServo) servo).setPower(0);
    }
}
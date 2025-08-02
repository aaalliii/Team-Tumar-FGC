package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class EzMotor {

    private final DcMotor motor;
    @SuppressWarnings("unused") private final ElapsedTime timer = new ElapsedTime();

    public EzMotor(DcMotor motor)           { this.motor = motor; }

    /* percent power */
    public void setPercent(double pct)      { motor.setPower(pct / 100.0); }
    public void reverse()                   { motor.setPower(-motor.getPower()); }
    public void stop()                      { motor.setPower(0); }

    /* zero-power behaviour */
    public void brakeOn()                   { motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); }

    /* encoders */
    public void resetEncoder() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void runToPosition(int ticks,double pct){
        motor.setTargetPosition(ticks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPercent(pct);
    }
    public boolean isBusy()                   { return motor.isBusy(); }
}
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.*;

@TeleOp(name = "Tumar FGC")
public class motor extends OpMode {

    /** Subsystems **/
    private EzMotor tl, bl, tr, br;
    private EzMotor hang, hang1;
    private IntakeController intake;
    private EzCRServo accel;

    /** Precision drive **/
    private boolean precision = false, lastB = false;
    private static final double[] SCALE = {0.20, 0.40, 0.60};
    private int scaleIdx = 2;
    private double scaleVal = SCALE[scaleIdx];
    private boolean lastL, lastR;

    /** D-pad DOWN state **/
    private boolean lastDown = false;

    private static final double SHOOT_PCT = 100;

    @Override public void init() {

        hang  = new EzMotor(hardwareMap.get(DcMotor.class, "hang"));
        hang1 = new EzMotor(hardwareMap.get(DcMotor.class, "hang1"));

        intake = new IntakeController(
                new EzMotor(hardwareMap.get(DcMotor.class, "intakeLeft")),
                new EzMotor(hardwareMap.get(DcMotor.class, "intakeRight"))
        );

        accel = new EzCRServo(hardwareMap.get(CRServo.class, "accel"));

        tl = new EzMotor(hardwareMap.get(DcMotor.class, "topLeft"));
        bl = new EzMotor(hardwareMap.get(DcMotor.class, "bottomLeft"));
        tr = new EzMotor(hardwareMap.get(DcMotor.class, "topRight"));
        br = new EzMotor(hardwareMap.get(DcMotor.class, "bottomRight"));
    }

    @Override public void loop() {

        /* Precision toggle (B) */
        if (gamepad1.b && !lastB) precision = !precision;
        lastB = gamepad1.b;

        /* Precision scale adjust (D-pad L/R) */
        if (gamepad1.dpad_left  && !lastL && scaleIdx > 0)              { scaleIdx--; scaleVal = SCALE[scaleIdx]; }
        if (gamepad1.dpad_right && !lastR && scaleIdx < SCALE.length-1) { scaleIdx++; scaleVal = SCALE[scaleIdx]; }
        lastL = gamepad1.dpad_left; lastR = gamepad1.dpad_right;

        /* Lift (L1/L2) */
        if (gamepad1.left_bumper) {
            hang.setPercent(-100); hang1.setPercent(100);
        } else if (gamepad1.left_trigger > 0.2) {
            hang.setPercent(100);  hang1.setPercent(-100);
        } else {
            hang.stop(); hang1.stop();
        }

        /* Intake close / zero (D-pad DOWN) */
        if (gamepad1.dpad_down) {
            intake.driveClosedOpenLoop(40);        // push closed at 40%
        } else if (lastDown) {
            intake.zero();                         // set new zero on release
        }
        lastDown = gamepad1.dpad_down;

        intake.update(gamepad1.right_bumper,
                gamepad1.right_trigger > 0.2);

        /* Acceleration (Y) */
        if (gamepad1.y) accel.setPercent(SHOOT_PCT);
        else            accel.stop();
        accel.update();

        /* Drive */
        drive(gamepad1.right_stick_x, gamepad1.left_stick_y);

        telemetry.addData("Precision", precision ? (int)(scaleVal*100)+"%" : "OFF");
    }

    private void drive(double fb, double turn) {
        double s = precision ? scaleVal : 1.0;
        double pTL = (fb - turn) * s, pBL = (fb - turn) * s,
                pTR = (fb + turn) * s, pBR = (fb + turn) * s;
        tl.setPercent(pTL*100); bl.setPercent(pBL*100);
        tr.setPercent(pTR*100); br.setPercent(pBR*100);
    }
}
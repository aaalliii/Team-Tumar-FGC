package org.firstinspires.ftc.teamcode.util;

// Intake: r1 toggles 0 - 120deg, r2 toggles 0 - 180deg
public class IntakeController {

    private static final int MID  =  96;   // 120deg
    private static final int FULL = 144;   // 180deg
    private static final double MOVE = 40;
    private static final double HOLD = 15;

    private enum Pos { CLOSE, MID, FULL }
    private Pos pos = Pos.CLOSE;

    private boolean lastR1, lastR2;
    private final EzMotor left, right;   // left counts negative, right positive

    public IntakeController(EzMotor l, EzMotor r) {
        left = l; right = r;
        left.resetEncoder();  right.resetEncoder();
        left.brakeOn();       right.brakeOn();
    }

    /** r1 = 0-120  |  r2 = 0-180. Call every loop. */
    public void update(boolean r1, boolean r2) {
        if (r1 && !lastR1) pos = (pos == Pos.MID)  ? Pos.CLOSE : Pos.MID;
        if (r2 && !lastR2) pos = (pos == Pos.FULL) ? Pos.CLOSE : Pos.FULL;
        lastR1 = r1; lastR2 = r2;

        int tgt = (pos == Pos.MID) ? MID : (pos == Pos.FULL ? FULL : 0);
        left.runToPosition( -tgt, MOVE );
        right.runToPosition(  tgt, MOVE );

        if (!left.isBusy() && !right.isBusy()) {
            double p = (pos == Pos.CLOSE) ? 0 : HOLD;
            left.setPercent(p); right.setPercent(p);
        }
    }

    /** Open-loop power toward the closed stop; ignores encoders. */
    public void driveClosedOpenLoop(double pct) {
        left .setPercent(-pct);   // sign per robot wiring
        right.setPercent( pct);
    }

    /** Stop motors, zero encoders, mark CLOSED. */
    public void zero() {
        left .setPercent(0); right.setPercent(0);
        left .resetEncoder(); right.resetEncoder();
        pos = Pos.CLOSE;
    }
}
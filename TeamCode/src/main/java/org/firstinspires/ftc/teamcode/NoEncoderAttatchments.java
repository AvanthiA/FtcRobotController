package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.lang.Math;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
//import java.lang.Float;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "NO ENCODER ATTACHMENTS", group = "")
public class NoEncoderAttatchments extends LinearOpMode {

    public DcMotor Right_Front_Wheel;
    public DcMotor Left_Front_Wheel;
    public DcMotor Right_Rear_Wheel;
    public DcMotor Left_Rear_Wheel;
    public DcMotor elbow;
    public DcMotor arm;
    public DcMotor base;
    public CRServo picker;
    public DcMotor carousel;


    //private Robot robot;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {


        Left_Rear_Wheel = hardwareMap.get(DcMotor.class, "Left_back");
        Left_Front_Wheel = hardwareMap.get(DcMotor.class, "Left_front");
        Right_Rear_Wheel = hardwareMap.get(DcMotor.class, "Right_back");
        Right_Front_Wheel = hardwareMap.get(DcMotor.class, "Right_front");
        elbow = hardwareMap.get(DcMotor.class, "elbow");
        arm = hardwareMap.get(DcMotor.class, "arm");
        base = hardwareMap.get(DcMotor.class, "base");
        picker = hardwareMap.get(CRServo.class, "picker");
        carousel = hardwareMap.get(DcMotor.class, "carousel");

        Left_Front_Wheel.setDirection(DcMotorSimple.Direction.REVERSE);
        Left_Rear_Wheel.setDirection(DcMotorSimple.Direction.REVERSE);


        /*robot = new Robot();
        robot.Init(hardwareMap, telemetry, false);
         */

        waitForStart();

/*        BaseController baseController = new BaseController(blocker);
        Thread baseControllerThread = new Thread(baseController);
        baseControllerThread.start();*/

        // baseController.stop();

        if (opModeIsActive()) {

            while (opModeIsActive()) {

                // Elbow
                if (gamepad2.dpad_down) {
                    elbow.setPower(-0.4);
                } else if (gamepad2.dpad_up) {
                    elbow.setPower(0.4);
                } else {
                    elbow.setPower(0);
                }
                // Base
                if (gamepad2.dpad_left) {
                    base.setPower(-0.3);
                } else if (gamepad2.dpad_right) {
                    base.setPower(0.3);
                } else {
                    base.setPower(0);
                }
                // Arm
                if (gamepad2.x) {
                    arm.setPower(-0.5);
                } else if (gamepad2.b) {
                    arm.setPower(0.5);
                } else {
                    arm.setPower(0);
                }
                // Picker
                if (gamepad2.a) {
                    picker.setPower(1);
                } else if (gamepad2.y) {
                    picker.setPower(-1);
                } else {
                    picker.setPower(0);
                }
                // Carousel
                if (gamepad2.left_bumper) {
                    carousel.setPower(0.5);
                } else if (gamepad2.right_bumper) {
                    carousel.setPower(-0.5);
                } else {
                    carousel.setPower(0);
                }

                // Mecanum drive is controlled with three axes: drive (front-and-back),
                // strafe (left-and-right), and twist (rotating the whole chassis).
                double drive  = gamepad1.left_stick_y * -1;
                double strafe = gamepad1.left_stick_x;
                double twist  = gamepad1.right_stick_x;

                /*
                 * If we had a gyro and wanted to do field-oriented control, here
                 * is where we would implement it.
                 *
                 * The idea is fairly simple; we have a robot-oriented Cartesian (x,y)
                 * coordinate (strafe, drive), and we just rotate it by the gyro
                 * reading minus the offset that we read in the init() method.
                 * Some rough pseudocode demonstrating:
                 *
                 * if Field Oriented Control:
                 *     get gyro heading
                 *     subtract initial offset from heading
                 *     convert heading to radians (if necessary)
                 *     new strafe = strafe * cos(heading) - drive * sin(heading)
                 *     new drive  = strafe * sin(heading) + drive * cos(heading)
                 *
                 * If you want more understanding on where these rotation formulas come
                 * from, refer to
                 * https://en.wikipedia.org/wiki/Rotation_(mathematics)#Two_dimensions
                 */

                // You may need to multiply some of these by -1 to invert direction of
                // the motor.  This is not an issue with the calculations themselves.
                double[] speeds = {
                        (drive + strafe + twist),
                        (drive - strafe - twist),
                        (drive - strafe + twist),
                        (drive + strafe - twist)
                };

                // Because we are adding vectors and motors only take values between
                // [-1,1] we may need to normalize them.

                // Loop through all values in the speeds[] array and find the greatest
                // *magnitude*.  Not the greatest velocity.
                double max = Math.abs(speeds[0]);
                for(int i = 0; i < speeds.length; i++) {
                    if ( max < Math.abs(speeds[i]) ) max = Math.abs(speeds[i]);
                }

                // If and only if the maximum is outside of the range we want it to be,
                // normalize all the other speeds based on the given speed value.
                if (max > 1) {
                    for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
                }

                // apply the calculated values to the motors.
                Left_Front_Wheel.setPower(speeds[0]);
                Right_Front_Wheel.setPower(speeds[1]);
                Left_Rear_Wheel.setPower(speeds[2]);
                Right_Rear_Wheel.setPower(speeds[3]);
            }
        }

        telemetry.update();
    }

    // all of our base movement functions
    private void StopBase() {
        Left_Front_Wheel.setPower(0);
        Right_Front_Wheel.setPower(0);
        Left_Rear_Wheel.setPower(0);
        Right_Rear_Wheel.setPower(0);
    }


}
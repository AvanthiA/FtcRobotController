package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@Disabled
@Autonomous(name = "slide1Inch")
public class slide1Inch extends LinearOpMode {

    public DcMotor Right_Front_Wheel;
    public DcMotor Left_Front_Wheel;
    public DcMotor Right_Rear_Wheel;
    public DcMotor Left_Rear_Wheel;
    public DcMotor elbow;
    public DcMotor arm;
    public DcMotor base;
    public CRServo picker;
    public DcMotor carousel;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        int elbowTargetPosition;
        int armTargetPosition;

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

        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            Left_Front_Wheel.setPower(-0.2);
            Right_Front_Wheel.setPower(0.2);
            Left_Rear_Wheel.setPower(0.2);
            Right_Rear_Wheel.setPower(-0.2);
            sleep(200);
            Left_Front_Wheel.setPower(0);
            Right_Front_Wheel.setPower(0);
            Left_Rear_Wheel.setPower(0);
            Right_Rear_Wheel.setPower(0);
            telemetry.update();
        }
    }
}
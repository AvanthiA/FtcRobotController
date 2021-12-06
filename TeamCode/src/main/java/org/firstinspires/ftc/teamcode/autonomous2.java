package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@Autonomous(name = "autonomous2")
public class autonomous2 extends LinearOpMode {

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
            carousel.setPower(-0.5);
            sleep(2000);
        }
    }
}
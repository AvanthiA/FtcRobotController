package org.firstinspires.ftc.teamcode.Odometry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.Odometry.OdometryGlobalCoordinatePosition;

/**
 * Created by Sarthak on 6/1/2019.
 * Example OpMode that runs the GlobalCoordinatePosition thread and accesses the (x, y, theta) coordinate values
 */
@TeleOp(name = "kabob", group = "Calibration")
public class Autonomous extends LinearOpMode {
    //globalPositionUpdate.reverseRightEncoder();
    //globalPositionUpdate.reverseLeftEncoder();
    //globalPositionUpdate.reverseNormalEncoder();
    //Odometry encoder wheels
    DcMotor verticalRight, verticalLeft, horizontal;

    //The amount of encoder ticks for each inch the robot moves. This will change for each robot and needs to be changed here
    final double COUNTS_PER_INCH = 307.699557;

    //Hardware map names for the encoder wheels. Again, these will change for each robot and need to be updated below
    String rfName = "Right_front", rbName = "Right_back", lfName = "Left_front", lbName = "Left_back";
    String verticalLeftEncoderName = lfName, verticalRightEncoderName = rfName, horizontalEncoderName = rbName;

    public DcMotor Right_Front_Wheel;
    public DcMotor Left_Front_Wheel;
    public DcMotor Right_Rear_Wheel;
    public DcMotor Left_Rear_Wheel;
    double yMulitplier = 0.7;
    @Override
    public void runOpMode() throws InterruptedException {
        // globalPositionUpdate.reverseRightEncoder();
        // globalPositionUpdate.reverseLeftEncoder();
        //globalPositionUpdate.reverseNormalEncoder();
        //Assign the hardware map to the odometry wheels
        verticalLeft = hardwareMap.dcMotor.get(verticalLeftEncoderName);
        verticalRight = hardwareMap.dcMotor.get(verticalRightEncoderName);
        horizontal = hardwareMap.dcMotor.get(horizontalEncoderName);
        horizontal.setDirection(DcMotor.Direction.REVERSE);

        Left_Rear_Wheel = hardwareMap.get(DcMotor.class, "Left_back");
        Left_Front_Wheel = hardwareMap.get(DcMotor.class, "Left_front");
        Right_Rear_Wheel = hardwareMap.get(DcMotor.class, "Right_back");
        Right_Front_Wheel = hardwareMap.get(DcMotor.class, "Right_front");
        Left_Front_Wheel.setDirection(DcMotor.Direction.REVERSE);
        Left_Rear_Wheel.setDirection(DcMotor.Direction.REVERSE);




        //Reset the encoders
        verticalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        /*
        Reverse the direction of the odometry wheels. THIS WILL CHANGE FOR EACH ROBOT. Adjust the direction (as needed) of each encoder wheel
        such that when the verticalLeft and verticalRight encoders spin forward, they return positive values, and when the
        horizontal encoder travels to the right, it returns positive value
        */
        // verticalLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        // verticalRight.setDirection(DcMotorSimple.Direction.REVERSE);
        // horizontal.setDirection(DcMotorSimple.Direction.REVERSE);

        //Set the mode of the odometry encoders to RUN_WITHOUT_ENCODER
        verticalRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Init complete
        telemetry.addData("Status", "Init Complete");
        telemetry.update();
        waitForStart();

        /**
         * *****************
         * OpMode Begins Here
         * *****************
         */

        //Create and start GlobalCoordinatePosition thread to constantly update the global coordinate positions\
        OdometryGlobalCoordinatePosition globalPositionUpdate = new OdometryGlobalCoordinatePosition(
                verticalLeft, verticalRight, horizontal, COUNTS_PER_INCH, 75);
        Thread positionThread = new Thread(globalPositionUpdate);
        positionThread.start();

        // globalPositionUpdate.reverseRightEncoder();
        // globalPositionUpdate.reverseLeftEncoder();
        // globalPositionUpdate.reverseNormalEncoder();

        while(opModeIsActive()){
            if (gamepad1.dpad_up){
                verticalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                verticalRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                while((globalPositionUpdate.returnYCoordinate() / COUNTS_PER_INCH) > -12){
                    forward(0.3);
                }
            }
            forward(gamepad1.left_stick_y);

            stopBase();
            //Display Global (x, y, theta) coordinates
            telemetry.addData("X Position", globalPositionUpdate.returnXCoordinate() / COUNTS_PER_INCH);
            telemetry.addData("Y Position", globalPositionUpdate.returnYCoordinate() / COUNTS_PER_INCH);
            telemetry.addData("Y Position with multiplier", globalPositionUpdate.returnYCoordinate() * yMulitplier / COUNTS_PER_INCH);
            telemetry.addData("Orientation (Degrees)", globalPositionUpdate.returnOrientation());

            telemetry.addData("Vertical left encoder position", globalPositionUpdate.verticalLeftEncoderWheelPosition);
            telemetry.addData("Vertical right encoder position", globalPositionUpdate.verticalRightEncoderWheelPosition);
            telemetry.addData("Horizontal encoder position", globalPositionUpdate.normalEncoderWheelPosition);

            telemetry.addData("Thread Active", positionThread.isAlive());
            telemetry.update();
        }

        //Stop the thread
        globalPositionUpdate.stop();
    }
    private void forward(double power) {
        Left_Front_Wheel.setPower(power);
        Right_Front_Wheel.setPower(power);
        Left_Rear_Wheel.setPower(power);
        Right_Rear_Wheel.setPower(-power);
    }
    private void stopBase() {
        Left_Front_Wheel.setPower(-0.1);
        Right_Front_Wheel.setPower(-0.1);
        Left_Rear_Wheel.setPower(-0.1);
        Right_Rear_Wheel.setPower(-0.1);
        Left_Front_Wheel.setPower(0);
        Right_Front_Wheel.setPower(0);
        Left_Rear_Wheel.setPower(0);
        Right_Rear_Wheel.setPower(0);
    }
}
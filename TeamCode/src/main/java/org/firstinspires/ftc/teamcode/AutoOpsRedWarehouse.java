package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;

import java.util.List;



/*
 * This is an example of a more complex path to really test the tuning.
 */

@Autonomous(group = "drive")
public class AutoOpsRedWarehouse extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    private static final String VUFORIA_KEY =
            "AfNKhrX/////AAABmUBEXb6zGEKxsiFzrqjzR/lwcHlaAe4Kzi2jQqpWPYBIMequ0Kafs1C+YNhx3L5m2YV+qXgZQu/ZKxuV1qYCO6Pov3RCWbHaXVrWfqwJBoO5zLUYpOXM/AuivZd97OY0R1fdGvGWdzBe+5M34jqdSPSy3iV74mbUjaqia4qYoRFFikMY5uipMzuoMdRPMf/vPSFujM5QxnLdb7MjmKFyr0hoRKnEOpQBIV8j1o1tQdhU+KzuBQ5pmnCf0VHJ5Y3mRZk+oFIQRZ/Tp8oWlwphDSQSWjyppTdlkIVfq2b6p49Y0A6/OkD4OONSPA2GNHDk1K/aad9oS7BFPDbGNh1tYBmFQmEyBts4qM84IGR3WzqW";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    public DcMotor Right_Front_Wheel;
    public DcMotor Left_Front_Wheel;
    public DcMotor Right_Rear_Wheel;
    public DcMotor Left_Rear_Wheel;
    public DcMotor elbow;
    public DcMotor arm;
    public DcMotor base;
    public CRServo picker;
    public DcMotor carousel;

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
    private void first(){

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        if (isStopRequested()) return;

        //forward so doesnt hit anything
        Trajectory forward = drive.trajectoryBuilder(new Pose2d())
                .forward(7)
                .build();
        drive.followTrajectory(forward);
        forward.end();

        //strafe right a little so doesn't hit wall while turning later on
        Trajectory strafeRight = drive.trajectoryBuilder(forward.end())
                .strafeRight(19)
                .build();
        drive.followTrajectory(strafeRight);
        strafeRight.end();

        //move forward so parallel to shipping hub
        forward = drive.trajectoryBuilder(strafeRight.end())
                .forward(16)
                .build();
        drive.followTrajectory(forward);


        //turn 90 degrees so robot is facing backwards (battery to the wall and arm is pointing to shipping hub)
        drive.turn(Math.toRadians(95));

        //arm, elbow, picker init
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


        //arm tilts upward
        elbow.setTargetPosition(875);
        while (elbow.getCurrentPosition() < 875 && opModeIsActive()) {
            elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elbow.setPower(0.5);
            telemetry.update();
        }
        elbow.setPower(0);

        // extend arm
        arm.setTargetPosition(1100);
        while (arm.getCurrentPosition() < 1050 && opModeIsActive()) {
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(1);
            telemetry.addData("current position", arm.getCurrentPosition());
            telemetry.update();
        }
        arm.setPower(0);

        //running the picker to drop the cube
        int x=0;
        while (x<50000 && opModeIsActive()){
            picker.setPower(1);
            x++;
            telemetry.addData("x value: ", x);
            telemetry.update();
        }
        picker.setPower(0);

        //arm retracts
        arm.setTargetPosition(0);
        while (arm.getCurrentPosition() > 50 && opModeIsActive()) {
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(-1);
            telemetry.addData("current position", arm.getCurrentPosition());
            telemetry.update();
        }
        arm.setPower(0);


        //elbow down
        elbow.setTargetPosition(0);
        while (elbow.getCurrentPosition() > 0 && opModeIsActive()) {
            elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elbow.setPower(-0.5);
            telemetry.update();
        }
        elbow.setPower(0);

        //robot turns 90 degrees so previous 90 turn is cancelled
        drive.turn(Math.toRadians(-95));

        //strafe Left so robot touches wall again
        Trajectory strafeLeft = drive.trajectoryBuilder(strafeRight.end())
                .strafeLeft(20.5)
                .build();

        drive.followTrajectory(strafeLeft);

        Left_Front_Wheel.setPower(-0.25);
        Right_Front_Wheel.setPower(0.25);
        Left_Rear_Wheel.setPower(0.25);
        Right_Rear_Wheel.setPower(-0.25);
        sleep(1000);
        Left_Front_Wheel.setPower(0);
        Right_Front_Wheel.setPower(0);
        Left_Rear_Wheel.setPower(0);
        Right_Rear_Wheel.setPower(0);


        //move back 45 inches so in warehouse
        Trajectory backward = drive.trajectoryBuilder(strafeLeft.end())
                .back(45)
                .build();
        drive.followTrajectory(backward);
        backward.end();

        //strafe right a little to protect against potentially getting rammed by the other team
        strafeRight = drive.trajectoryBuilder(backward.end())
                .strafeRight(20)
                .build();
        drive.followTrajectory(strafeRight);
        strafeRight.end();

    }

    private void second(){

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        if (isStopRequested()) return;

        //forward so doesnt hit anything
        Trajectory forward = drive.trajectoryBuilder(new Pose2d())
                .forward(7)
                .build();
        drive.followTrajectory(forward);
        forward.end();

        //strafe right a little so doesn't hit wall while turning later on
        Trajectory strafeRight = drive.trajectoryBuilder(forward.end())
                .strafeRight(13.5)
                .build();
        drive.followTrajectory(strafeRight);
        strafeRight.end();

        //move forward so parallel to shipping hub
        forward = drive.trajectoryBuilder(strafeRight.end())
                .forward(16)
                .build();
        drive.followTrajectory(forward);


        //turn 90 degrees so robot is facing backwards (battery to the wall and arm is pointing to shipping hub)
        drive.turn(Math.toRadians(95));

        //arm, elbow, picker init
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


        //arm tilts upward
        elbow.setTargetPosition(500);
        while (elbow.getCurrentPosition() < 500 && opModeIsActive()) {
            elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elbow.setPower(0.5);
            telemetry.update();
        }
        elbow.setPower(0);

        // extend arm
        arm.setTargetPosition(1100);
        while (arm.getCurrentPosition() < 1050 && opModeIsActive()) {
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(1);
            telemetry.addData("current position", arm.getCurrentPosition());
            telemetry.update();
        }
        arm.setPower(0);

        //running the picker to drop the cube
        int x=0;
        while (x<50000 && opModeIsActive()){
            picker.setPower(1);
            x++;
            telemetry.addData("x value: ", x);
            telemetry.update();
        }
        picker.setPower(0);

        //arm retracts
        arm.setTargetPosition(0);
        while (arm.getCurrentPosition() > 50 && opModeIsActive()) {
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(-1);
            telemetry.addData("current position", arm.getCurrentPosition());
            telemetry.update();
        }
        arm.setPower(0);


        //elbow down
        elbow.setTargetPosition(0);
        while (elbow.getCurrentPosition() > 0 && opModeIsActive()) {
            elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elbow.setPower(-0.5);
            telemetry.update();
        }
        elbow.setPower(0);

        //robot turns 90 degrees so previous 90 turn is cancelled
        drive.turn(Math.toRadians(-95));

        //strafe Left so robot touches wall again
        Trajectory strafeLeft = drive.trajectoryBuilder(strafeRight.end())
                .strafeLeft(15)
                .build();

        drive.followTrajectory(strafeLeft);

        Left_Front_Wheel.setPower(-0.25);
        Right_Front_Wheel.setPower(0.25);
        Left_Rear_Wheel.setPower(0.25);
        Right_Rear_Wheel.setPower(-0.25);
        sleep(1000);
        Left_Front_Wheel.setPower(0);
        Right_Front_Wheel.setPower(0);
        Left_Rear_Wheel.setPower(0);
        Right_Rear_Wheel.setPower(0);


        //move back 45 inches so in warehouse
        Trajectory backward = drive.trajectoryBuilder(strafeLeft.end())
                .back(45)
                .build();
        drive.followTrajectory(backward);
        backward.end();

        //strafe right a little to protect against potentially getting rammed by the other team
        strafeRight = drive.trajectoryBuilder(backward.end())
                .strafeRight(20)
                .build();
        drive.followTrajectory(strafeRight);
        strafeRight.end();

    }


    private void third(){

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        if (isStopRequested()) return;

        //forward so doesnt hit anything
        Trajectory forward = drive.trajectoryBuilder(new Pose2d())
                .forward(7)
                .build();
        drive.followTrajectory(forward);
        forward.end();

        //strafe right a little so doesn't hit wall while turning later on
        Trajectory strafeRight = drive.trajectoryBuilder(forward.end())
                .strafeRight(11.5)
                .build();
        drive.followTrajectory(strafeRight);
        strafeRight.end();

        //move forward so parallel to shipping hub
        forward = drive.trajectoryBuilder(strafeRight.end())
                .forward(16)
                .build();
        drive.followTrajectory(forward);


        //turn 90 degrees so robot is facing backwards (battery to the wall and arm is pointing to shipping hub)
        drive.turn(Math.toRadians(95));

        //arm, elbow, picker init
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



        // extend arm
        arm.setTargetPosition(1100);
        while (arm.getCurrentPosition() < 1050 && opModeIsActive()) {
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(1);
            telemetry.addData("current position", arm.getCurrentPosition());
            telemetry.update();
        }
        arm.setPower(0);

        //running the picker to drop the cube
        int x=0;
        while (x<50000 && opModeIsActive()){
            picker.setPower(1);
            x++;
            telemetry.addData("x value: ", x);
            telemetry.update();
        }
        picker.setPower(0);

        //arm retracts
        arm.setTargetPosition(0);
        while (arm.getCurrentPosition() > 50 && opModeIsActive()) {
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(-1);
            telemetry.addData("current position", arm.getCurrentPosition());
            telemetry.update();
        }
        arm.setPower(0);


        //robot turns 90 degrees so previous 90 turn is cancelled
        drive.turn(Math.toRadians(-95));

        //strafe Left so robot touches wall again
        Trajectory strafeLeft = drive.trajectoryBuilder(strafeRight.end())
                .strafeLeft(13)
                .build();

        drive.followTrajectory(strafeLeft);

        Left_Front_Wheel.setPower(-0.25);
        Right_Front_Wheel.setPower(0.25);
        Left_Rear_Wheel.setPower(0.25);
        Right_Rear_Wheel.setPower(-0.25);
        sleep(1000);
        Left_Front_Wheel.setPower(0);
        Right_Front_Wheel.setPower(0);
        Left_Rear_Wheel.setPower(0);
        Right_Rear_Wheel.setPower(0);


        //move back 45 inches so in warehouse
        Trajectory backward = drive.trajectoryBuilder(strafeLeft.end())
                .back(45)
                .build();
        drive.followTrajectory(backward);
        backward.end();

        //strafe right a little to protect against potentially getting rammed by the other team
        strafeRight = drive.trajectoryBuilder(backward.end())
                .strafeRight(20)
                .build();
        drive.followTrajectory(strafeRight);
        strafeRight.end();

    }








    @Override
    public void runOpMode() throws InterruptedException {
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1, 16.0/9.0);
        }

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();


        waitForStart();

        if (opModeIsActive()) {

            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                String position = "third";
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());

                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        double left = recognition.getLeft();
                        double right = recognition.getRight();
                        double top = recognition.getTop();
                        double bottom = recognition.getBottom();

                        if(recognition.getLabel().equals("Duck") && left>370 && left<480&& top>50 && top<170 &&right>460 && right<590 && bottom>130 && bottom<270){
                            position = "first";
                            break;
                        }
                        if(recognition.getLabel().equals("Duck") && left>60 && left<190&& top>40 && top<190 &&right>160 && right<290 && bottom>130 && bottom<290){
                            position = "second";
                            break;
                        }
                        i++;
                    }
                    telemetry.addData("position",position);
                    telemetry.update();
                    sleep(2000);

                    if(position.equals("first")){
                        first();
                    }
                    if(position.equals("second")){
                        first();
                    }
                    if(position.equals("third")){
                        first();
                    }
                }
            }
        }

    }




}

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Disabled
@Autonomous(group = "drive")
public class autonomous extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);


        waitForStart();
        if (isStopRequested()) return;

        /*Trajectory forward = drive.trajectoryBuilder(new Pose2d())
                .forward(20)
                .build();

        drive.followTrajectory(forward);
        forward.end();
        sleep(5000); */

        /*Trajectory strafeRightTrajectory = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(40)
                .build();
        drive.followTrajectory(strafeRightTrajectory);
        strafeRightTrajectory.end();
        sleep(500); */

        Trajectory strafeLeftTrajectory = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(40)
                .build();
        drive.followTrajectory(strafeLeftTrajectory);
        strafeLeftTrajectory.end();
        sleep(500);

        /*Trajectory backwardTrajectory = drive.trajectoryBuilder(new Pose2d())
                .back(15)
                .build();
        drive.followTrajectory(backwardTrajectory);
        backwardTrajectory.end();*/

        /*Trajectory forwardTrajectory = drive.trajectoryBuilder(new Pose2d())
                .forward(15)
                .build();
        drive.followTrajectory(forwardTrajectory);
        forwardTrajectory.end();*/


    }


}

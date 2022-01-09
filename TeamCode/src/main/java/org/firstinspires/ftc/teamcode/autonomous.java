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

@Autonomous(group = "drive")
public class autonomous extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);


        waitForStart();
        if (isStopRequested()) return;



        /*Trajectory strafeRightTrajectory = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(10*1.32)
                .build();
        drive.followTrajectory(strafeRightTrajectory);
        strafeRightTrajectory.end();
        sleep(5000);

        strafeRightTrajectory = drive.trajectoryBuilder(strafeRightTrajectory.end())
                .strafeRight(15*1.32)
                .build();
        drive.followTrajectory(strafeRightTrajectory);
        strafeRightTrajectory.end();
        sleep(5000);

        strafeRightTrajectory = drive.trajectoryBuilder(strafeRightTrajectory.end())
                .strafeRight(5*1.32)
                .build();
        drive.followTrajectory(strafeRightTrajectory);
        strafeRightTrajectory.end();
        sleep(5000);

        strafeRightTrajectory = drive.trajectoryBuilder(strafeRightTrajectory.end())
                .strafeRight(20*1.32)
                .build();
        drive.followTrajectory(strafeRightTrajectory);
        strafeRightTrajectory.end();
        sleep(5000);*/


        /*Trajectory backward = drive.trajectoryBuilder(new Pose2d())
                .back(20)
                .build();

        drive.followTrajectory(backward);
        backward.end();
        sleep(5000);*/

        /*
        strafeRightTrajectory = drive.trajectoryBuilder(forward.end())
                .strafeRight(35)
                .build();
        drive.followTrajectory(strafeRightTrajectory);
        strafeRightTrajectory.end();
        sleep(5000);*/




    }


}

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
    double DISTANCE = 7; // in
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory strafeRightTrajectory = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(DISTANCE)
                .build();
        DISTANCE = 26;
        Trajectory backwardTrajectory = drive.trajectoryBuilder(strafeRightTrajectory.end())
                .back(DISTANCE)
                .build();

        waitForStart();
        if (isStopRequested()) return;

        drive.followTrajectory(strafeRightTrajectory);

        drive.followTrajectory(backwardTrajectory);

    }


}

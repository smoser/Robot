// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class TurnDegrees extends CommandBase {
  private final Drive drive_subsystem;
  private double target_degrees = 0.0; //number of degrees to turn
  private double error_degrees = 1.0; //default error margin in degrees
  private double target_rotations = 0.0; //in sparkmax rotations
  private double error_rotations; //in sparkmax rotations
  private double left_start_rotations = 0.0; //in sparkmax rotations
  private double right_start_rotations = 0.0; //in sparkmax rotations
  private double left_target_rotations = 0.0; //in sparkmax rotations
  private double right_target_rotations = 0.0; //in sparkmax rotations
  boolean left_at_target = false;
  boolean right_at_target = false;

  // constructor that takes in degrees to turn, default error is 1 degree
  public TurnDegrees(Drive subsystem, double degrees) {
    drive_subsystem = subsystem;
    addRequirements(subsystem);
    target_degrees = degrees;
  }
  
  // constructor that takes in degrees to turn and amount of acceptable error
  public TurnDegrees(Drive subsystem, double degrees, double error) {
    drive_subsystem = subsystem;
    addRequirements(subsystem);
    target_degrees = degrees;
    if(error < 0.5){
      error_degrees = 0.5;
    }else{
      error_degrees = error;
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    left_start_rotations = m_drive.getLeftRotations();
    right_start_rotations = m_drive.getRightRotations();

    // should start robot in motion using sparkmax hardware PID/enconders
    drive_subsystem.setLeftTarget(
        left_start_rotations + degreesToRotations(target_degrees))
    drive_subsystem.setRightTarget(
        right_start_rotations + degreesToRotations(-target_degrees))
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  private static boolean isDone(double target, double cur, double closeEnough) {
    return ((target - closeEnough) <= cur && cur <= (target + closeEnough));
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double curLeft = drive_subsystem.getLeftRotations();
    double curRight = drive_subsystem.getRightRotations();

    System.out.println("Left Degrees Turned: " +
        rotationsToDegrees(curLeft - left_start_rotations))
    System.out.println("Right Degrees Turned: " +
        rotationsToDegrees(curRight - right_start_rotations))

    return isDone(left_target_rotations, curLeft, error_rotations) &&
        isDone(right_target_rotations, curRight, error_rotations)
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive_subsystem.cancelLeftTarget();
    drive_subsystem.cancelRightTarget();
  }
  
  private double degreesToRotations(double degrees){
      return ((degrees/360.0)*Constants.rotationsPer360);
  }
  private double rotationsToDegrees(double rotations){
    return ((rotations/Constants.rotationsPer360) * 360.0);
  }
}

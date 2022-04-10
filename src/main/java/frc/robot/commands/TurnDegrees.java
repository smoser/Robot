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
  private double left_current_rotations = 0.0; //in sparkmax rotations
  private double right_current_rotations = 0.0; //in sparkmax rotations
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
    drive_subsystem.resetEncoders();
    left_current_rotations = 0.0;
    right_current_rotations = 0.0;

    //convert target degrees to sparkmax rotations
    target_rotations = degreesToRotations(target_degrees);
    //convert error margin in degrees to sparkmax rotations 
    error_rotations = degreesToRotations(error_degrees);

    // should start robot in motion using sparkmax hardware PID/enconders
    drive_subsystem.setLeftRotations(target_rotations);//left is pos
    drive_subsystem.setRightRotations(-target_rotations);//right is neg
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(!left_at_target){
      left_current_rotations = drive_subsystem.getLeftRotations();
      if(left_current_rotations > (target_rotations - error_rotations) && left_current_rotations < (target_rotations + error_rotations)){
        left_at_target = true;
      }
    }
    if(!right_at_target){
      right_current_rotations = drive_subsystem.getRightRotations();
      if(right_current_rotations > (target_rotations - error_rotations) && right_current_rotations < (target_rotations + error_rotations)){
        right_at_target = true;
      }
    }
    System.out.println("Left Degrees Turned: " + rotationsToDegrees(left_current_rotations));
    System.out.println("Right Degrees Turned: " + rotationsToDegrees(right_current_rotations));

    return(left_at_target && right_at_target);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive_subsystem.resetEncoders();
    drive_subsystem.resetPIDControllerReference();
  }
  
  private double degreesToRotations(double degrees){
      return ((degrees/360.0)*Constants.rotationsPer360);
  }
  private double rotationsToDegrees(double rotations){
    return ((rotations/Constants.rotationsPer360) * 360.0);
  }
}

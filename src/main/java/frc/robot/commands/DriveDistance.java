// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DriveDistance extends CommandBase {
  private final Drive drive_subsystem;
  private double target_distance = 0.0; // desired distance to travel in inches
  private double error_inches = 2.0; // default error margin in inches
  private double target_rotations = 0.0; // in sparkmax rotations
  private double error_rotations; //in sparkmax rotations
  private double left_current_rotations = 0.0; // in sparkmax rotations
  private double right_current_rotations = 0.0; // in sparkmax rotations
  boolean left_at_target = false;
  boolean right_at_target = false;

  // constructor that takes in a distance in inches and uses default error margin
  public DriveDistance(Drive subsystem, double distance) {
    drive_subsystem = subsystem;
    addRequirements(subsystem);
    target_distance = distance;
  }

  // constructor that takes in a distance in inches and specified error margin in inches
  public DriveDistance(Drive subsystem, double distance, double error) {
    drive_subsystem = subsystem;
    addRequirements(subsystem);
    target_distance = distance;
    if(error < 1.0){
      error_inches = 1.0;
    }else{
      error_inches = error;
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drive_subsystem.resetEncoders();
    left_current_rotations = 0.0;
    right_current_rotations = 0.0;

    // convert target distance in inches into sparkmax rotations
    target_rotations = (inchesToRotations(target_distance));
    // convert error margin in inches into sparkmax rotations
    error_rotations = (inchesToRotations(error_inches));

    // should start robot in motion using sparkmax hardware PID/enconders
    drive_subsystem.setLeftRotations(target_rotations);
    drive_subsystem.setRightRotations(target_rotations);
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
    System.out.println("Left Inches Travelled: " + rotationsToInches(left_current_rotations));
    System.out.println("Right Inches Travelled: " + rotationsToInches(right_current_rotations));
    
    return(left_at_target && right_at_target);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive_subsystem.resetEncoders();
    drive_subsystem.resetPIDControllerReference();
  }
  
  //returns equivalent sparkmax rotations
  private double inchesToRotations(double inches){
    return ((inches/Constants.wheelCircumferenceInches) * Constants.driveGearRatio);
  }
  //returns equivalent inches
  private double rotationsToInches(double rotations){
    return ((rotations/Constants.driveGearRatio) * Constants.wheelCircumferenceInches);
  }  
}

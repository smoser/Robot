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
  private final Drive m_subsystem;
  private double target_distance = 0.0; // desired distance to travel in inches
  private double error_inches = 2.0; // default error margin in inches
  private double target_rotations = 0.0; // in sparkmax rotations
  private double current_rotations = 0.0; // in sparkmax rotations
  private double error_rotations; //in sparkmax rotations

  // constructor that takes in a distance in inches and uses default error margin
  public DriveDistance(Drive subsystem, double distance) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
    target_distance = distance;
  }

  // constructor that takes in a distance in inches and specified error margin in inches
  public DriveDistance(Drive subsystem, double distance, double error) {
    m_subsystem = subsystem;
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
    m_subsystem.resetEncoders();
    current_rotations = 0.0;
    // convert target distance in inches into sparkmax rotations
    target_rotations = (inchesToRotations(target_distance));
    // convert error margin in inches into sparkmax rotations
    error_rotations = (inchesToRotations(error_inches));

    // should start robot in motion using sparkmax hardware PID/enconders
    m_subsystem.setLeftRotations(target_rotations);
    m_subsystem.setRightRotations(target_rotations);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    current_rotations =  (m_subsystem.getLeftRotations() + m_subsystem.getRightRotations())/2.0;//takes average, could check individually instead
    double abs_current_rotations = Math.abs (current_rotations); // putting everything positive reduces number of compares below, error margin is always pos
    double abs_target_rotations = Math.abs(target_rotations); //putting everything positive reduces number of compares below, error margin is always pos
    SmartDashboard.putNumber("Current Inches", rotationsToInches(current_rotations));//display in inches
    SmartDashboard.putNumber("Target Inches", target_distance);
    if(abs_current_rotations > (abs_target_rotations - error_rotations) && abs_current_rotations < (abs_target_rotations + error_rotations)){
      return true;
    }
    return false;   // could also add a failsafe to stop after 10 seconds under all circumstances
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.resetEncoders();
    m_subsystem.setLeftRotations(0);
    m_subsystem.setRightRotations(0);
    m_subsystem.setDrive(0,0); //probably unnecessary, but just in case
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

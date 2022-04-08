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
  private final Drive m_subsystem;
  private double target_degrees = 0.0; //number of degrees to turn
  private double error_degrees = 1.0; //default error margin in degrees
  private double target_rotations = 0.0; //in sparkmax rotations
  private double left_current_rotations = 0.0; //in sparkmax rotations
  private double right_current_rotations = 0.0; //in sparkmax rotations
  private double error_rotations; //in sparkmax rotations

  // constructor that takes in degrees to turn, default error is 1 degree
  public TurnDegrees(Drive subsystem, double degrees) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
    target_degrees = degrees;
  }
  
  // constructor that takes in degrees to turn and amount of acceptable error
  public TurnDegrees(Drive subsystem, double degrees, double error) {
    m_subsystem = subsystem;
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
    m_subsystem.resetEncoders();
    left_current_rotations = 0.0;
    right_current_rotations = 0.0;
    target_rotations = degreesToRotations(target_degrees); 
    error_rotations = degreesToRotations(error_degrees);

    // should start robot in motion using sparkmax hardware PID/enconders
    m_subsystem.setLeftRotations(target_rotations);//left is pos
    m_subsystem.setRightRotations(-target_rotations);//right is neg
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // could maybe also add a failsafe to stop after 3 seconds under all circumstances
    boolean leftDone = false;
    boolean rightDone = false;
    left_current_rotations = Math.abs(m_subsystem.getLeftRotations());//one will be neg depending on turn direction
    right_current_rotations = Math.abs(m_subsystem.getRightRotations());//one will be neg depending on turn direction
    SmartDashboard.putNumber("Current Degrees", rotationsToDegrees(left_current_rotations));// display in degrees
    SmartDashboard.putNumber("Target Degrees", target_degrees);
    
    //left_current_rotations and right_current_rotations should always be postive
    if(left_current_rotations > (target_rotations - error_rotations) && left_current_rotations < (target_rotations + error_rotations)){
      leftDone = true;
    }
    if(right_current_rotations > (target_rotations - error_rotations) && right_current_rotations < (target_rotations + error_rotations)){
      rightDone = true;
    }
    if(leftDone && rightDone){
      return true;
    }
    return false;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.resetEncoders();
    m_subsystem.setLeftRotations(0);
    m_subsystem.setRightRotations(0);
    m_subsystem.setDrive(0,0); //probably unnecessary, but just in case
  }
  
  private double degreesToRotations(double degrees){
      return ((degrees/360.0)*Constants.rotationsPer360);
  }
  private double rotationsToDegrees(double rotations){
    return ((rotations/Constants.rotationsPer360) * 360.0);
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Sensors;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;



public class RetractClimb extends CommandBase {
  private final Climb m_climb;
  private final Sensors m_sensor;

  /**
   *
   *
   * @param subsystem The subsystem used by this command.
   */
  public RetractClimb(Climb subsystem, Sensors sensor) {
    m_climb = subsystem;
    m_sensor = sensor;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_climb.isArmExtended()){
      m_climb.runWench();
    }
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climb.stopWench();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(m_sensor.getClimbSensor()){
      return true;
    }
    else{
      return false;
    }
  }
}

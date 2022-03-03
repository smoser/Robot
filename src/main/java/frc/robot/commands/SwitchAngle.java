// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Launch;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class SwitchAngle extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Launch m_subsystem;
  private boolean isUsed;
  private int timer;

  /**
   *
   *
   * @param subsystem The subsystem used by this command.
   */
  public SwitchAngle(Launch subsystem) {
    m_subsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isUsed = false;
    timer = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_subsystem.getAngle() && !isUsed){
      m_subsystem.setFar();
      isUsed = true;
    }
    else if(!m_subsystem.getAngle() && !isUsed){
      m_subsystem.setClose();
      isUsed = true;
    }

    timer++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.setOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(timer > 50){
      return true;
    }
    else{
    return false;
    }
  }
}

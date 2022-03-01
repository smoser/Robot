// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.IntakeSolenoid;

import javax.management.timer.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class SwitchIntakeSolenoid extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final IntakeSolenoid m_subsystem;
  private boolean beenUsed = false;
  int timer = 0;
  

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public SwitchIntakeSolenoid(IntakeSolenoid subsystem) {
    m_subsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer = 0;
    beenUsed = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_subsystem.getExtended() && !beenUsed){
      m_subsystem.runReverse();
      beenUsed = true;
    }
    else if(!m_subsystem.getExtended() && !beenUsed){
      m_subsystem.runForeward();
      beenUsed = true;
    }
    timer++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

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

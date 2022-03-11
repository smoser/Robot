// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class RunIntakeAuton extends CommandBase {
  private final Intake m_subsystem;
  int i;

  private long m_startTime = 0;

  /**
   * Creates a new FrontIntake.
   *
   * @param subsystem The subsystem used by this command.
   */
  public RunIntakeAuton(Intake subsystem) {
    m_subsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_startTime = System.currentTimeMillis(); 
    m_subsystem.stopFront();
    m_subsystem.stopBack();
    i = 1;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_subsystem.startFront();
    m_subsystem.startBack();
    i++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(i > 25){
      return true;
    }
    return false;
  }
}

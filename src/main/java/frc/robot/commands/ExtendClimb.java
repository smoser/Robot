// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Climb;

import edu.wpi.first.wpilibj.Timer;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;


public class ExtendClimb extends CommandBase {
  private final Climb m_subsystem;
  private DoubleSupplier speed;
  private final Timer m_timer = new Timer();

  /**
   * Creates a new Command
   *
   * @param subsystem The subsystem used by this command.
   */
  public ExtendClimb(Climb subsystem) {
    m_subsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_subsystem.retractSolenoid();
    m_timer.reset();
    m_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.extendSolenoid();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(m_timer.get() > 2){
      return true;
    }
    else{
      return false;
    }
  }
}

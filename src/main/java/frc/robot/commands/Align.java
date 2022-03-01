// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Limelight;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class Align extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Drive m_drive;
  private final Limelight m_limelight;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public Align(Drive drive, Limelight limelight) {
    m_drive = drive;
    m_limelight = limelight;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_limelight.tx() < -1){
      m_drive.setDrive(0, -0.75);
    }
    else if(m_limelight.tx() > 1){
      m_drive.setDrive(0, 0.75);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.setDrive(0,0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(m_limelight.tx() > -1 && m_limelight.tx() < 1 || m_limelight.tv() == 0){
      return true;
    }
    else{
      return false;
    }
  }
}

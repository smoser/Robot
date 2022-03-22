// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Limelight;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Align extends CommandBase {
  private final Drive m_drive;
  private final Limelight m_limelight;
  private final double deadZone = .7f;

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
    double tx = m_limelight.tx();
    double kP = -0.3f;
    double minCommand = 0.05f;
    double adj = 0.0f;

    double power = kP * Math.abs(tx) + minCommand;
    if (tx > deadZone) {
        adj = -power;
    } else if (tx < deadZone) {
        adj = power;
    }
    m_drive.setDrive(0, adj);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.setDrive(0,0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
      if (!m_limelight.tv()) {
          return true;
      }
      double tx = m_limelight.tx();
      if ((0 - deadZone) < tx && (tx > deadZone)) {
          return true;
      }
      return false;
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveTime extends CommandBase {
  private final Drive m_drive;
  private Double m_time;
  private Double m_power = 0.6;
  private final Timer m_timer = new Timer();

  public DriveTime(Drive drive, Double time, Double power) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);

    m_drive = drive;
    m_time = time;
    m_power = power;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
    m_drive.setDrive(m_power, 0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // I'm 98% sure this is not necessary here, but not able to verify.
    m_drive.setDrive(m_power, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.setDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_timer.get() > m_time);
  }
}

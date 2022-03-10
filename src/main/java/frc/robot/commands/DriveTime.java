// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveTime extends CommandBase {
  private final Drive m_drive;
  private Intake m_intake;
  private Double m_driveTime = 0.0, m_intakeTime = 0.0;
  private Double m_speed = 0.6;
  private final Timer m_timer = new Timer();

  public DriveTime(Drive drive, Double time, Double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);

    m_drive = drive;
    m_driveTime = time;
    m_speed = speed;
  }

  public DriveTime(Drive drive, Double driveTime, Double speed, Intake intake, Double intakeTime) {
    this(drive, driveTime, speed);
    addRequirements(intake);
    m_intake = intake;
    m_intakeTime = intakeTime;
  }

  private void startIntake() {
    if (m_intake == null) {
      return;
    }
    m_intake.startFront();
    m_intake.startBack();
  }

  private void stopIntake() {
    if (m_intake == null) {
      return;
    }
    m_intake.stopFront();
    m_intake.stopBack();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
    m_drive.setDrive(m_speed, 0);
    startIntake();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_timer.get() > m_driveTime) {
      m_drive.setDrive(0, 0);
    } else {
      // I'm 98% sure this is not necessary here, but not able to verify.
      m_drive.setDrive(m_speed, 0);
    }

  if (m_timer.get() > m_intakeTime) {
    stopIntake();
  }
}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.setDrive(0, 0);
    stopIntake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_timer.get() > m_driveTime && m_timer.get() > m_intakeTime);
  }
}

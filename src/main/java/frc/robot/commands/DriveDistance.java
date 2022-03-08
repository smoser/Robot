// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drive;
import frc.robot.Constants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveDistance extends CommandBase {
  private final Drive m_drive;
  private Double m_distance;
  private Double lTarget, rTarget;
  //Creating a margin of error for the motors
  private final double errorMargin = 0.2;
  private final double distancePerRotation = Constants.wheelDiameterFeet * Math.PI;

  // Distance is in inches.
  public DriveDistance(Drive subsystem, Double dist) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);

    m_drive = subsystem;
    m_distance = dist;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      // This could use RelativeEncoder.setPositionConversionFactor to then use
      // a different unit.
      double l = m_drive.getLeftRotations();
      double r = m_drive.getRightRotations();

      lTarget = m_drive.getLeftRotations() + (m_distance / distancePerRotation) + errorMargin;
      rTarget = m_drive.getRightRotations() + (m_distance / distancePerRotation) + errorMargin;
      
      m_drive.setLeftRotations(lTarget);
      m_drive.setRightRotations(rTarget);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      if (interrupted) {
          // Since we were intereuppted, cancel the 'setPosition'
          m_drive.setLeftRotations(m_drive.getLeftRotations());
          m_drive.setRightRotations(m_drive.getRightRotations());
      }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_distance < 0) {
        return (m_drive.getLeftRotations() <= lTarget + errorMargin &&
                m_drive.getRightRotations() <= rTarget + errorMargin);
    }
    return (m_drive.getLeftRotations() >= lTarget - errorMargin &&
                m_drive.getRightRotations() >= rTarget - errorMargin);
  }
}

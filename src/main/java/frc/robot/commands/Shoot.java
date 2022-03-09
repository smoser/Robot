// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Index;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Limelight;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class Shoot extends CommandBase {
  private final Launch m_launch;
  private final Index m_index;
  private Limelight m_limelight;
  private double launchSpeed;
  private boolean feedRunning = false;

  private final Timer m_timer = new Timer();

  private final double closeTargetRpm = 1600;
  private final double noLimelightRpm = 3500;

  /**
   * Creates a new shot command.
   *
   * @param subsystem The subsystem used by this command.
   */
  public Shoot(Launch launch, Index index, Limelight limelight) {
    this(launch, index);
    m_limelight = limelight;
  }

  public Shoot(Launch launch, Index index) {
    m_launch = launch;
    m_index = index;
    m_limelight = null;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(launch, index);
  }

  private double determineLaunchSpeed() {
    // close angle we shoot at closeAngleSpeed
    if(m_launch.getAngle()){
      return closeTargetRpm;
    }

    if (m_limelight == null || m_limelight.ta() == 0) {
      return noLimelightRpm;
    }

    return m_limelight.distance() * 82.738 + 143.2;
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    launchSpeed = determineLaunchSpeed();
    m_launch.setLaunchRpm(launchSpeed);
  }

  public boolean launcherReady() {
    double curRpm = m_launch.getCurRpm();
    return ((curRpm >= launchSpeed * 0.95) && curRpm <= launchSpeed * 1.05);
  }


  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (launcherReady()) {
      if (!feedRunning) {
        m_timer.reset();
        m_timer.start();
        m_launch.runFeed();
        m_index.runIndex();
        feedRunning = true;
      }
    }
    else if(!launcherReady()){
      m_launch.stopFeed();
      m_index.stopIndex();
      feedRunning = false;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_launch.stopFeed();
    m_launch.stopLaunch();
    m_index.stopIndex();
    m_timer.stop();
    feedRunning = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // feed has been running for 6 seconds.
    if(m_timer.get() > 6) {
      return true;
    }
    return false;
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Index;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Limelight;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class Shoot extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Launch m_launch;
  private final Index m_index;
  private final Limelight m_limelight;
  private double launchSpeed;
  private boolean feedRunning = false;

  private long m_startTime = 0;

  /**
   * Creates a new shot command.
   *
   * @param subsystem The subsystem used by this command.
   */
  public Shoot(Launch launch, Index index, Limelight limelight) {
    m_launch = launch;
    m_index = index;
    m_limelight = limelight;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(launch, index);
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_startTime = System.currentTimeMillis();
    
    if(m_launch.getAngle()){
      launchSpeed = 100;
      m_launch.setLaunchSpeed(launchSpeed);
      
    }
    else{
    launchSpeed = m_limelight.distance() * 82.738 + 143.2;
    m_launch.setLaunchSpeed(launchSpeed);
      if(m_launch.getCurVelo() >= launchSpeed){
        m_launch.runFeed();
        m_index.runIndex();
      }
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_launch.getCurVelo() >= 100 && !feedRunning){
      m_launch.runFeed();
      m_index.runIndex();
      feedRunning = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_launch.stopFeed();
    m_launch.stopLaunch();
    m_index.stopIndex();
    feedRunning = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

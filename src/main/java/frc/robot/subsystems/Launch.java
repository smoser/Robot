// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Launch extends SubsystemBase {
  private TalonSRX feed = new TalonSRX(6);
  double feedSpeed = 0.2;
  /** Creates a new ExampleSubsystem. */
  public Launch() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
  public void runFeed(){
    feed.set(ControlMode.PercentOutput, feedSpeed);
  }

  public void stopFeed(){
    feed.set(ControlMode.PercentOutput, 0.0);
  }
}

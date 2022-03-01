// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSolenoid extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  public IntakeSolenoid() {}

  private DoubleSolenoid solenoid0 = new DoubleSolenoid(12, PneumaticsModuleType.CTREPCM, 0, 1);
  private boolean extended = false;

  public void runForeward(){
    solenoid0.set(Value.kForward);
    extended = !extended;
  }

  public void runReverse(){
    solenoid0.set(Value.kReverse);
    extended = !extended;
  }

  public void off(){
    solenoid0.set(Value.kOff);
  }

  public boolean getExtended(){
    return extended;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}

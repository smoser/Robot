// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalGlitchFilter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Sensors extends SubsystemBase {

  public Sensors() {}

  private DigitalInput breakBeam0 = new DigitalInput(0);
  private DigitalInput breakBeam1 = new DigitalInput(1);

  public boolean getSensor0(){
    return breakBeam0.get();
  }

  public boolean getSensor1(){
    return breakBeam1.get();
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

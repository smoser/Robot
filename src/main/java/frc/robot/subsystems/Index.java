// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class Index extends SubsystemBase {
  /** Creates a new Index Subsystem. */
  public Index() {}

  private WPI_TalonSRX index = new WPI_TalonSRX(6);

  private DigitalInput breakBeam0 = new DigitalInput(0);
  private DigitalInput breakBeam1 = new DigitalInput(1);

  

  final static int STATE_START = 0;
  final static int STATE_IDLE = 1;
  final static int STATE_RUNNING = 2;

  private int state = STATE_START;

  boolean sensor0 = breakBeam0.get();
  boolean sensor1 = breakBeam1.get();

  
  public boolean getSensor0(){
    return sensor0;
  }

  public boolean getSensor1(){
    return sensor1;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putBoolean("sensor1", sensor1);
    SmartDashboard.putBoolean("sensor0", sensor0);
    

    switch(state){
      case STATE_START:
      state = STATE_IDLE;
      break;

      case STATE_IDLE:
      if(sensor0 && !sensor1)
        state = STATE_RUNNING;
      else if(true)
        state = STATE_IDLE;
      break;

      case STATE_RUNNING:
      if(sensor1)
        state = STATE_IDLE;
      else if(true)
        state = STATE_RUNNING;
      break;
    }

    if(state == STATE_IDLE){
      index.set(0);
    }
    else if(state == STATE_RUNNING){
      index.set(0.5);
    }

    }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}

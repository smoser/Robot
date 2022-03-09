// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class Index extends SubsystemBase {
  /** Creates a new Index Subsystem. */
  public Index() {}

  private WPI_TalonSRX index = new WPI_TalonSRX(6);

  final static int STATE_START = 0;
  final static int STATE_IDLE = 1;
  final static int STATE_RUNNING = 2;
  final static int STATE_MANUAL = 3;

  private int state = STATE_START;

  public void manualIndex(DoubleSupplier m_manualInput){
    index.set(m_manualInput.getAsDouble() * -1f);
  }

  public void runIndex(){
    index.set(1.0);
  }

  public void stopIndex(){
    index.set(0);
  }

  public void state(boolean driverInput){
    RobotContainer m_container = RobotContainer.getInstance();
    Sensors m_sensors = m_container.getSensors();

    boolean sensor0 = m_container.getSensors().getSensor0();
    boolean sensor1 = m_container.getSensors().getSensor1();

    switch(state){
      case STATE_START:
      state = STATE_IDLE;
      break;

      case STATE_IDLE:
      if(driverInput)
        state = STATE_MANUAL;
      else if(!sensor0 && sensor1)
        state = STATE_RUNNING;
      else if(true)
        state = STATE_IDLE;
      break;

      case STATE_RUNNING:
      if(!sensor1)
        state = STATE_IDLE;
      else if(driverInput)
        state = STATE_MANUAL;
      else if(true)
        state = STATE_RUNNING;
      break;

      case STATE_MANUAL:
      if(!driverInput)
        state = STATE_IDLE;
      else if(true)
        state = STATE_MANUAL;
      return;
    }

    if(state == STATE_IDLE){
      index.set(0);
    }
    else if(state == STATE_RUNNING){
      index.set(1);
    }

  }


  @Override
  public void periodic() {
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}

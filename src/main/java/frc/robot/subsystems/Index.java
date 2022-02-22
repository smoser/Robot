// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Index extends SubsystemBase {
  /** Creates a new Index Subsystem. */
  public Index() {}

  private WPI_TalonSRX index = new WPI_TalonSRX(6);

  private DigitalInput breakBeam1 = new DigitalInput(0);
  private DigitalInput breakBeam2 = new DigitalInput(1);
  private DigitalInput breakBeam3 = new DigitalInput(2);

  final static int STATE_START = 0;
  final static int STATE_IDLE = 1;
  final static int STATE_MAGAZINE = 2;
  final static int STATE_CHAMBERED = 3;
  final static int STATE_TWO_BALL = 4;
  final static int STATE_RUNNING = 5;

  private int state = STATE_START;

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    switch(state){
      case STATE_START:
      state = STATE_IDLE;
      break;

      case STATE_IDLE:
      if(false)
        state = STATE_IDLE;
      break;

      case STATE_MAGAZINE:
      state = STATE_IDLE;
      break;

      case STATE_CHAMBERED:
      state = STATE_IDLE;
      break;
      
      case STATE_TWO_BALL:
      state = STATE_IDLE;
      break;

      case STATE_RUNNING:
      state = STATE_IDLE;
      break;


    }

    }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}

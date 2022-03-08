// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Launch extends SubsystemBase {
  private TalonSRX feed = new TalonSRX(7);
  private TalonSRX bottom = new TalonSRX(10);
  private TalonSRX top = new TalonSRX(11);

  private DoubleSolenoid launchSolenoid = new DoubleSolenoid(12, PneumaticsModuleType.CTREPCM, Constants.launchSolenoidForewardID, Constants.launchSolenoidReverseID);

  private boolean angleClose = true;
  private int counter = 0;
  private double targetRpm = 0;

  double feedSpeed = 0.2;

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    counter++;
    if (counter == 25) {
        counter = 0;
        SmartDashboard.putNumber("Bottom Velocity [cur]", getCurRpm());
        SmartDashboard.putNumber("Bottom Output   [cur]", bottom.getMotorOutputPercent());
    }
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

  public void setLaunchRpm(double target){
    /* Get Talon/Victor's current output percentage */
    double motorOutput = bottom.getMotorOutputPercent();

    targetRpm = target;

    bottom.set(ControlMode.Velocity, rpmToVelo(targetRpm));
    top.set(ControlMode.Velocity, rpmToVelo(targetRpm));

    SmartDashboard.putNumber("Bottom Velocity [tar]", targetRpm);
    SmartDashboard.putNumber("Bottom Velocity [cur]", getCurRpm());
    SmartDashboard.putNumber("Bottom Output   [cur]", motorOutput);
  }

  // convert native velocity (units of 4096 per rotation per 100ms) to RPM
  public static double veloToRpm(double velocity) {
      return (velocity / 4096 ) * 600;
  }

  /**
   * Convert RPM to units / 100ms.
   * 4096 Units/Rev * RPM / 600 100ms/min in either direction:
   * velocity setpoint is in units/100ms
   */
  public static double rpmToVelo(double rpm) {
      return (rpm * 4096) / 600;
  }

  // get the current RPM
  public double getCurRpm(){
    return veloToRpm(bottom.getSelectedSensorVelocity(Constants.kPIDLoopIdx));
  }

  public void stopLaunch(){
    top.set(ControlMode.PercentOutput, 0);
    bottom.set(ControlMode.PercentOutput, 0);
  }


  public boolean getAngle(){
    return angleClose;
  }

  public void setClose(){
    launchSolenoid.set(Value.kReverse);
    angleClose = true;
  }

  public void setFar(){
    launchSolenoid.set(Value.kForward);
    angleClose = false;
  }

  public void setOff(){
    launchSolenoid.set(Value.kOff);
  }


  public void setupTalonEncoder(TalonSRX talon) {
    talon.configFactoryDefault();

             /* Config sensor used for Primary PID [Velocity] */
    talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
                                         Constants.kPIDLoopIdx,
                                         Constants.kTimeoutMs);

    /**
     * Phase sensor accordingly.
      * Positive Sensor Reading should match Green (blinking) Leds on Talon
     */
    talon.setSensorPhase(false);

    /* Config the peak and nominal outputs */
    talon.configNominalOutputForward(0, Constants.kTimeoutMs);
    talon.configNominalOutputReverse(0, Constants.kTimeoutMs);
    talon.configPeakOutputForward(1, Constants.kTimeoutMs);
    talon.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    /* Config the Velocity closed loop gains in slot0 */
    talon.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kF, Constants.kTimeoutMs);
    talon.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kP, Constants.kTimeoutMs);
    talon.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
    talon.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
  }

  public void doInit() {
      setupTalonEncoder(bottom);
      setupTalonEncoder(top);
  }
}

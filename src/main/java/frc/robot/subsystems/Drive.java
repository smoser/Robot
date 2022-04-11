// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {
  /** Creates a new Drive subsystem. */


    private CANSparkMax leftFront = new CANSparkMax(1, MotorType.kBrushless);
    private CANSparkMax rightFront = new CANSparkMax(4, MotorType.kBrushless);
    private CANSparkMax leftBack = new CANSparkMax(2, MotorType.kBrushless);
    private CANSparkMax rightBack = new CANSparkMax(3, MotorType.kBrushless);
    private SparkMaxPIDController leftFrontPidController;
    private SparkMaxPIDController rightFrontPidController;
    private SparkMaxPIDController leftBackPidController;
    private SparkMaxPIDController rightBackPidController;
    private RelativeEncoder leftFrontEncoder = leftFront.getEncoder();
    private RelativeEncoder rightFrontEncoder = rightFront.getEncoder();
    private RelativeEncoder leftBackEncoder = leftBack.getEncoder();
    private RelativeEncoder rightBackEncoder = rightBack.getEncoder();
    private boolean sparkFollow = true;

    private MotorControllerGroup leftGroup, rightGroup;
    private DifferentialDrive driveDifferential;

  public Drive() {
    doInit();
    if (sparkFollow) {
      leftGroup = new MotorControllerGroup(leftFront);
      rightGroup = new MotorControllerGroup(rightFront);
    } else {
      leftGroup = new MotorControllerGroup(leftFront, leftBack);
      rightGroup = new MotorControllerGroup(rightFront, rightBack);
    }

    leftFrontPidController = leftFront.getPIDController();
    rightFrontPidController = rightFront.getPIDController();
    leftBackPidController = leftBack.getPIDController();
    rightBackPidController = rightBack.getPIDController();

    driveDifferential = new DifferentialDrive(leftGroup, rightGroup);
  }

  public void arcadeDrive(DoubleSupplier speed, DoubleSupplier rotation) {
      driveDifferential.arcadeDrive(speed.getAsDouble() * 0.7f, rotation.getAsDouble() * -0.7f);
  }

  public void tankDrive(DoubleSupplier leftSpeed, DoubleSupplier rightSpeed){
    driveDifferential.tankDrive(leftSpeed.getAsDouble() * 0.75f, rightSpeed.getAsDouble() * 0.75f);
  }

  public void tankDriveFast(DoubleSupplier leftSpeed, DoubleSupplier rightSpeed){
    driveDifferential.tankDrive(leftSpeed.getAsDouble(), rightSpeed.getAsDouble());
  }

  public void setDrive(double speed, double rotation){
    driveDifferential.arcadeDrive(speed, rotation);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Left Velocity", leftFrontEncoder.getVelocity());
    SmartDashboard.putNumber("Right Velocity", rightFrontEncoder.getVelocity());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public double getLeftRotations() {
    return leftFrontEncoder.getPosition();
  }

  public double getRightRotations() {
    return rightFrontEncoder.getPosition();
  }

  public void cancelLeftTarget() {
    // cancel the operation.  Another option is: leftFront.set(0);
    leftFrontPidController.setReference(0, CANSparkMax.ControlType.kDutyCycle);
    if (!sparkFollow) {
      leftBackPidController.setReference(0, CANSparkMax.ControlType.kDutyCycle);
    }
  }

  public void cancelRightTarget() {
    // cancel the operation.  Another option is: rightFront.set(0);
    rightFrontPidController.setReference(0, CANSparkMax.ControlType.kDutyCycle);
    if (!sparkFollow) {
      rightBackPidController.setReference(0, CANSparkMax.ControlType.kDutyCycle);
    }
  }

  public void setLeftTarget(double target) {
    leftFrontPidController.setReference(target, CANSparkMax.ControlType.kPosition);
    if (!sparkFollow) {
      leftBackPidController.setReference(target, CANSparkMax.ControlType.kPosition);
    }
  }

  public void setRightTarget(double target) {
    rightFrontPidController.setReference(target, CANSparkMax.ControlType.kPosition);
    if (!sparkFollow) {
      rightBackPidController.setReference(target, CANSparkMax.ControlType.kPosition);
    }
  }

  public static void setPID(SparkMaxPIDController pidc,
        double p, double i, double d, double izone, double ff, double min, double max) {
    pidc.setP(p);
    pidc.setI(i);
    pidc.setD(d);
    pidc.setIZone(izone);
    pidc.setFF(ff);
    pidc.setOutputRange(min, max);
  }

  public void doInit() {
    /**
     * The restoreFactoryDefaults method can be used to reset the configuration parameters
     * in the SPARK MAX to their factory default state. If no argument is passed, these
     * parameters will not persist between power cycles
     */
    leftFront.restoreFactoryDefaults();
    rightFront.restoreFactoryDefaults();
    leftBack.restoreFactoryDefaults();
    rightBack.restoreFactoryDefaults();

    // zero rotations count for the encoder.
    leftFrontEncoder.setPosition(0);
    rightFrontEncoder.setPosition(0);
    leftBackEncoder.setPosition(0);
    rightBackEncoder.setPosition(0);

    /**
    * In CAN mode, one SPARK MAX can be configured to follow another. This is done by calling
    * the follow() method on the SPARK MAX you want to configure as a follower, and by passing
    * as a parameter the SPARK MAX you want to configure as a leader.
    */
    if (sparkFollow) {
      leftBack.follow(leftFront);
      rightBack.follow(rightFront);
    }

    leftFront.setInverted(true);
    if (!sparkFollow) {
      leftBack.setInverted(true);
    }

    // PID coefficients
    double kP, kI, kD, kIz, kFF, kMin, kMax;
    kP = 0.1;
    kI = 1e-4;
    kD = 1;
    kIz = 0;
    kFF = 0;
    kMin = -0.6;
    kMax = 0.6;

    setPID(leftFront.getPIDController(), kP, kI, kD, kIz, kFF, kMax, kMin);
    setPID(rightFront.getPIDController(), kP, kI, kD, kIz, kFF, kMax, kMin);
    if (!sparkFollow) {
      setPID(leftBack.getPIDController(), kP, kI, kD, kIz, kFF, kMax, kMin);
      setPID(rightBack.getPIDController(), kP, kI, kD, kIz, kFF, kMax, kMin);
    }

    // display PID coefficients on SmartDashboard
    // SmartDashboard.putNumber("P Gain", kP);
    // SmartDashboard.putNumber("I Gain", kI);
    // SmartDashboard.putNumber("D Gain", kD);
    // SmartDashboard.putNumber("I Zone", kIz);
    // SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("DMax Output", kMax);
    SmartDashboard.putNumber("DMin Output", kMin);
    SmartDashboard.putNumber("Set Rotations", 0);
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {
  /** Creates a new Drive subsystem. */


    private CANSparkMax leftFront = new CANSparkMax(1, MotorType.kBrushless);
    private CANSparkMax leftBack = new CANSparkMax(2, MotorType.kBrushless);
    private CANSparkMax rightFront = new CANSparkMax(3, MotorType.kBrushless);
    private CANSparkMax rightBack = new CANSparkMax(4, MotorType.kBrushless);
    private MotorControllerGroup leftGroup = new MotorControllerGroup(leftFront, leftBack);
    private MotorControllerGroup rightGroup = new MotorControllerGroup(rightFront, rightBack);
    private RelativeEncoder leftEncoder = leftFront.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    private RelativeEncoder rightEncoder = rightFront.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    private DifferentialDrive driveDifferential = new DifferentialDrive(leftGroup, rightGroup);

  public void arcadeDrive(DoubleSupplier speed, DoubleSupplier rotation) {
      driveDifferential.arcadeDrive(speed.getAsDouble(), rotation.getAsDouble());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Left Velocity", leftEncoder.getVelocity());
    SmartDashboard.putNumber("Right Velocity", rightEncoder.getVelocity());
    // SmartDashboard.putNumber("LimeLight", Limelight.d());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}

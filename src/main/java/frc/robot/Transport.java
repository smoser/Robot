package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Transport {

    public XboxController joystick1 = new XboxController(0);
    public XboxController joystick2 = new XboxController(1);

    public CANSparkMax leftMotor = new CANSparkMax(1, MotorType.kBrushless);
    public CANSparkMax rightMotor = new CANSparkMax(2, MotorType.kBrushless);

    DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);

    public void drive(){

        drive.arcadeDrive(joystick1.getLeftX() * 0.5f, joystick1.getLeftY() * 0.5f);

    }
}

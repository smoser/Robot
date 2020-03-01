/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Robot extends TimedRobot 
{
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  
  private XboxController joystick = new XboxController(0); 
  
  //motors controllers
  private TalonSRX left1 = new TalonSRX(2);
  private WPI_VictorSPX left2 = new WPI_VictorSPX(3);
  private TalonSRX right1 = new TalonSRX(4);
  private WPI_VictorSPX right2 = new WPI_VictorSPX(5);

  private CANSparkMax winchMotor = new CANSparkMax(12, MotorType.kBrushless);

  //drive
  private SpeedControllerGroup leftGroup = new SpeedControllerGroup(left2);
  private SpeedControllerGroup rightGroup = new SpeedControllerGroup(right2);
  private DifferentialDrive drive = new DifferentialDrive(leftGroup, rightGroup); 
  private boolean yeetMode = false;

  private PIDController pidController;

  //transport
  private Transport transport = new Transport();

  private Solenoid armLeft = new Solenoid(0);
  private Solenoid armRight = new Solenoid(1);
  private Solenoid hangerLeft = new Solenoid(2);
  private Solenoid hangerRight = new Solenoid(3);

  //limelight
  NetworkTable table;
  NetworkTableEntry tv;
  NetworkTableEntry tx;
  NetworkTableEntry ty;
  NetworkTableEntry ta;
  NetworkTableEntry pipeline;
  private int currentPipeline = 0;
  
  double hasTarget;
  double angleOffsetX; 
  double angleOffsetY; 
  double area; 
  
  public double limelightHeight = 2.3; //feet
  public double targetHeight = 7; //also feet
  public double limelightAngle = 0;

  //launcher helper variables, distance equals distance from the sun
  private double distance = 0;
  public double deltaHeight = 0;

  //These two components are the motor and PID controller for the shooter
  PWMTalonSRX pwmTalonSRX; 
  PIDSubsystem pidSubsystem;

  //The value of these three constants will need to be determined later
  double Kp = 0.04f; 
  double Kd = 0.0008f;
  double Ki = 0.0004f;
  double min_command = 0.03f;

  int neutralZone = 0;

  private boolean driverInputAccepted = true;

  //hanger
  public enum armState
  {
    start,
    releaseSpring,
    winchPull,
    stop
  }
  private armState state = armState.start;

  @Override
  public void robotInit() 
  {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
      
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tv = table.getEntry("tv");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");
    pipeline = table.getEntry("pipeline");

    deltaHeight = targetHeight - limelightHeight;

    transport.Reset();

    pidController = new PIDController(Kp, Ki, Kd);
    pidController.setTolerance(0.3f);

    left1.setNeutralMode(NeutralMode.Brake);
    right1.setNeutralMode(NeutralMode.Brake);

    hangerLeft.set(false);
    hangerRight.set(false);
  }

  @Override
  public void robotPeriodic() 
  {
    transport.UpdateTransport();
    Drive();
    CheckArm();
    //SearchX();
    PIDSearch();
    ArmControl();
    ControlLimelight();
    CalculateDistance();
  }

  private void ControlLimelight()
  {
    if (joystick.getBButtonPressed())
    {
      currentPipeline += 1;
      if (currentPipeline > 2)
      {
        currentPipeline = 0;
      }
      pipeline.setDouble(currentPipeline);
    }

    if (hasTarget >= 0.1f)
    {
      CalculateDistance();
    }
    else
    {
      distance = -1;
    }
    SmartDashboard.putNumber("Distance", distance);

    //read values periodically
    hasTarget = tv.getDouble(0.0);
    angleOffsetX = tx.getDouble(0.0);
    angleOffsetY = ty.getDouble(0.0);
    area = ta.getDouble(0.0);

    SmartDashboard.putNumber("HasTarget", hasTarget);

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", angleOffsetX);
    SmartDashboard.putNumber("LimelightY", angleOffsetY);
    SmartDashboard.putNumber("LimelightArea", area);
    SmartDashboard.putNumber("Current Pipeline", pipeline.getDouble(0.0));
  }

  private void Drive()
  {
    if (driverInputAccepted)
    {
      if (joystick.getYButtonPressed())
      {
        yeetMode = !yeetMode;
      }
      if (yeetMode)
      {
        drive.arcadeDrive(-joystick.getY(Hand.kLeft) * 0.5f, joystick.getX(Hand.kLeft) * 0.5f, true);
      }
      else
      {
        drive.arcadeDrive(-joystick.getY(Hand.kLeft) * 0.75f, joystick.getX(Hand.kLeft) * 0.75f, true);
      }
      left1.set(ControlMode.PercentOutput, left2.getMotorOutputPercent());
      right1.set(ControlMode.PercentOutput, right2.getMotorOutputPercent());
    }

    if(joystick.getBumper(Hand.kRight))
    {
      transport.IntakeForward(true);
    }
    else if(joystick.getBumper(Hand.kLeft))
    {
      transport.IntakeBackward(true);
    }
    else
    {
      transport.IntakeBackward(false);
    }
    if(joystick.getStartButtonPressed())
    {
      transport.Reset();
    }
  }

  private void Rotate(double turn)
  {
    double min = 0.4f;
  
    if(turn > 1)
    {
      turn = 1;
    }
    if(turn < -1)
    {
      turn = -1;
    }
    if(turn < min && turn > 0)
    {
      turn = min; 
    }
    if (turn > -min && turn < 0)
    {
      turn = -min;
    }

    SmartDashboard.putNumber("turn", turn);
    drive.arcadeDrive(0, turn, true);
  }

  private void CheckArm()
  {  
    SmartDashboard.putString("Arm State", state.toString());
    //Create timer so arm does not activate until endgame

    if(joystick.getAButtonPressed() && state == armState.start)
    {
      state = armState.releaseSpring; 
      hangerLeft.set(true);
      hangerRight.set(true);
    }
    if(joystick.getAButtonPressed() && state == armState.releaseSpring)
    {
      state = armState.winchPull;
      winchMotor.set(0.1f);
    }

    if(joystick.getAButtonPressed() && state == armState.winchPull)
    {
      winchMotor.stopMotor();
    }
  }

  private void CalculateDistance()
  {
    distance = deltaHeight / (Math.tan(Math.toRadians(limelightAngle + angleOffsetY)));
  }

  private double Velocity()
  {
    double launchVelocity = Math.sqrt(-16 * Math.pow(distance , 2) / Math.pow(Math.cos(Math.toRadians(limelightAngle + angleOffsetY)) , 2) * (deltaHeight - Math.tan(Math.toRadians(limelightAngle + angleOffsetY) * distance)));
    SmartDashboard.putNumber("velocity", launchVelocity);
    return launchVelocity;
  }

  public void PIDSearch()
  {
    if (joystick.getTriggerAxis(Hand.kLeft) > 0 && hasTarget == 1 && Math.abs(angleOffsetX) > 1)
    {
      double scale = 0.65f;
      if(Math.abs(angleOffsetX) < 8 && neutralZone < 10)
      {
        //improvement if angleOffsetX changes we are moving
        Rotate(0.05f *angleOffsetX);
        neutralZone++;
        return;
      }
      if(Math.abs(angleOffsetX) < 8 && neutralZone > 10)
      {
        scale = 0.3f;
      }

      driverInputAccepted = false;
      neutralZone++;
      double signal = -scale * pidController.calculate(angleOffsetX, 0);
      Rotate(signal);
    }
    else
    {
      driverInputAccepted = true;
      neutralZone = 0;
    }
    if(joystick.getTriggerAxis(Hand.kRight) > 0.5f)
    {
      transport.StartLaunch(Velocity());
    }
    else
    {
      transport.StopLaunch();
    }
  }
  
  public void SearchX()
  {
      if (joystick.getTriggerAxis(Hand.kRight) > 0 && hasTarget == 1)
      {
          driverInputAccepted = false;
          double heading_error = angleOffsetX;
          double steering_adjust = 0.0f;
          if (Math.abs(angleOffsetX) > 1f)
          {
                  steering_adjust = 0.05f * heading_error - min_command;
          }
          else if (Math.abs(angleOffsetX) < 1f)
          {
                  steering_adjust = 0.05f * heading_error + min_command;
          }
          Rotate(steering_adjust);
      }
      else
      {
        driverInputAccepted = true;
      }
  }

  private void ArmControl()
  {
    if(joystick.getXButtonPressed())
    {
      armLeft.set(!armLeft.get());
      armRight.set(!armRight.get());
    }
  }

  @Override
  public void autonomousInit() 
  {
    m_autoSelected = m_chooser.getSelected();

    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }


  @Override
  public void autonomousPeriodic() 
  {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        //AutonSearch();
        break;
    }
  }

  private void AutonSearch()
  {
    if (hasTarget > 0.1f)
    {
      if (Math.abs(angleOffsetX) > 1)
      {
        double scale = 0.65f;
        if(Math.abs(angleOffsetX) < 8 && neutralZone < 10)
        {
          //improvement if angleOffsetX changes we are moving
          Rotate(0.05f *angleOffsetX);
          neutralZone++;
          return;
        }
        if(Math.abs(angleOffsetX) < 8 && neutralZone > 10)
        {
          scale = 0.3f;
        }

        driverInputAccepted = false;
        neutralZone++;
        double signal = -scale * pidController.calculate(angleOffsetX, 0);
        Rotate(signal);
      }
      else
      {
        transport.StartLaunch(Velocity());
        
        Timer timer = new Timer();
        timer.delay(3);
        transport.StopLaunch();
        drive.arcadeDrive(0, 0.04f, true);
        timer.delay(2);
        drive.arcadeDrive(-0.5f, 0, true);

      }
    }
    else
    {
      drive.arcadeDrive(0, 0.04f, true);
    }
  } 

  @Override
  public void teleopPeriodic()
  {

  }

  @Override
  public void testPeriodic()
  {

  }
}


// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.AutoIndex;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.TankDriveRobot;
import frc.robot.commands.RunIntake;
import frc.robot.commands.ReverseIntake;
import frc.robot.commands.LaunchGroup;
import frc.robot.commands.Shoot;
import frc.robot.commands.SwitchAngle;
import frc.robot.commands.SwitchIntakeSolenoid;
import frc.robot.commands.UpdateDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeSolenoid;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.LimelightSim;
import frc.robot.subsystems.Sensors;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Index;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private static RobotContainer robotContainer = new RobotContainer();
  private final Drive m_drive = new Drive();
  private final Intake m_intake = new Intake();
  private final Launch m_launch = new Launch();
  private final Index m_index = new Index();
  private final Sensors m_sensors = new Sensors();
  private final IntakeSolenoid m_intakeSolenoid = new IntakeSolenoid();
  private final AutonomousCommand m_autoCommand;
  
  private XboxController controller1 = new XboxController(0);
  private XboxController controller2 = new XboxController(1);
  private XboxController controller3 = new XboxController(2);

  private Limelight m_limelight;

  public Sensors getSensors(){
    return m_sensors;
  }

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  private RobotContainer() {
    if (RobotBase.isReal()) {
        m_limelight = new Limelight();
    } else {
        m_limelight = new LimelightSim();
    }

    m_drive.setDefaultCommand(new TankDriveRobot(m_drive, controller1::getLeftY, controller2::getLeftY));

    m_autoCommand = new AutonomousCommand(m_launch, m_intakeSolenoid, m_index, m_drive, m_limelight, m_intake);
  
    m_launch.doInit();

    CameraServer.startAutomaticCapture();

    m_drive.doInit();

    // Configure the button bindings
    configureButtonBindings();

    m_index.setDefaultCommand(new AutoIndex(m_index, controller3::getLeftY));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    JoystickButton a = new JoystickButton(controller1, 1);
    JoystickButton a2 = new JoystickButton(controller2, 1);
    JoystickButton x3 = new JoystickButton(controller3, XboxController.Button.kX.value);
    JoystickButton b3 = new JoystickButton(controller3, XboxController.Button.kB.value);
    JoystickButton y3 = new JoystickButton(controller3, XboxController.Button.kY.value);
    JoystickButton rb3 = new JoystickButton(controller3, XboxController.Button.kRightBumper.value);
    JoystickButton lb3 = new JoystickButton(controller3, XboxController.Button.kLeftBumper.value);

    a.whileHeld(new RunIntake(m_intake));
    lb3.whileHeld(new LaunchGroup(m_launch, m_index, m_drive, m_limelight));
    x3.whenPressed(new SwitchIntakeSolenoid(m_intakeSolenoid));
    b3.whenPressed(new SwitchAngle(m_launch));
    y3.whileHeld(new ReverseIntake(m_intake));
    // shoot independent of limelight.
    rb3.whileHeld(new Shoot(m_launch, m_index));
  }

 
  public static RobotContainer getInstance(){
    return robotContainer;
  }

 /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_autoCommand;
  }

  public XboxController getController1(){
    return controller1;
  }

  public XboxController getController2(){
    return controller2;
  }

  public XboxController getController3(){
    return controller3;
  }
}

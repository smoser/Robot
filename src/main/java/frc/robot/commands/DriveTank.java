// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import java.util.function.DoubleSupplier;

/** An example command that uses an example subsystem. */
public class DriveTank extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Drive m_subsystem;

  private final DoubleSupplier m_lSpeed;
  private final DoubleSupplier m_rSpeed;
  private final double m_pOutput;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public DriveTank(Drive subsystem, DoubleSupplier lSpeed, DoubleSupplier rSpeed, Double pOutput) {
    m_subsystem = subsystem;
    m_lSpeed = lSpeed;
    m_rSpeed = rSpeed;
    m_pOutput = pOutput;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    m_subsystem.setTankDrive(m_lSpeed, m_rSpeed, m_pOutput);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

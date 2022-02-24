// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Index;


public class AutoIndex extends CommandBase {
  private final Index m_subsystem;
  private DoubleSupplier manualInput;

  /**
   *
   *
   * @param subsystem The subsystem used by this command.
   */
  public AutoIndex(Index subsystem, DoubleSupplier input) {
    m_subsystem = subsystem;
    manualInput = input;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if(manualInput.getAsDouble() > 0.1 || manualInput.getAsDouble() < -0.1){
      m_subsystem.manualIndex(manualInput);
    }
    else{
      m_subsystem.state();
    }
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

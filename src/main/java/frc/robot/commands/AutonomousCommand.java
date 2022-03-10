package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeSolenoid;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Limelight;

public class AutonomousCommand extends SequentialCommandGroup {
    
    public AutonomousCommand(Launch launch, IntakeSolenoid intakeSolenoid, Index index, Drive drive, Limelight limelight, Intake intake){
        
        addCommands(
            new DriveTime(drive, 1.5, .6),
            new SwitchIntakeSolenoid(intakeSolenoid),
            new RunIntake(intake),
            new DriveTime(drive, 2.0, .6),
            new Align(drive, limelight),
            new Shoot(launch, index, limelight)
        );
    }

}

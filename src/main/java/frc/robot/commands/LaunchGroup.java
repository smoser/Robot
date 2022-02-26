package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Limelight;

public class LaunchGroup extends SequentialCommandGroup {
    
    public LaunchGroup(Launch launch, Index index, Drive drive, Limelight limelight){
        
        addCommands(
            new Align(drive, limelight),
            new Shoot(launch, index, limelight)
        );
    }

}

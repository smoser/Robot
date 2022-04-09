package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeSolenoid;
import frc.robot.subsystems.Launch;
import frc.robot.subsystems.Limelight;

public class TurtleAuton extends SequentialCommandGroup {
    
    public TurtleAuton(Launch launch, IntakeSolenoid intakeSolenoid, Index index, Drive drive, Limelight limelight, Intake intake){
        
        addCommands(
            new DriveDistance(drive, 60.0, 1.0),//drive between 59 and 61 inches
            new TurnDegrees(drive, 180.0, 0), //turn around
            new DriveDistance(drive, 60.0, 1.0), //drive back
            new TurnDegrees(drive, -180.0, 0), //test turning the other direction, should be in start pos
            new DriveDistance(drive, 36),//drive forward about 3 feet
            new DriveDistance(drive, -36)//reverse, should be at start pos
        );
    }
}

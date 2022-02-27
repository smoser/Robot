package frc.robot.subsystems;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class LimelightSim extends Limelight {

    private NetworkTable table;

    public LimelightSim() {
        table = NetworkTableInstance.getDefault().getTable("limelightSim");
        SmartDashboard.putNumber("Limelight X", 9);
        SmartDashboard.putNumber("Limelight Y", 9);
        SmartDashboard.putNumber("Limelight TA", 9.9);
        SmartDashboard.putNumber("Limelight Target", 1);
    }

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
      SmartDashboard.putNumber("Limelight Distance", distance());
    }

    public double tv() { //gets x from the limelight
        return SmartDashboard.getNumber("Limelight Target", 9);
    }

    public double tx() { //gets x from the limelight
        return SmartDashboard.getNumber("Limelight X", 9);
    }

    public double ty() { //gets y from limelight
        return SmartDashboard.getNumber("Limelight Y", 9);
    }

    public double ta() { //gets the area from the limelight
        return SmartDashboard.getNumber("Limelight TA", 9.9);
    }
}


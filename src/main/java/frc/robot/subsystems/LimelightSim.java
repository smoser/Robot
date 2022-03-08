package frc.robot.subsystems;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class LimelightSim extends Limelight {

    private NetworkTable table;

    public LimelightSim() {
        table = NetworkTableInstance.getDefault().getTable("limelightSim");
        SmartDashboard.putNumber("LL X", 9);
        SmartDashboard.putNumber("LL Y", 9);
        SmartDashboard.putNumber("LL TA", 9.9);
        SmartDashboard.putNumber("LL Target", 1);
    }

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
      SmartDashboard.putNumber("LL Distance", distance());
      SmartDashboard.putNumber("LL ProjVel", projVelo());
      SmartDashboard.putNumber("LL ProjRPM", projRpm());
      SmartDashboard.putNumber("LL TablRPM", tableRpm());
    }

    public double tv() { //gets x from the limelight
        return SmartDashboard.getNumber("LL Target", 9);
    }

    public double tx() { //gets x from the limelight
        return SmartDashboard.getNumber("LL X", 9);
    }

    public double ty() { //gets y from limelight
        return SmartDashboard.getNumber("LL Y", 9);
    }

    public double ta() { //gets the area from the limelight
        return SmartDashboard.getNumber("LL TA", 9.9);
    }
}


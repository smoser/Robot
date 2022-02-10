package frc.robot.commands;

public class CalcEngine {

  // Everything in Meters
  public double launchHeight = 1.0f; // about the robot launch height
  public double targetHeight = 2.4384f; // hub high target height (8ft)
  public final double gravity = 9.8f;

  public CalcEngine(double targetHeight) {
    this.launchHeight = 0.0f;
    this.targetHeight = targetHeight;
  }

  public CalcEngine(double launchHeight, double targetHeight) {
    this.launchHeight = launchHeight;
    this.targetHeight = targetHeight;
  }

  public double getVelo(double distance, double angle) {
    return Math.sqrt(
        (gravity * distance * distance)
            / ((distance * Math.sin(2 * angle))
                - (2 * (targetHeight - launchHeight) * (Math.cos(angle) * Math.cos(angle)))));
  }
}

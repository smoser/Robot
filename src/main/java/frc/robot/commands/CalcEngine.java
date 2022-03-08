package frc.robot.commands;

public class CalcEngine {

  // Everything in Feet
  public double launchHeight;
  public double targetHeight;
  public final double gravity = 32.174f;

  /*
  public CalcEngine(double targetHeight) {
    this.launchHeight = 0.0f;
    this.targetHeight = targetHeight;
  }
  */

  public CalcEngine(double launchHeight, double targetHeight) {
    this.launchHeight = launchHeight;
    this.targetHeight = targetHeight;
  }

  public double getVelo(double distance, double angle) {
    double y = targetHeight - launchHeight;
    double num = gravity * distance * distance;
    double den1 = distance * Math.sin(2 * angle);
    double den2 = 2 * y * Math.cos(angle) * Math.cos(angle);
    return Math.sqrt(num /(den1 - den2));
  }
}

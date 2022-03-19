package frc.robot;

public class CalcEngine {

  // Everything in Feet, Angle in degrees.
  public double launchHeight;
  public double targetHeight;
  public double launchAngle;
  public double wheelDiameter;
  public final double gravity = 32.174f;

  public CalcEngine(double launchHeight, double targetHeight, double wheelDiameter) {
    this.launchHeight = launchHeight;
    this.targetHeight = targetHeight;
    this.wheelDiameter = wheelDiameter;
  }

  public double projVeloRadians(double distance, double angle) {
    double y = targetHeight - launchHeight;
    double num = gravity * distance * distance;
    double den1 = distance * Math.sin(2 * angle);
    double den2 = 2 * y * Math.cos(angle) * Math.cos(angle);
    return Math.sqrt(num /(den1 - den2));
  }

  // return projectile velocity in feet/sec
  // distance is distance on the x axis.
  public double projVelo(double distance, double launchAngle) {
    return projVeloRadians(distance, degreesToRadians(launchAngle));
  }

  // velocity in feet per second, rpm in revolutions per minute.
  public double veloToRpm(double velocity) {
    return velocity * 60 / (wheelDiameter * Math.PI);
  }

  public double projRpm(double distance, double launchAngle) {
      return veloToRpm(projVelo(distance, launchAngle));
  }

  // return the rpm expected based on distance from a table.
  public double tableRpm(double distance) {
    return distance * 82.738 + 143.2;
  }

  public static double degreesToRadians(double degrees) {
      return degrees * Math.PI / 180;
  }

}

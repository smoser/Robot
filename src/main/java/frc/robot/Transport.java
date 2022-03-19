
package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Transport
{
    // private boolean launching = false;

    State state = State.initial;

    //private boolean lastFirstValue = false;
    //private boolean lastInitialSensor = false;
    //private boolean lastMiddleSensor = false;
    
    //first = mechanum + roller, second = 2" wheels

    //launcher motors

    //controllers
    XboxController joystick1;
    XboxController joystick2;

    double timer = 100;
    
    double manualLaunch = -1;
    int lastPOV = -1;

    public Transport() { }
    public Transport (XboxController one, XboxController two)
    {
        joystick1 = one;
        joystick2 = two;
    }

    public void InitTransport(){}

    public void UpdateTransport(){ }

    private void GetInput(){ }

    public void Reset()
    {
        state = State.initial;
    }
   
    public void StartLaunch(double percent){}
 
    public void StopLaunch(){}
    public enum State {initial, oneBall, twoBall, threeBall}
    public void ProcessIO() { }
  
    public void GetLastValues() { }









}

package com.ra;



import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class MyRobotV3 extends AdvancedRobot{
	double bulletPower;
	private NearByBot nearBot = null;
	private double lastEnergy;
	private double lastTime;
	double moveDir=1;
	public void run()
	{
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		while(true)
		{
			setTurnRadarRight(1000);
			execute();
		}
	}
	 public void onScannedRobot(ScannedRobotEvent e) {
		 System.out.println("scanned"+e.getName());
		   
			if(nearBot == null)
			{
				nearBot = new NearByBot(e.getName(),e.getDistance(),e.getVelocity(),e.getEnergy(),
						e.getBearing(),e.getHeading());			
			}
			else if(nearBot.nearBotName != e.getName() && nearBot.nearBotDist > e.getDistance())
			{
				nearBot = new NearByBot(e.getName(),e.getDistance(),e.getVelocity(),e.getEnergy(),
						e.getBearing(),e.getHeading());
				
			}
			if(nearBot.nearBotName == e.getName()){
			  setTurnGunRight(Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getGunHeading()));
			  double bulletPower = 400 / e.getDistance(); 
		//	  fire(bulletPower); 
			  if (getEnergy() - e.getEnergy() > 50) {
				  System.out.println("e.getDistance()"+e.getDistance());
				  setAhead(moveDir*e.getDistance());
			  }
			  fire(bulletPower); 
	 }
	 }
	 public void onRobotDeath(RobotDeathEvent e)
	 {
		 if(nearBot!=null&&e.getName()==nearBot.nearBotName)
		 {
			 nearBot = null;
		 }
	 }
	 public void onHitWall(HitWallEvent e)
	 {
		 moveDir = moveDir*-1;
	 }
	 public void onHitByBullet(HitByBulletEvent e) {
		 System.out.println("GetEnergy"+getEnergy()+"lastentergy"+lastEnergy);
		 System.out.println("Time"+getTime()+"lastentergy"+lastTime);
		 if(lastEnergy - getEnergy() > 10 )//&& getTime()-lastTime < 10 )
		 {
			 fire(1); 
			 setAhead((Math.random() - 0.50) * 400); 
		 }
		 lastEnergy = getEnergy();
		 lastTime = getTime();
		 
	 }
	 private void goTo(int x, int y) {
		    double a;
		    setTurnRightRadians(Math.tan(
		        a = Math.atan2(x -= (int) getX(), y -= (int) getY()) 
		              - getHeadingRadians()));
		    setAhead(Math.hypot(x, y) * Math.cos(a));
		}
	 class NearByBot{
			
		 private String nearBotName; 
		 private double nearBotDist;
		 private double nearBotVelocity;
		 private double nearBotEnergy;
		 private double nearBotBearing;
		 private double nearBotHeading;
		 public NearByBot(String botName, double botDistance, double botVelocity, double botEnergy,
				double botBearing, double botHeading ) {
			// TODO Auto-generated constructor stub
			 nearBotName = botName;
			 nearBotDist = botDistance;
			 nearBotVelocity = botVelocity;
			 nearBotEnergy = botEnergy;
			 nearBotBearing = botBearing;
			 nearBotHeading = botHeading;			 
		}

	 }
}

package Orange.Team6;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class RamaAllu extends AdvancedRobot{
	private NearByBot nearBot = null;
	private double lastEnergy;
	double moveDir;
	
	public void run()
	{
		setBodyColor(Color.BLACK);
		setBulletColor(Color.ORANGE);
		setGunColor(Color.RED);
		//Adjust the Gun and Radar to run independently		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		moveDir= 1;
		while(true)
		{
			setTurnRadarRight(1000); //turn the radar
			execute();
		}
	}
	/**
	 * onScannedRobot:  Hit the nearest target.
	 */	
		 public void onScannedRobot(ScannedRobotEvent e) {
			 //Set the nearBot instance on the first scan
			 if(nearBot == null)
				{
					nearBot = new NearByBot(e.getName(),e.getDistance());			
				}
			 //On the next scan if there is a different robot which is near, update the instance.			 
				else if(nearBot.getNearBotName() != e.getName() && nearBot.getNearBotDist() > e.getDistance())
				{
					nearBot = new NearByBot(e.getName(),e.getDistance());
					
				}
			 //*If it is the near by bot			 
				if(nearBot.getNearBotName() == e.getName()){
					//Set the Gun direction to the enemy					
				  setTurnGunRight(Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getGunHeading()));
				  //Calculate the bullet power based on the distance				  
				  double bulletPower = 400 / e.getDistance(); 
				  //If our Robo energy is higher than	enemy robo move ahead 			  
				  if (getEnergy() - e.getEnergy() > 40) {
					  setAhead(moveDir*e.getDistance());
				  }
				  //Fire the bullet				  
				  fire(bulletPower); 
		 }
		 }
		 
		 public void onRobotDeath(RobotDeathEvent e)
		 {
			 //Clear the instance on the near by bot death			 
			 if(nearBot!=null&&e.getName()==nearBot.getNearBotName())
			 {
				 nearBot = null;
			 }
		 }
		 public void onHitWall(HitWallEvent e)
		 {
			 //Reverse direction
			 moveDir = moveDir*-1;
		 }		 
		 public void onHitByBullet(HitByBulletEvent e) {
			 //If the energy is decreasing quickly move to a random position			 
			 if(lastEnergy - getEnergy() > 10 )//&& getTime()-lastTime < 10 )
			 {
				 fire(1); 
				 setAhead((Math.random() - 0.50) * 400); 
			 }
			 lastEnergy = getEnergy();
			 
			 
		 }
	
		 /*NearByBot class is used to store the instance of the nearBy enemy Bot*/
	 class NearByBot{
			
		 public String getNearBotName() {
			return nearBotName;
		}
		public double getNearBotDist() {
			return nearBotDist;
		}
		private String nearBotName; 
		 private double nearBotDist;
		 public NearByBot(String botName, double botDistance ) {
			// TODO Auto-generated constructor stub
			 nearBotName = botName;
			 nearBotDist = botDistance;		 
		}

	 }	
}

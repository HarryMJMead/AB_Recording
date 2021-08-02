/*****************************************************************************
 ** ANGRYBIRDS AI AGENT FRAMEWORK
 ** Copyright (c) 2014, XiaoYu (Gary) Ge, Stephen Gould, Jochen Renz
 **  Sahan Abeyasinghe,Jim Keys,  Andrew Wang, Peng Zhang
 ** All rights reserved.
**This work is licensed under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
**To view a copy of this license, visit http://www.gnu.org/licenses/
 *****************************************************************************/
package ab.demo;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.java_websocket.WebSocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import ab.demo.other.ActionRobot;
import ab.demo.other.ActionRobotRec;
import ab.demo.other.Shot;
import ab.planner.TrajectoryPlanner;
import ab.server.Proxy;
import ab.server.Proxy.ProxyResult;
import ab.utils.StateUtil;
import ab.utils.LogWriter;
import ab.vision.ABObject;
import ab.vision.ABType;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.Vision;

public class RecordAgent implements Runnable {

	public static Proxy proxy;
	private ActionRobotRec aRobot;
	private Random randomGenerator;
	public int currentLevel = 0;
	public int[] levels = {1, 3, 4, 5};
	public static int time_limit = 12;
	private Map<Integer,Integer> scores = new LinkedHashMap<Integer,Integer>();
	TrajectoryPlanner tp;
	private boolean firstShot = true;
	private Point prevTarget;
	private boolean recording = false;
	private boolean recordState = false;
	private boolean shooting = false;
	private boolean copyShot = false;
	private Point relShot;
	private int slingHeight;
	
	
	static {
		
	}
	
	// a standalone implementation of the Naive Agent
	public RecordAgent() {
		tp = new TrajectoryPlanner();

		
		if (proxy == null) {
			try {
				proxy = new Proxy(9000) {
					@Override
					public void onOpen() {
						System.out.println("Client connected");
					}

					@Override
					public void onClose() {
						System.out.println("Client disconnected");
					}
					
					@Override
				    public void onMessage(WebSocket conn, String message) {
				        JSONArray j = (JSONArray) JSONValue.parse(message);
				        Long id = (Long) j.get(0);
				        JSONObject data = (JSONObject) j.get(1);
				        
				        if (id == -1 && recording) {
				        	record(data);
				        } else {
				        
					        ProxyResult<?> result = results.get(id);
	
				            if (result != null) {
				                results.remove(id);
				                try {
				                    result.queue.put(result.message.gotResponse(data));
				                } catch (InterruptedException e) {
	
				                    e.printStackTrace();
				                }
				            }
				        }
			            
				        //System.out.println("ID: " + id);
				        //System.out.println("Data: " + imageBytes);

				    }
				};
				proxy.start();

				System.out
						.println("Server started on port: " + proxy.getPort());

				System.out.println("Waiting for client to connect");
				proxy.waitForClients(1);

			} catch (UnknownHostException e) {

				e.printStackTrace();
			}
		}
		
		System.out.println(proxy);
		aRobot = new ActionRobotRec(proxy);
		
	}


	// run the client
	public void run1() {
		System.out.println("Atleast it runs");
		
		ActionRobotRec.fullyZoomOut();
		BufferedImage screenshot = ActionRobotRec.doScreenShot();
		Vision vision = new Vision(screenshot);
		Rectangle sling = vision.findSlingshotMBR();
		
		slingHeight = sling.height;
		
		System.out.println(slingHeight);
		
		while (true) {
			screenshot = ActionRobotRec.doScreenShot();
			vision = new Vision(screenshot);
			sling = vision.findSlingshotMBR();
			
			if (sling != null) {
				double scaleFactor = (double)sling.height/(double)slingHeight;
				
				if (scaleFactor > 1) {
					System.out.println("ScaleFactor: " + scaleFactor);
					ActionRobotRec.fullyZoomOut();
				}
			}
			//takeShot(new Point(-5, 5));
	        
	        
			if (shooting) {
				System.out.println(relShot);
				takeShot(relShot);
				
				shooting = false;
			}
			
		}

	}
	
	public void run() {
		System.out.println("Atleast it runs");
		
		
		//aRobot.loadLevel(currentLevel);
		while (true) {
			GameState state = aRobot.getState();			
			
			if (state == GameState.PLAYING) {
				recording = true;
				BufferedImage screenshot = ActionRobotRec.doScreenShot();
				Vision vision = new Vision(screenshot);
				Rectangle sling = vision.findSlingshotMBR();
				
				// Record Initial Sling Height
				if (firstShot) {
					ActionRobotRec.fullyZoomOut();
					
					slingHeight = sling.height;
					System.out.println("Recorded Sling Height: " + slingHeight);
					
					firstShot = false;
					recordState = true;
				}
				
				// Check to see if Zoomed in
				if (sling != null) {
					double scaleFactor = (double)sling.height/(double)slingHeight;
					
					if (scaleFactor > 1) {
						System.out.println("ScaleFactor: " + scaleFactor);
						ActionRobotRec.fullyZoomOut();
					}
				}
				
				//Record Gamestate after shot
				if (recordState) {
					List<ABObject> pigs = vision.findPigsMBR();
					final List<ABObject> birds = vision.findBirdsRealShape();
					final List<ABObject> hills = vision.findHills();
					final List<ABObject> blocks = vision.findBlocksRealShape();
					
					System.out.println("Pigs: " + pigs + ", Birds: " + birds + ", Hills: " + hills + ", Blocks: " + blocks);
					recordState = false;
				}
				
			} else {
				recording = false;
				firstShot = true;
				
				if (state == GameState.LOST || state == GameState.WON) {
					System.out.println("Next Level: " + levels[currentLevel + 1]);
					aRobot.loadLevel(levels[++currentLevel]);
				} else if (state == GameState.LEVEL_SELECTION) {
					System.out
					.println("Unexpected level selection page, go to the last current level : "
							+ levels[currentLevel]);
					aRobot.loadLevel(levels[currentLevel]);
				} else if (state == GameState.MAIN_MENU) {
					System.out
					.println("Unexpected main menu page, go to the last current level : "
							+ levels[currentLevel]);
					ActionRobotRec.GoFromMainMenuToLevelSelection();
					aRobot.loadLevel(levels[currentLevel]);
				} else if (state == GameState.EPISODE_MENU) {
					System.out
					.println("Unexpected episode menu page, go to the last current level : "
							+ levels[currentLevel]);
					ActionRobotRec.GoFromMainMenuToLevelSelection();
					aRobot.loadLevel(levels[currentLevel]);
				}
			}

		}

	}
	

	private double distance(Point p1, Point p2) {
		return Math
				.sqrt((double) ((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)
						* (p1.y - p2.y)));
	}

	public void record(JSONObject data)
	{
		//Handles Clicks
		JSONArray clicks = (JSONArray) data.get("click");
		
		Point mDown = new Point((int) (long) clicks.get(0), (int) (long) clicks.get(1));
		Point mUp = new Point((int) (long) clicks.get(2), (int) (long) clicks.get(3));
		
		//System.out.println("Clicks: " + clicks);
		
		//Handles Screenshot
        String imageStr = (String) data.get("screenshot");
        imageStr = imageStr.split(",", 2)[1];
        byte[] imageBytes = Base64.decodeBase64(imageStr);
        
        BufferedImage screenshot = null;
		try {
			ByteArrayInputStream testing = new ByteArrayInputStream(imageBytes);
			
			screenshot = ImageIO.read(testing);
			
		} catch (IOException e) {

		}
		
		
		// process image
		Vision vision = new Vision(screenshot);

		// find the slingshot
		Rectangle sling = vision.findSlingshotMBR();

		// confirm the slingshot
		
//		if (sling == null && StateUtil.getGameState(proxy) == GameState.PLAYING) {
//			System.out.println("No slingshot detected. Please remove pop up or zoom out");
//
//		}
		// get all the pigs

		//GameState state = StateUtil.getGameState(proxy);

		//creates the logwriter that will be used to store the information about turns
		//final LogWriter log = new LogWriter("output_bad_only.csv");


		// if there is a sling, then play, otherwise just skip.
		if (sling != null) {
			//System.out.println("Sling at - x: " + sling.x + ", y: " + sling.y);
			
			Point refPoint = tp.getReferencePoint(sling);
			
			int dx = (int)mUp.getX() - refPoint.x;
			int dy = (int)mUp.getY() - refPoint.y;
			
			//System.out.println(distance(refPoint, mDown));
			
			if (distance(refPoint, mDown) < slingHeight*0.4) {
				System.out.println("Shot - dx: " + dx + ", dy: " + dy);
				relShot = new Point(dx, dy);
				
				try {
					Thread.sleep(7000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				recordState = true;
				
				if (copyShot) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println("waited");
					shooting = true;
					System.out.println(shooting);
				}
			}
		}
	}
	
	public void takeShot(Point relReleasePoint) {
		recording = false; 
		ActionRobotRec.fullyZoomOut();
		BufferedImage screenshot = ActionRobotRec.doScreenShot();
		Vision vision = new Vision(screenshot);
		Rectangle sling = vision.findSlingshotMBR();
		
		
		if(sling != null)
		{
			Point refPoint = tp.getReferencePoint(sling);
			
			Shot shot = new Shot(refPoint.x, refPoint.y, relReleasePoint.x, relReleasePoint.y, 0, 0);
			aRobot.cshoot(shot);
			
			
		} else {
			System.out.println("no sling detected, can not execute the shot, will re-segement the image");
		}
		
		recording = true;
	}

	public static void main(String args[]) {

		NaiveAgent na = new NaiveAgent();
		if (args.length > 0)
			na.currentLevel = Integer.parseInt(args[0]);
		na.run();

	}
}

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
import ab.vision.GameStateExtractor;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.Vision;

import com.mongodb.*;

public class RecordAgent implements Runnable {

	public static Proxy proxy;
	private ActionRobotRec aRobot;
	private Random randomGenerator;
	public int currentLevel = 0;
	public int[] levels = {1, 4, 5, 11, 17, 19};
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
	
	private int shotWait = 4000;
	
	private long shotTime;
	private int lastTapTime = 0;
	private long jShotTime = -shotWait;
	
	private MongoClient mongoClient;
	private DBObject run;
	private List<Integer> x_list;
	private List<Integer> y_list;
	private List<Integer> tap_list;
	private List<Integer> pigs_list;
	private List<Integer> birds_list;
	private DBCollection collection;
	
	private Vision lastVision;
	private Vision savedVision;
	
	private long playerID = System.currentTimeMillis();
	
	static {
		
	}
	
	// a standalone implementation of the Naive Agent
	public RecordAgent() {
		//Set up proxy message handling
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
				        	//Record shot on click up
				        	record(data);
				        } else if (id == -2 && recording) {
				        	//Save previous game state on click down
				        	savedVision = lastVision;
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
				    }
				};
				proxy.start();

				System.out.println("Server started on port: " + proxy.getPort());

				System.out.println("Waiting for client to connect");
				proxy.waitForClients(1);

			} catch (UnknownHostException e) {

				e.printStackTrace();
			}
		}
		
		//Set up ActionRobot/Trajectory Planner
		aRobot = new ActionRobotRec(proxy);
		tp = new TrajectoryPlanner();
		
		//Connect to MongoDB
		try {
			mongoClient =new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DB database = mongoClient.getDB("AngryBirds");
		collection = database.getCollection("HumanAgent");
		
	}

	
	public void run() {
		System.out.println("PlayerID: " + playerID);
		System.out.println("Loading Level");
		setRunClear();
		
		
		//aRobot.loadLevel(currentLevel);
		while (true) {
			BufferedImage screenshot = ActionRobotRec.doScreenShot();	
			GameStateExtractor gameStateExtractor = new GameStateExtractor();
	        GameState state = gameStateExtractor.getGameState(screenshot);
			
			if (state == GameState.PLAYING) {
				recording = true;
				lastVision = new Vision(screenshot);
				Rectangle sling = lastVision.findSlingshotMBR();
				
				
				// Record Initial Sling Height
				if (firstShot) {
					System.out.println("Recording Inital State. Please wait before taking first shot.");
					recording = false;
					
					ActionRobotRec.fullyZoomOut();
					screenshot = ActionRobotRec.doScreenShot();	
					lastVision = new Vision(screenshot);
					
					sling = lastVision.findSlingshotMBR();
					slingHeight = sling.height;
					//System.out.println("Recorded Sling Height: " + slingHeight);
					System.out.println("Start when ready");
					
					firstShot = false;
					recording = true;
				}
				
				
				// Check to see if Zoomed in
				if (sling != null) {
					double scaleFactor = (double)sling.height/(double)slingHeight;
					
					if (scaleFactor > 1) {
						System.out.println("ScaleFactor: " + scaleFactor);
						ActionRobotRec.fullyZoomOut();
					}
				}
				
				
			} else {
				recording = false;
				
				if (state == GameState.LOST || state == GameState.WON) {
					RecordState(lastVision);
					fillRun();
					collection.insert(run);
					
					if (currentLevel + 1 >= levels.length) {
						break;
					}
					
					System.out.println("Loading Next Level: " + levels[currentLevel + 1]);
					aRobot.loadLevel(levels[++currentLevel]);
				} else if (state == GameState.LEVEL_SELECTION) {
					System.out
					.println("Unexpected level selection page, loading current level : "
							+ levels[currentLevel]);
					aRobot.loadLevel(levels[currentLevel]);
				} else if (state == GameState.MAIN_MENU) {
					System.out
					.println("Unexpected main menu page, loading last current level : "
							+ levels[currentLevel]);
					ActionRobotRec.GoFromMainMenuToLevelSelection();
					aRobot.loadLevel(levels[currentLevel]);
				} else if (state == GameState.EPISODE_MENU) {
					System.out
					.println("Unexpected episode menu page, loading last current level : "
							+ levels[currentLevel]);
					ActionRobotRec.GoFromMainMenuToLevelSelection();
					aRobot.loadLevel(levels[currentLevel]);
				}
				
				firstShot = true;
				relShot = null;
				jShotTime = -shotWait;
				setRunClear();
			}

		}
		
		System.out.println("Completed all levels. Thank You!");

	}
	
	private void RecordState(Vision vision) {
		//Record the current game state
		
		List<ABObject> pigs = vision.findPigsMBR();
		List<ABObject> birds = vision.findBirdsRealShape();
		List<ABObject> hills = vision.findHills();
		List<ABObject> blocks = vision.findBlocksRealShape();
		
		if (relShot == null) {
			x_list.add(null);
			y_list.add(null);
			tap_list.add(null);
		} else {
			x_list.add(relShot.x);
			y_list.add(relShot.y);
			tap_list.add(lastTapTime);
		}
		
		pigs_list.add(pigs.size());
		birds_list.add(birds.size());
		
		//System.out.println("Shot: "+ relShot +", TapTime: "+ lastTapTime + ", Pigs: " + pigs.size() + ", Birds: " + birds.size());
		
	}

	private double distance(Point p1, Point p2) {
		return Math
				.sqrt((double) ((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)
						* (p1.y - p2.y)));
	}
	
	private void setRunClear(){
		run = new BasicDBObject("level", levels[currentLevel]);
		x_list = new ArrayList<>();
		y_list = new ArrayList<>();
		tap_list = new ArrayList<>();
		pigs_list = new ArrayList<>();
		birds_list = new ArrayList<>();
	}

	private void fillRun(){
		run.put("playerID", playerID);
		run.put("xVals", x_list);
		run.put("yVals", y_list);
		run.put("tapVals", tap_list);
		run.put("pigsNum", pigs_list);
		run.put("birdsNum", birds_list);
	}


	public void record(JSONObject data)
	{
		//Gets Time
		Long time = (Long) data.get("time");
		//System.out.println(time);
		
		//Handles Clicks
		JSONArray clicks = (JSONArray) data.get("click");
		//System.out.println("Clicks: " + clicks);
		
		Point mDown = new Point((int) (long) clicks.get(0), (int) (long) clicks.get(1));
		Point mUp = new Point((int) (long) clicks.get(2), (int) (long) clicks.get(3));
		
		
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
			
		//process image
		Vision vision = new Vision(screenshot);

		//find the slingshot
		Rectangle sling = vision.findSlingshotMBR();

		
		//Record shot if sling can be found
		if (sling != null) {
			//System.out.println("Sling at - x: " + sling.x + ", y: " + sling.y);
			
			Point refPoint = tp.getReferencePoint(sling);
			
			int dx = (int)mUp.getX() - refPoint.x;
			int dy = (int)mUp.getY() - refPoint.y;
			
			//System.out.println(distance(refPoint, mDown));
			
			if (distance(refPoint, mDown) < slingHeight*0.4 && System.currentTimeMillis() - jShotTime > shotWait) {
				//System.out.println("Shot - dx: " + dx + ", dy: " + dy);
				
				//Record the game state before the shot was taken
				RecordState(savedVision);
				System.out.println("Recorded Shot");
				
				//Record the shot that was taken, relative to the sling
				relShot = new Point(dx, dy);
				
				//Record the current time to calculate tap time
				shotTime = time; //time from chrome
				lastTapTime = 0;
				jShotTime = System.currentTimeMillis(); //time from java
			}
			
			//Record the time of the next click after a shot
			if (lastTapTime == 0) {
				lastTapTime = (int)(time - shotTime);
			}
		}
		
		//Check if restart was clicked
		Point restart = new Point(100, 30);
		if (distance(restart, mDown) < 30) {
			firstShot = true;
			relShot = null;
			jShotTime = -shotWait;
			setRunClear();
			
			System.out.println("Restarted Level");
		}
	}
}

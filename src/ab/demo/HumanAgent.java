/*****************************************************************************
 ** ANGRYBIRDS AI AGENT FRAMEWORK
 ** Copyright (c) 2014, XiaoYu (Gary) Ge, Stephen Gould, Jochen Renz
 **  Sahan Abeyasinghe,Jim Keys,  Andrew Wang, Peng Zhang
 ** All rights reserved.
**This work is licensed under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
**To view a copy of this license, visit http://www.gnu.org/licenses/
 *****************************************************************************/
package ab.demo;

import ab.demo.other.ActionRobot;
import ab.demo.other.Shot;
import ab.planner.TrajectoryPlanner;
import ab.utils.LogWriter;
import ab.utils.StateUtil;
import ab.vision.ABObject;
import ab.vision.ABType;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.Vision;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.*;
import java.util.Scanner;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class HumanAgent implements Runnable {

	private ActionRobot aRobot;
	private Random randomGenerator;
	public int currentLevel = 1;
	public static int time_limit = 12;
	private Map<Integer,Integer> scores = new LinkedHashMap<Integer,Integer>();
	TrajectoryPlanner tp;
	private boolean firstShot;
	private Point prevTarget;
	private MongoClient mongoClient;
	private DBObject run;
	private List<Integer> x_list;
	private List<Integer> y_list;
	private List<Integer> tap_list;
	private List<Integer> pigs_list;
	private DBCollection collection;
	// a standalone implementation of the Human Agent
	public HumanAgent() {
		
		aRobot = new ActionRobot();
		tp = new TrajectoryPlanner();
		prevTarget = null;
		firstShot = true;
		randomGenerator = new Random();
		try {
			mongoClient =new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DB database = mongoClient.getDB("AngryBirds");
		collection = database.getCollection("HumanAgent");
		// --- go to the Poached Eggs episode level selection page ---
		ActionRobot.GoFromMainMenuToLevelSelection();

	}

	
	// run the client
	public void run() {

		aRobot.loadLevel(currentLevel);
		setRunClear();
		while (true) {
			GameState state = solve();
			if (state == GameState.WON) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int score = StateUtil.getScore(ActionRobot.proxy);
				if(!scores.containsKey(currentLevel))
					scores.put(currentLevel, score);
				else
				{
					if(scores.get(currentLevel) < score)
						scores.put(currentLevel, score);
				}
				int totalScore = 0;
				for(Integer key: scores.keySet()){

					totalScore += scores.get(key);
					System.out.println(" Level " + key
							+ " Score: " + scores.get(key) + " ");
				}
				System.out.println("Total Score: " + totalScore);
				aRobot.loadLevel(++currentLevel);
				fillRun();
				collection.insert(run);
				setRunClear();
				// make a new trajectory planner whenever a new level is entered
				tp = new TrajectoryPlanner();

				// first shot on this level, try high shot first
				firstShot = true;
			} else if (state == GameState.LOST) {
				System.out.println("Restart");
				fillRun();
				collection.insert(run);
				aRobot.restartLevel();
				setRunClear();
			} else if (state == GameState.LEVEL_SELECTION) {
				System.out
				.println("Unexpected level selection page, go to the last current level : "
						+ currentLevel);
				aRobot.loadLevel(currentLevel);
				setRunClear();
			} else if (state == GameState.MAIN_MENU) {
				System.out
				.println("Unexpected main menu page, go to the last current level : "
						+ currentLevel);
				ActionRobot.GoFromMainMenuToLevelSelection();
				aRobot.loadLevel(currentLevel);
				setRunClear();
			} else if (state == GameState.EPISODE_MENU) {
				System.out
				.println("Unexpected episode menu page, go to the last current level : "
						+ currentLevel);
				ActionRobot.GoFromMainMenuToLevelSelection();
				aRobot.loadLevel(currentLevel);
				setRunClear();
			}

		}

	}


	private void setRunClear(){
		run = new BasicDBObject("level", currentLevel);
		x_list = new ArrayList<>();
		y_list = new ArrayList<>();
		tap_list = new ArrayList<>();
		pigs_list = new ArrayList<>();
	}

	private void fillRun(){
		run.put("xVals", x_list);
		run.put("yVals", y_list);
		run.put("tapVals", tap_list);
		run.put("pigsNum", pigs_list);
	}

	private double distance(Point p1, Point p2) {
		return Math
				.sqrt((double) ((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)
						* (p1.y - p2.y)));
	}

	public GameState solve()
	{

		// capture Image
		BufferedImage screenshot = ActionRobot.doScreenShot();

		// process image
		Vision vision = new Vision(screenshot);

		// find the slingshot
		Rectangle sling = vision.findSlingshotMBR();


		System.out.println("Input X, Y and Boost Time(ms): ");
		Scanner scanner = new Scanner(System.in);
		String[] args = scanner.nextLine().split(" ");
		while(args.length != 3){
			System.out.println("Incorrect Input form. Please try again.");
			System.out.println("Input X, Y and Boost Time(ms): ");
			scanner = new Scanner(System.in);
			args = scanner.nextLine().split(" ");
		}

		ActionRobot ar = new ActionRobot();
		TrajectoryPlanner tp = new TrajectoryPlanner();
		ActionRobot.fullyZoomOut();
		vision = new Vision(ActionRobot.doScreenShot());
		Rectangle slingshot = vision.findSlingshotMBR();
		while(slingshot == null)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("no slingshot detected. Please remove pop up or zoom out");
			vision = new Vision(ActionRobot.doScreenShot());
			slingshot = vision.findSlingshotMBR();
		}
		Point refPoint = tp.getReferencePoint(slingshot);
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		int tap = 0;
		if(args.length > 2)
			tap = Integer.parseInt(args[2]);
		x_list.add(x);
		y_list.add(y);
		tap_list.add(tap);
		Shot shot = new Shot( refPoint.x, refPoint.y, -x, y,0,tap);

		ActionRobot.fullyZoomOut();
		vision = new Vision(ActionRobot.doScreenShot());
		Rectangle _slingshot = vision.findSlingshotMBR();
		if(!slingshot.equals(_slingshot))
			System.out.println("the scale is changed, the shot might not be executed properly.");
		ar.cshoot(shot);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		screenshot = ActionRobot.doScreenShot();
		vision = new Vision(screenshot);
		List<ABObject> pigs = vision.findPigsMBR();
		List<ABObject> birds = vision.findBirdsRealShape();
		pigs_list.add(pigs.size());
		System.out.println(pigs.size());

		if(birds.size() == 1){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else
		if(birds.size() == 0){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return aRobot.getState();
	}

	public static void main(String args[]) {

		HumanAgent na = new HumanAgent();
		if (args.length > 0)
			na.currentLevel = Integer.parseInt(args[0]);
		na.run();

	}
}

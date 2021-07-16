package ab.demo;
import ab.demo.other.ActionRobot;
import ab.demo.other.Shot;
import ab.planner.TrajectoryPlanner;
import ab.utils.ABUtil;
import ab.utils.LogWriter;
import ab.utils.StateUtil;
import ab.vision.ABObject;
import ab.vision.ABType;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.Vision;
import ab.proto.ClientServerPolicy;

import org.zeromq.ZContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.Scanner;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class HumanAgent_combined implements Runnable{

    private ActionRobot aRobot;
    private Random randomGenerator;
    public int currentLevel = 1;
    public static int time_limit = 12;
    private Map<Integer, Integer> scores = new LinkedHashMap<>();
    TrajectoryPlanner tp;
    private  boolean firstShot;
    private Point prevTarget;
    private ClientServerPolicy policy;
    private MongoClient mongoClient;
    private DBObject run;
    private List<Integer> x_list;
    private List<Integer> y_list;
    private List<Integer> tap_list;
    private List<Integer> pigs_list;
    private List<String> agent_list;
    private DBCollection collection;

    public HumanAgent_combined(){
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
        collection = database.getCollection("HumanSA");
        // --- go to the Poached Eggs episode level selection page ---
        ActionRobot.GoFromMainMenuToLevelSelection();
    }

    public void run(){
        aRobot.loadLevel(currentLevel);
        setRunClear();
        policy = new ClientServerPolicy();
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
                setRunClear();
                aRobot.restartLevel();
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
        pigs_list = new ArrayList<>();
        agent_list = new ArrayList<>();
        x_list = new ArrayList<>();
        y_list = new ArrayList<>();
        tap_list = new ArrayList<>();
    }

    private void fillRun(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        run.put("_id", timestamp);
        run.put("pigsNum", pigs_list);
        run.put("Agents", agent_list);
        run.put("xList", x_list);
        run.put("yList", tap_list);
    }

    private double distance(Point p1, Point p2) {
        return Math
                .sqrt((double) ((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)
                        * (p1.y - p2.y)));
    }

    public GameState solve(){
        BufferedImage screenshot = ActionRobot.doScreenShot();

        int agent;
        Vision vision = new Vision(screenshot);
        List<ABObject> pigs = vision.findPigsMBR();
        final List<ABObject> birds = vision.findBirdsRealShape();
        try (ZContext context = new ZContext()){
            policy.connect(context);

            agent = policy.getNextAgent(pigs, birds);

            policy.shutdown();
        }
        if (agent == 0){
            return solve_default();
        }
        else{
            return solve_human();
        }
    }

    public GameState solve_default(){
        agent_list.add("AI");

        // capture Image
        BufferedImage screenshot = ActionRobot.doScreenShot();

        // process image
        Vision vision = new Vision(screenshot);

        // find the slingshot
        Rectangle sling = vision.findSlingshotMBR();

        // confirm the slingshot
        while (sling == null && aRobot.getState() == GameState.PLAYING) {
            System.out
                    .println("No slingshot detected. Please remove pop up or zoom out");
            ActionRobot.fullyZoomOut();
            screenshot = ActionRobot.doScreenShot();
            vision = new Vision(screenshot);
            sling = vision.findSlingshotMBR();
        }
        // get all the pigs
        List<ABObject> pigs = vision.findPigsMBR();
        GameState state = aRobot.getState();


        // if there is a sling, then play, otherwise just skip.
        if (sling != null) {

            if (!pigs.isEmpty()) {

                Point releasePoint = null;
                Shot shot = new Shot();
                int dx,dy;

                List<Point> reachableReleasePoint = new ArrayList<>();
                List<Shot> reachableShot = new ArrayList<>();

                //Calculate the tapping time according the bird type
                int reachtapInterval = 0;
                switch (aRobot.getBirdTypeOnSling()) {

                    case RedBird:
                        reachtapInterval = 0;
                        break;               // start of trajectory
                    case YellowBird:
                        reachtapInterval = 65 + randomGenerator.nextInt(25);
                        break; // 65-90% of the way
                    case WhiteBird:
                        reachtapInterval = 70 + randomGenerator.nextInt(20);
                        break; // 70-90% of the way
                    case BlackBird:
                        reachtapInterval = 70 + randomGenerator.nextInt(20);
                        break; // 70-90% of the way
                    case BlueBird:
                        reachtapInterval = 65 + randomGenerator.nextInt(20);
                        break; // 65-85% of the way
                    default:
                        reachtapInterval = 60;
                }

                for (ABObject pig : pigs) {
                    Point _tpt = pig.getCenter();
                    ArrayList<Point> pts = tp.estimateLaunchPoint(sling, _tpt);
                    for (Point rPoint : pts) {
                        Point refPoint = tp.getReferencePoint(sling);

                        if (rPoint != null) {
                            double releaseAngle = tp.getReleaseAngle(sling,
                                    rPoint);
                            int temp_dx = (int) rPoint.getX() - refPoint.x;
                            int temp_dy = (int) rPoint.getY() - refPoint.y;
                            System.out.println(rPoint);
                            int tapTime = tp.getTapTime(sling, rPoint, _tpt, reachtapInterval);
                            Shot poss_shot = new Shot(refPoint.x, refPoint.y, temp_dx, temp_dy, 0, tapTime);
                            if (ABUtil.isReachable(vision, pig.getCenter(), poss_shot)){
                                reachableReleasePoint.add(rPoint);
                                reachableShot.add(poss_shot);
                            }
                        }
                    }
                }

                if (!reachableShot.isEmpty()){

                    System.out.println("Found clear Trajectory. ");
                    Integer index = randomGenerator.nextInt(reachableShot.size());
                    shot = reachableShot.get(index);
                    dx = shot.getDx();
                    dy = shot.getDy();

                    releasePoint = reachableReleasePoint.get(index);
                    System.out.println("Release Point: " + releasePoint);
                    System.out.println(dx + dy);
                }
                else {
                    // random pick up a pig
                    ABObject pig = pigs.get(randomGenerator.nextInt(pigs.size()));

                    Point _tpt = pig.getCenter();// if the target is very close to before, randomly choose a
                    // point near it
                    if (prevTarget != null && distance(prevTarget, _tpt) < 10) {
                        double _angle = randomGenerator.nextDouble() * Math.PI * 2;
                        _tpt.x = _tpt.x + (int) (Math.cos(_angle) * 10);
                        _tpt.y = _tpt.y + (int) (Math.sin(_angle) * 10);
                        System.out.println("Randomly changing to " + _tpt);
                    }

                    prevTarget = new Point(_tpt.x, _tpt.y);

                    // estimate the trajectory
                    ArrayList<Point> pts = tp.estimateLaunchPoint(sling, _tpt);

                    // do a high shot when entering a level to find an accurate velocity
                    if (firstShot && pts.size() > 1)
                    {
                        releasePoint = pts.get(1);
                    }
                    else if (pts.size() == 1)
                        releasePoint = pts.get(0);
                    else if (pts.size() == 2)
                    {
                        // randomly choose between the trajectories, with a 1 in
                        // 6 chance of choosing the high one
                        if (randomGenerator.nextInt(6) == 0)
                            releasePoint = pts.get(1);
                        else
                            releasePoint = pts.get(0);
                    }
                    else
                    if(pts.isEmpty())
                    {
                        System.out.println("No release point found for the target");
                        System.out.println("Try a shot with 45 degree");
                        releasePoint = tp.findReleasePoint(sling, Math.PI/4);
                    }

                    // Get the reference point
                    Point refPoint = tp.getReferencePoint(sling);


                    //Calculate the tapping time according the bird type
                    if (releasePoint != null) {
                        double releaseAngle = tp.getReleaseAngle(sling,
                                releasePoint);
                        System.out.println("Release Point: " + releasePoint);
                        System.out.println("Release Angle: "
                                + Math.toDegrees(releaseAngle));
                        int tapInterval = 0;
                        switch (aRobot.getBirdTypeOnSling())
                        {

                            case RedBird:
                                tapInterval = 0; break;               // start of trajectory
                            case YellowBird:
                                tapInterval = 65 + randomGenerator.nextInt(25);break; // 65-90% of the way
                            case WhiteBird:
                                tapInterval =  70 + randomGenerator.nextInt(20);break; // 70-90% of the way
                            case BlackBird:
                                tapInterval =  70 + randomGenerator.nextInt(20);break; // 70-90% of the way
                            case BlueBird:
                                tapInterval =  65 + randomGenerator.nextInt(20);break; // 65-85% of the way
                            default:
                                tapInterval =  60;
                        }

                        int tapTime = tp.getTapTime(sling, releasePoint, _tpt, tapInterval);
                        dx = (int)releasePoint.getX() - refPoint.x;
                        dy = (int)releasePoint.getY() - refPoint.y;
                        shot = new Shot(refPoint.x, refPoint.y, dx, dy, 0, tapTime);
                    }
                    else
                    {
                        System.err.println("No Release Point Found");
                        return state;
                    }
                }

                // check whether the slingshot is changed. the change of the slingshot indicates a change in the scale.
                {
                    ActionRobot.fullyZoomOut();
                    screenshot = ActionRobot.doScreenShot();
                    vision = new Vision(screenshot);
                    Rectangle _sling = vision.findSlingshotMBR();
                    if(_sling != null)
                    {
                        double scale_diff = Math.pow((sling.width - _sling.width),2) +  Math.pow((sling.height - _sling.height),2);
                        if(scale_diff < 25)
                        {
                            if(dx < 0)
                            {
                                aRobot.cshoot(shot);
                                state = aRobot.getState();
                                try
                                {
                                    screenshot = ActionRobot.doScreenShot();
                                    vision = new Vision(screenshot);
                                    List<ABObject> new_pigs = vision.findPigsMBR();
                                    pigs_list.add(new_pigs.size());
                                    System.out.println(new_pigs.size());
                                    Thread.sleep(10000);
                                }
                                catch (Exception e){e.printStackTrace();}
                                if ( state == GameState.PLAYING )
                                {
                                    screenshot = ActionRobot.doScreenShot();
                                    vision = new Vision(screenshot);
                                    List<Point> traj = vision.findTrajPoints();
                                    tp.adjustTrajectory(traj, sling, releasePoint);
                                    firstShot = false;
                                }
                            }
                        }
                        else
                            System.out.println("Scale is changed, can not execute the shot, will re-segement the image");
                    }
                    else
                        System.out.println("no sling detected, can not execute the shot, will re-segement the image");
                }

            }

        }
        return state;
    }

    public GameState solve_human(){
        // capture Image
        agent_list.add("Human");
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

}

package ab.proto;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import ab.proto.Simulator;
import ab.proto.Simulator.State;
import ab.proto.Simulator.Action;
import ab.vision.ABType;
import ab.vision.ABObject;

public class ClientServerPolicy {

    private ZMQ.Socket socket;

    public ClientServerPolicy(){
    }

    public int getNextAgent(List<ABObject> pigs, List<ABObject> birds){
        State.Builder currentState = State.newBuilder();

        // create the current state
        currentState.setPigs(pigs.size());
        currentState.setBirds(birds.size());
        currentState.setEnd(0);

        Action action = callServer(currentState.build());

        return action.getAgent();
    }

    private Action callServer(State state){
        System.out.println("Sending State");
        socket.send(state.toByteArray(), 0);

        byte[] reply = socket.recv(0);
        try {
            return Simulator.Action.parseFrom(reply);
        } catch (InvalidProtocolBufferException e){
            System.out.println(e);
            return null;
        }
    }

    public void connect(ZContext context){
        String server = "tcp://localhost:5555";
        System.out.println("Connecting to server: "+ server);
        socket = context.createSocket(SocketType.REQ);
        socket.connect(server);
    }

    public void shutdown(){
        socket.close();

    }


}

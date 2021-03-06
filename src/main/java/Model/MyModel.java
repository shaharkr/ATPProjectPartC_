package Model;

import Server.Server;
import Client.*;
import IO.*;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * implementation if the IModel interface, javadocs of override functions are stated.
 */
public class MyModel extends Observable implements IModel{
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private Server mazeGeneratingServer,solveSearchProblemServer;
    private Stack<Integer[]> visited;
    private Integer[] prevVisited;
    private final Logger LOG = LogManager.getLogger();

    /**
     * this model uses 2 servers, one for maze generating and one for maze solving.
     * it also uses a stack to save the visited positions inside the maze.
     */
    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
        visited = new Stack<>();
    }

    /**
     * create a client and use the server and client strategy to create the maze.
     * notify observers(viewModel) when finished.
     */
    @Override
    public void generateMaze(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        LOG.info("client number: "+InetAddress.getLocalHost()+" asked for maze of size "+rows+", "+cols);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    clientStrategyGenerateMaze(inFromServer, outToServer, rows, cols);
                }
            });
            client.communicateWithServer();
            movePlayer(this.maze.getStartPosition().getRowIndex(), this.maze.getStartPosition().getColumnIndex());
            setChanged();
            notifyObservers("maze created");
            LOG.info("maze created");
        } catch (Exception e) {
            LOG.error("maze creation failed");
        }
    }

    /**
     * @param inFromServer servers output, clients input
     * @param outToServer servers input, clients output
     * @param rows of maze
     * @param cols of maze
     * helper function used to supply a strategy for the client
     */
    private void clientStrategyGenerateMaze(InputStream inFromServer, OutputStream outToServer, int rows, int cols){
        try{
            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
            toServer.flush();
            int[] mazeDimensions = new int[]{rows,cols};
            toServer.writeObject(mazeDimensions); //send maze dimensions to server
            toServer.flush();
            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
            byte[] decompressedMaze = new byte[rows*cols + 24/*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
            is.read(decompressedMaze);
            //Fill decompressedMaze 25 | P a g e with bytes
            this.maze= new Maze(decompressedMaze);
            this.maze.setPlaceInMaze(this.maze.getGoalPosition(),0);
        } catch (Exception e) {
            LOG.error("cannot connect to maze generating server");
        }
    }

    /**
     * use the client and server strategies to solve the maze(sent by client)
     * notify observers when finished
     */
    @Override
    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        LOG.info("client number: "+InetAddress.getLocalHost()+" asked a solution for his maze");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    clientStrategySolvingMaze(inFromServer, outToServer);
                }
            });
            client.communicateWithServer();
        } catch (Exception e) {
            LOG.error("cannot connect to maze solving server");
        }
    }

    /**
     * @param inFromServer servers output, clients input
     * @param outToServer servers input, clients output
     * helper function to supply the client strategy for solving mazes.
     */
    private void clientStrategySolvingMaze(InputStream inFromServer, OutputStream outToServer){
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(new MyCompressorOutputStream(outToServer));
            Position currP = new Position(this.playerRow, this.playerCol);
            Maze maze2 = new Maze(currP, this.maze.getGoalPosition(), this.maze.getMaze());
            toServer.writeObject(maze2); //send maze to server
            toServer.flush();
            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
            this.solution = (Solution) fromServer.readObject();//read generated maze (compressed with MyCompressor) from server

            setChanged();
            notifyObservers("maze solved");
            LOG.info("maze solved successfully");
        } catch (Exception e) {
            this.solution=null;
            LOG.error("cannot find solution");
        }
    }

    /**
     * @param direction the direction to which the player tries to move his character
     * updates his position using the movePlayer method.
     */
    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if (playerRow > 0)
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < maze.getN() - 1)
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0)
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < maze.getM() - 1)
                    movePlayer(playerRow, playerCol + 1);
            }
            case UP_L -> {
                if (playerRow > 0 && playerCol > 0)
                    movePlayer(playerRow - 1, playerCol - 1);
            }
            case UP_R -> {
                if (playerRow > 0 && playerCol < maze.getM() - 1)
                    movePlayer(playerRow - 1, playerCol + 1);
            }
            case DOWN_L -> {
                if (playerRow < maze.getN() - 1 && playerCol > 0)
                    movePlayer(playerRow + 1, playerCol - 1);
            }
            case DOWN_R -> {
                if (playerRow < maze.getN() - 1 && playerCol < maze.getM() - 1)
                    movePlayer(playerRow + 1, playerCol + 1);
            }
        }

    }

    /**
     * @param row new player row
     * @param col new player column
     * helper for updatePlayerLocation, handles the visited stack and checks if the player reached the goal.
     */
    private void movePlayer(int row, int col){
        if(this.maze.getMaze()[row][col]==1)return;
        if(visited.isEmpty()){
            visited.push(new Integer[]{this.maze.getStartPosition().getRowIndex(),this.maze.getStartPosition().getColumnIndex()});
            prevVisited = new Integer[]{this.maze.getStartPosition().getRowIndex(),this.maze.getStartPosition().getColumnIndex()};
        }
        this.playerRow = row;
        this.playerCol = col;
        addVisited();
        if(this.playerRow==this.maze.getGoalPosition().getRowIndex() && this.playerCol==this.maze.getGoalPosition().getColumnIndex()){
            setChanged();
            notifyObservers("player finished");
            LOG.info("player finished maze");
        }
        else{
            setChanged();
            notifyObservers("player moved");
        }
    }

    /**
     * helper function for movePlayer, handles the visited stack.
     */
    private void addVisited() {
        int bit=0;
        if(prevVisited[0]==playerRow && prevVisited[1]==playerCol){
            visited.pop();
            if(!visited.isEmpty()){
                Integer[] temp = visited.pop();
                if(!visited.isEmpty())prevVisited = visited.peek();
                visited.push(temp);
            }
            bit=1;
        }
        if(bit!=1 && visited.size()>2){
            Integer[] temp1 = visited.pop();
            Integer[] temp2 = visited.pop();
            if(visited.peek()[0]==playerRow && visited.peek()[1]==playerCol){
                Integer[] temp = visited.pop();
                prevVisited = visited.peek().clone();
                visited.push(temp);
                bit=1;
            }
            else{
                visited.push(temp2);
                visited.push(temp1);
            }

        }
        if(bit!=1 && visited.size()>=2){
            if((prevVisited[0]==playerRow && visited.peek()[1]==playerCol) || (visited.peek()[0]==playerRow && prevVisited[1]==playerCol)){
                visited.pop();
                visited.push(new Integer[]{playerRow,playerCol});
                bit=1;
            }
        }
        if(bit!=1){
            prevVisited = visited.peek().clone();
            visited.push(new Integer[]{playerRow,playerCol});
        }
        setChanged();
        notifyObservers("new visited");
    }

    @Override
    public int[][] getMaze() {

        return this.maze.getMaze();
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }


    @Override
    public Integer[] getStartPos() {
        return new Integer[]{this.maze.getStartPosition().getRowIndex(),this.maze.getStartPosition().getColumnIndex()};
    }

    @Override
    public Integer[] getGoalPos() {
        return new Integer[]{this.maze.getGoalPosition().getRowIndex(),this.maze.getGoalPosition().getColumnIndex()};
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void stopServers() {
        this.mazeGeneratingServer.stop();
        this.solveSearchProblemServer.stop();
    }

    @Override
    public Stack<Integer[]> getVisited() {
        return (Stack<Integer[]>)this.visited.clone();
    }

    @Override
    public Queue<Integer[]> getSolution() {
        Queue<Integer[]> to_ret = new LinkedList<>();
        ArrayList<AState> lst = solution.getSolutionPath();
        for (AState as: lst) {
            Integer[] to_add = new Integer[2];
            to_add[0] = Integer.parseInt(as.getState().substring(1,as.getState().indexOf(',')));
            to_add[1] = Integer.parseInt(as.getState().substring(as.getState().indexOf(',')+1,as.getState().indexOf('}')));
            to_ret.add(to_add);
        }
        return to_ret;
    }

    @Override
    public void saveMaze(File file) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new MyCompressorOutputStream(new FileOutputStream(file)));
            out.writeObject(maze);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMaze(File file) {
        try {
            ObjectInputStream decompress = new ObjectInputStream(new MyDecompressorInputStream(new FileInputStream(file)));
            this.maze = (Maze)decompress.readObject();
            while(!visited.isEmpty()){
                visited.pop();
            }
            movePlayer(this.maze.getStartPosition().getRowIndex(), this.maze.getStartPosition().getColumnIndex());
            setChanged();
            notifyObservers("maze created");
            LOG.info("maze created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

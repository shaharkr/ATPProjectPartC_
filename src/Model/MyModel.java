package Model;

import Server.Server;
import Client.*;
import IO.*;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel{
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private Server mazeGeneratingServer,solveSearchProblemServer;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    clientStrategyGenerateMaze(inFromServer, outToServer, rows, cols);
                }
            });
            client.communicateWithServer();
            if(this.maze!=null)
                movePlayer(this.maze.getStartPosition().getRowIndex(), this.maze.getStartPosition().getColumnIndex());

        } catch (Exception e) {
            System.out.println("cannot connect to generate server");
        }
    }

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
            setChanged();
            notifyObservers("maze created");
        } catch (Exception e) {
            this.maze=null;
            setChanged();
            notifyObservers("creating maze failed");
        }
    }

    private void movePlayer(int row, int col){
        if(this.maze.getMaze()[row][col]==1)return;
        this.playerRow = row;
        this.playerCol = col;
        if(this.playerRow==this.maze.getGoalPosition().getRowIndex() && this.playerCol==this.maze.getGoalPosition().getColumnIndex()){
            setChanged();
            notifyObservers("player finished");
        }
        else{
            setChanged();
            notifyObservers("player moved");
        }
    }

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

    @Override
    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    clientStrategySolvingMaze(inFromServer, outToServer);
                }
            });
            client.communicateWithServer();
        } catch (Exception e) {
            System.out.println("cannot connect to solve server");
        }
    }

    private void clientStrategySolvingMaze(InputStream inFromServer, OutputStream outToServer){
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(new MyCompressorOutputStream(outToServer));
            toServer.writeObject(this.maze); //send maze to server
            toServer.flush();
            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
            this.solution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
            setChanged();
            notifyObservers("maze solved");
        } catch (Exception e) {
            this.solution=null;
            setChanged();
            notifyObservers("error seeking solution");
        }
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
    public Solution getSolution() {
        return solution;
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

}

package adventure;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private final String roomName;
    private String imagePath;
    private final String roomDescription;
    private final HashMap<String, Room> neighbors;
    private boolean visited;
    public Room(String roomName, String roomDescription) {
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.neighbors = new HashMap<>();
        visited = false;
    }
    public boolean firstVisit() {
        return !visited;
    }
    public void updateVisited(){visited = true;}
    public String getImagePath(){return imagePath;}
    public void setImagePath(String imagePath){this.imagePath = imagePath;}
    public String getRoomName() {
        return roomName;
    }
    public String getRoomDescription(){
        return roomDescription;
    }
    public boolean checkDirection(String direction){
        return neighbors.containsKey(direction);
    }
    public Room getNeighbor(String direction){
        return neighbors.get(direction);
    }
    public void addNeighbor(String direction, Room neighbor){
        neighbors.put(direction, neighbor);
    }
    public HashMap<String, Room> getNeighbors() {
        return neighbors;
    }
}

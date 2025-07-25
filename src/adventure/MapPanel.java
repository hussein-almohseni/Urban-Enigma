package adventure;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MapPanel extends JPanel {

    // --- Constants for styling the map ---
    private static final int ROOM_DIAMETER = 80;
    private static final Color CONNECTION_COLOR = Color.GRAY;
    private static final Color ROOM_COLOR = new Color(220, 220, 220);
    private static final Color CURRENT_ROOM_HIGHLIGHT = new Color(173, 216, 230); // Light blue
    private static final Color TEXT_COLOR = Color.BLACK;

    // --- Data for drawing ---
    private final HashMap<String, Point> roomLocations;
    private final ArrayList<Room> allRooms;
    private Room currentRoom;

    public MapPanel(ArrayList<Room> allRooms, HashMap<String, Point> locations, Room initialRoom) {
        this.allRooms = allRooms;
        this.roomLocations = locations;
        this.currentRoom = initialRoom;
        this.setBackground(Color.WHITE); // Set a clean background color
    }

    /**
     * Updates the player's current location on the map.
     * @param room The new current room.
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Improve rendering quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- 1. Draw connections between rooms ---
        g2d.setColor(CONNECTION_COLOR);
        g2d.setStroke(new BasicStroke(2)); // Set line thickness
        for (Room room : allRooms) {
            Point p1 = roomLocations.get(room.getRoomName());
            if (p1 == null) continue;

            for (Room neighbor : room.getNeighbors().values()) {
                Point p2 = roomLocations.get(neighbor.getRoomName());
                if (p2 != null) {
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        // --- 2. Draw the rooms (nodes) ---
        for (Room room : allRooms) {
            Point p = roomLocations.get(room.getRoomName());
            if (p == null) continue;

            // Use highlight color if it's the current room, otherwise use default
            if (room == currentRoom) {
                g2d.setColor(CURRENT_ROOM_HIGHLIGHT);
            } else {
                g2d.setColor(ROOM_COLOR);
            }

            // Draw the room as a filled circle
            int cornerX = p.x - ROOM_DIAMETER / 2;
            int cornerY = p.y - ROOM_DIAMETER / 2;
            g2d.fillOval(cornerX, cornerY, ROOM_DIAMETER, ROOM_DIAMETER);

            // Draw a border for the circle
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawOval(cornerX, cornerY, ROOM_DIAMETER, ROOM_DIAMETER);


            // --- 3. Draw the room name ---
            g2d.setColor(TEXT_COLOR);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
            // Center the text in the circle
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(room.getRoomName());
            int textX = p.x - textWidth / 2;
            int textY = p.y + fm.getAscent() / 2;
            g2d.drawString(room.getRoomName(), textX, textY);
        }
    }
}
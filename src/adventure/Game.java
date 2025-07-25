package adventure;
/*
 * Written by: Hussein Almohseni
 * This is a text-based game revolving around a city
 * The main idea is that you forgot your home address and need to retrieve it by going on an adventure
 */
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    //Global variables/attributes
    //rooms
    Room cityCenter, gym, park, store, library, currentRoom;
    private final MapPanel mapPanel;
    private final JLabel statsLabel;
    private final JTextArea instructionArea;
    private final JTextField inputField;
    private final JButton clickerButton;
    private final JButton miniGameButton;
    private final JFrame frame;
    IntWrapper strength, intelligence; // to pass by reference
    boolean printing=false;
    HashMap<String, Boolean> objectives;
    ArrayList<String> playerInventory;
    //frame size
    final int WIDTH = 800, HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    String playerName;


    public Game() {
        // Apply Nimbus, a more modern look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            e.printStackTrace();
        }

        // create rooms and add description
        // Room cityCenter, gym, park, store, library, currentRoom;
        // used AI for some the descriptions
        String description="The heart of the city. Tall buildings surround you, with streets leading in " +
                "every direction. The imposing Mystery Building stands in the middle, its locked doors daring you " +
                "to uncover its secrets.";
        cityCenter=new Room("City Center",description);
        description="A place of sweat and determination. The rhythmic clanging of weights fills the air, " +
                "accompanied by the grunts of those pushing their limits. Posters on the walls display " +
                "motivational quotes, and the gym instructor waits by the bench press, ready to challenge you.";
        gym=new Room("GYM",description);
        description="A tranquil oasis amidst the urban chaos. Towering oak trees provide shade, and a gentle breeze" +
                " carries the scent of blooming flowers. A mysterious clearing lies ahead, where legends speak of a " +
                "hidden treasure guarded by ancient locks.";
        park=new Room("Park",description);
        description="A quaint little shop packed with oddities and essentials. The shelves are crammed with items, " +
                "some dusty with age and others gleaming with newness. Behind the counter, the store owner eyes you " +
                "curiously, as though assessing your worth.";
        store=new Room("Store",description);
        description="A sanctuary of knowledge and whispers. Rows of towering bookshelves stretch endlessly, filled " +
                "with dusty tomes and gleaming covers. The dim lighting and faint smell of old paper create an " +
                "atmosphere of mystery. The librarian watches you intently, guarding the secrets of the books.";
        library=new Room("Library",description);
        currentRoom=cityCenter;
        //add neighbors and image paths
        strength=new IntWrapper(0);
        intelligence=new IntWrapper(0);
        cityCenter.addNeighbor("west",park);
        cityCenter.addNeighbor("east",gym);
        cityCenter.addNeighbor("south",store);
        cityCenter.addNeighbor("north",library);
        cityCenter.setImagePath("/res/City Center.png");
        gym.addNeighbor("west",cityCenter);
        gym.setImagePath("/res/GYM.png");
        park.addNeighbor("east",cityCenter);
        park.setImagePath("/res/Park.png");
        store.addNeighbor("north",cityCenter);
        store.setImagePath("/res/Store.png");
        library.addNeighbor("south",cityCenter);
        library.setImagePath("/res/Library.png");

        // Collect all rooms for the map panel
        ArrayList<Room> allRooms = new ArrayList<>();
        allRooms.add(cityCenter);
        allRooms.add(gym);
        allRooms.add(park);
        allRooms.add(store);
        allRooms.add(library);

        // Initialize panels
        JPanel centerPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel topPanel = new JPanel();

        // define the layout for the map
        HashMap<String, Point> roomLocations = new HashMap<>();
        roomLocations.put("Library", new Point(350, 50));
        roomLocations.put("Park", new Point(100, 150));
        roomLocations.put("City Center", new Point(350, 150));
        roomLocations.put("GYM", new Point(600, 150));
        roomLocations.put("Store", new Point(350, 250));

        // Initialize components
        mapPanel = new MapPanel(allRooms, roomLocations, currentRoom);
        statsLabel = new JLabel("Strength: "+strength.value+"          Intelligence: "+intelligence.value);
        JLabel messageLabel = new JLabel("What would you like to do?");
        instructionArea = new JTextArea(25,50);
        instructionArea.setLineWrap(true);
        instructionArea.setWrapStyleWord(true);
        instructionArea.setEditable(false);
        instructionArea.setFocusable(false);
        inputField = new JTextField(15);
        JButton enterButton = new JButton("Enter");
        JButton quitButton = new JButton("Quit");
        miniGameButton = new JButton("Mini Game: ");
        clickerButton = new JButton("Clicker");

        // Enable auto-scrolling by adjusting the caret
        DefaultCaret caret = (DefaultCaret) instructionArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Wrap the JTextArea inside a JScrollPane
        JScrollPane scrollPane = new JScrollPane(instructionArea);
        // Frame setup
        frame = new JFrame("City Adventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center panel setup
        centerPanel.setLayout(new GridLayout(2,1));
        centerPanel.add(mapPanel);
        centerPanel.add(scrollPane);

        // Bottom panel setup
        bottomPanel.setLayout(new BorderLayout());  // For more flexibility
        JPanel inputPanel = new JPanel();  // A panel to hold the input field and buttons
        inputPanel.setLayout(new FlowLayout());  // You can adjust to suit your design
        //miniGameStarter
        inputPanel.add(miniGameButton);
        inputPanel.add(quitButton);
        inputPanel.add(inputField);
        inputPanel.add(enterButton);
        inputPanel.add(clickerButton);
        //miniGameClicker
        miniGameButton.setVisible(false);
        clickerButton.setVisible(false);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(messageLabel, BorderLayout.NORTH);  // MessageLabel at the top of the bottom panel
        bottomPanel.add(inputPanel, BorderLayout.CENTER);  // Input field and buttons centered

        // Top panel setup
        topPanel.add(statsLabel);

        // Add action listeners
        enterButton.addActionListener(new EnterButtonListener());
        quitButton.addActionListener(e -> System.exit(0));
        inputField.addKeyListener(new EnterKeyListener());
        miniGameButton.addActionListener(new statsButtonListener());
        clickerButton.addActionListener(new statsButtonListener());

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        //Request player's name
        do {
            playerName = JOptionPane.showInputDialog("What is your name?");
            if(playerName==null)
                System.exit(0);
            else
                playerName.trim();
            
        } while (playerName.isEmpty());
        // Make the frame visible
        frame.setVisible(true);

        //print welcome message explaining the game
        printWelcome();
        //request focus for the input field
        inputField.requestFocus();

        //objectives set up
        objectives = new HashMap<>();
        objectives.put("hasExcalibur", false);
        objectives.put("hasTreasureKey", false);
        objectives.put("hasTreasureMap",false);
        objectives.put("hasAddress", false);
        objectives.put("hasRareBook", false);
        objectives.put("hasReadTreasureMap", false);
        objectives.put("hasReadTreasure", false);
        objectives.put("hasUnlockedTreasure", false);
        objectives.put("hasShinyKey",false);

        //initialize ArrayList
        playerInventory=new ArrayList<>();

    }

    // Action listener for the "Enter" button
    private class EnterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(printing){
                return;
            }
            String input = inputField.getText().trim();
            inputValidation(input);
        }
    }

    // Key listener for the input field (Enter key)
    private class EnterKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {
            // Ignore
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if(printing){
                    return;
                }
                String input = inputField.getText().trim();
                inputValidation(input);
                inputField.requestFocus();
            }
        }

        public void keyReleased(KeyEvent e) {
            // Ignore
        }
    }

    // Validate the input and update the room based on the direction
    private void inputValidation(String input) {
        instructionArea.append("\n>> "+input+"\n\n"); //got this idea from the text adventure game example:
        // https://rickadams.org/adventure/advent/
        inputField.setText("");
        input=input.trim().toLowerCase();
        if (input.equals("quit"))
            System.exit(0);

        if (input.equals("help")) {
            printHelp();
            return;
        }
        //display player inventory
        if(input.equalsIgnoreCase("show items")){
            itemsGUI();
            return;
        }
        if(input.equalsIgnoreCase("read map")&&objectives.get("hasTreasureMap")){
            if(intelligence.value<400){
                animateMessage("You examine the map, but the symbols and markings remain a mystery. Perhaps with " +
                        "greater intellect, the secrets of the map will become clear.");
                return;
            }
            if(objectives.get("hasReadTreasureMap")){
                animateMessage("You've already obtained the knowledge of the treasure location.");
                return;
            }
            animateMessage("""
                    You carefully unfold the treasure map, its edges worn and faded from time. As you \
                    study the intricate markings, the details begin to make sense. The map reveals a hidden \
                    clearing in the Park, where the treasure is said to lie beneath the largest oak tree. \
                    You feel a surge of excitement and determination.
                    The treasure’s location has been revealed! Head to the Park and seek out the ancient oak tree\
                     to claim your prize.
                     While in the park, input "find treasure\"""");
            objectives.put("hasReadTreasureMap",true);
            return;
        }
        if(input.equalsIgnoreCase("find treasure")&&currentRoom==park&&objectives.get
                ("hasReadTreasureMap")&&!objectives.get("hasReadTreasure")){
            if(intelligence.value>=600){
                animateMessage("While attempting to open the treasure, you notice a lock. There is also an engraved" +
                        " message guiding you to the location of the key. Due to your high intellect, you're able to" +
                        " decipher it. It reads as follows: \"The key is buried 10 steps west of this treasure.\" " +
                        "Input \"get key\" to pick it up.");
                objectives.put("hasReadTreasure",true);
            } else{
                animateMessage("While attempting to open the treasure, you notice a lock. There is also an engraved" +
                        " message guiding you to the location of the key. Unfortunately your intellect is not high" +
                        " enough to comprehend it.");
            }
            return;
        }
        if(input.equalsIgnoreCase("get key")&&currentRoom==park&&objectives.get("hasReadTreasure")){
            objectives.put("hasTreasureKey",true);
            playerInventory.add("Treasure Key");
            //I want dialogue
            animateMessage("Following the note’s instructions, you carefully count ten steps west of the treasure. You"+
                    " dig into the soft soil, your heart pounding with anticipation. Moments later, your hands grasp" +
                    " a solid object—it’s a key! You’ve found the Treasure Key! Input \"unlock treasure\" to check" +
                    " what's in the treasure box.");
            return;
        }
        if(input.equalsIgnoreCase("unlock treasure")&&currentRoom==park&&objectives.get("hasTreasureKey")&&!objectives.get("hasShinyKey")){
            String expression="";
            if(!objectives.get("hasUnlockedTreasure")){
                expression="Using the key, you successfully unlocked the treasure. ";
                objectives.put("hasUnlockedTreasure",true);
            }
            if(strength.value>=800){
                animateMessage(expression+"With your exceptional strength you lifted the lid open and seen a shiny" +
                        " key and a note which reads as follows:\n" +
                        "\"Great job adventurer for coming this far. Use this key to unlock the Mystery Building in" +
                        " the City Center.\" While in the City Center, you may unlock the building by inputting" +
                        " \"open building\"");
                objectives.put("hasShinyKey",true);
                playerInventory.add("Shiny Key");
            }
            else{
                animateMessage(expression+"You attempt to lift the lid of the treasure, but you find it too heavy. " +
                        "Work on increasing your strength.");
                return;
            }
        }
        if(input.equalsIgnoreCase("open building")&&currentRoom==cityCenter&&objectives.get("hasShinyKey")){
            animateMessage("""
                    With trembling hands, you insert the Shiny Key into the ornate lock of the Mystery Building.\
                     The ancient mechanism clicks, and the massive doors creak open, revealing a dimly lit interior.
                    The room is empty, save for a pedestal in the center, bathed in a soft, golden light. Upon the \
                    pedestal lies a sealed envelope with your name on it.
                    You open the envelope and find a handwritten note inside. The note reads: "[Your House is north \
                    west of the City Center]." A wave of relief washes over you as the pieces of your journey fall \
                    into place. You have finally found your way home.
                    To complete this game, input "go northwest" while in the City Center.""");
            objectives.put("hasAddress",true);
            playerInventory.add("Address: \"Northwest of the City Center\"");
        }
        if(input.equalsIgnoreCase("go northwest")&&currentRoom==cityCenter&&objectives.get("hasAddress")){
            JOptionPane.showMessageDialog(frame,"Congratulations!, "+playerName+". You have completed" +
                    " your adventure. Thank you for playing!","Congrats",JOptionPane.PLAIN_MESSAGE);
            frame.dispose();
        }
        if (input.startsWith("head")||input.startsWith("go")){
            String direction = input.startsWith("head") ? input.replaceAll("head", "") :
                    input.replaceAll("go", "");
            // Get the direction part of the input
            direction = direction.trim();
            String result = checkDirection(direction);

            // Handle invalid direction
            if (result.equals("Invalid")) {
                animateMessage("Invalid direction. You're currently in the " + currentRoom.getRoomName());
            } else {
                if(currentRoom.firstVisit()&&((currentRoom!=store||!objectives.get("hasTreasureMap")))) {
                    animateMessage("You entered the " + currentRoom.getRoomName()+"\n"
                            +currentRoom.getRoomDescription());
                    currentRoom.updateVisited();
                } else
                    animateMessage("You entered the " + currentRoom.getRoomName());
                // Inside the else block for a valid move
                mapPanel.setCurrentRoom(currentRoom);
                mapPanel.repaint(); // Redraw the map with the new location highlighted
            }
        }
        else
            animateMessage("Invalid input. At anytime, you may input \"help\"  to see a list of recognized actions");
    }

    // Method to print help instructions (not yet implemented)
    private void printHelp() {
        if(printing){
            return;
        }
        animateMessage("""
                General Commands:
                help: Shows a list of available commands and their descriptions.
                show items: Displays the items currently in your inventory.
                quit: Exit the game immediately.
                
                Exploration Commands:
                head (or go) [direction]: allows you to move between locations as long as there's a link between\
                 them.
                Directions Allowed: north, northeast, east, southeast, south, southwest, west, northwest.
                Example: "head north"
                read map: Examine the treasure map after obtaining it to reveal the location of the treasure in the\
                 Park.
                find treasure: Search for the treasure in the Park based on the map's instructions.
                get key: Retrieve the treasure key after finding the its location.
                unlock treasure: Unlock the treasure after finding its key.
                open building: Unlock and enter the Mystery Building in the City Center to uncover your address.
                go [direction on address]: Complete the game by heading to your house once you know its location.
                
                Stat-Building Commands:
                lift: Use this button in the GYM to increase your strength.
                Mini Game: Use this button in the GYM and Library to open a mini-game and increase stats.
                read: Use this button in the Library to raise your intelligence.""");


    }

    // Check the direction based on the current room and return the new room
    private String checkDirection(String direction) {
        if(currentRoom.checkDirection(direction.toLowerCase())){
        currentRoom=currentRoom.getNeighbor(direction.toLowerCase());
        if(currentRoom==store&&objectives.get("hasExcalibur")&&!objectives.get("hasTreasureMap")){
            objectives.put("hasTreasureMap",true);
            playerInventory.add("Treasure Map");
            animateMessage("""
                    As soon as you entered the store, the store owner stops you and says, "Ah, what’s this?\
                     The legendary sword, Excalibur! I never thought I’d see it in my lifetime. \
                     You’ve proven yourself worthy, adventurer. But strength alone won’t uncover \
                    the treasure. You’ll need a sharp mind as well. Here, take this treasure map. It’ll lead \
                    you to a secret buried in the Park. Use it wisely—your destiny awaits."
                    Once you reach the adequate intelligence, you may read the map by typing "read map\"""");

        }
        if(currentRoom.getRoomName().equalsIgnoreCase("GYM")){
            //show buttons
            clickerButton.setText("Lift: +10 strength");
            clickerButton.setVisible(true);
            miniGameButton.setText("Mini Game: +100 strength");
            miniGameButton.setVisible(true);
            checkForExcalibur();
        } else if(currentRoom.getRoomName().equalsIgnoreCase("Library")){
            //show appropriate buttons
            checkForRareBook();
            if(objectives.get("hasRareBook")){
                clickerButton.setText("Read: +10 intelligence");
                miniGameButton.setText("Mini Game: +75 intelligence");
            }
            else {
                clickerButton.setText("Read: +5 intelligence");
                miniGameButton.setText("Mini Game: +50 intelligence");
            }
            clickerButton.setVisible(true);
            miniGameButton.setVisible(true);
        } else {
            clickerButton.setVisible(false);
            miniGameButton.setVisible(false);
        }
        return currentRoom.getRoomName();
        }
            return "Invalid";
    }

    // check if the user met the requirements to unlock the legendary sword Excalibur
    private void checkForExcalibur(){
        if(strength.value>=500&&intelligence.value>=200&&!objectives.get("hasExcalibur")){
            objectives.put("hasExcalibur", true);
            playerInventory.add("Excalibur");
            animateMessage("""
                    The Gym instructor smiles approvingly.
                    "Congratulations! You have proven yourself worthy and obtained Excalibur."
                    The Gym instructor hands you the legendary sword. A faint glow surrounds it, \
                    and you feel a surge of power coursing through you.""");
        }
    }

    // check if the user met the requirements to unlock the rare magical book
    private void checkForRareBook(){
        if(intelligence.value>=250&&!objectives.get("hasRareBook")){
            objectives.put("hasRareBook", true);
            playerInventory.add("The Secrets of the Mind");
            animateMessage("""
                    The librarian noticed something different about you and called you. "Ah, I see you’ve\
                     been tirelessly working to expand your mind. Your dedication to learning is inspiring. As a\
                     token of recognition, I present to you this rare tome—'The Secrets of the Mind.' It is said\
                     to unlock the full potential of those who seek wisdom. With this, your learning will be faster\
                     and more profound." From now on, you’ll gain more insight whenever you read or engage in\
                     intellectual challenges.""");

        }
    }

    // show a window with buttons to allow the user to pickup items
    private void itemsGUI() {
        if(playerInventory.isEmpty()) {
            animateMessage("Your inventory is currently empty.");
            return;
        }
        StringBuilder items=new StringBuilder();
        for(String item:playerInventory){
            items.append(item);
            if(!item.equals(playerInventory.getLast()))
                items.append("\n");
        }
        animateMessage("Here's what you currently have in your inventory:\n"+items);
    }


    // append messages to instructionsLabel slowly to create an animation
    private void animateMessage(String message) {
        // make sure only one message is printing
        if(printing){
            return;
        }
        final int delay = 20; // Time between characters
        final Timer timer = new Timer(delay, null);

        printing=true;
        timer.addActionListener(new ActionListener() {
            int index = 0;

            public void actionPerformed(ActionEvent e) {
                if (index < message.length()) {
                    instructionArea.append(String.valueOf(message.charAt(index)));
                    index++;
                } else {
                    timer.stop(); // Stop the timer when done
                    instructionArea.append("\n");
                    printing=false; // change the status of printing
                }
            }
        });

        timer.start(); // Start the animation
    }
    private class statsButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==miniGameButton&&currentRoom==library){
                frame.setVisible(false);
                new MazeGUI(frame,strength,intelligence,statsLabel,objectives.get("hasRareBook"));
            } else if(e.getSource()==miniGameButton&&currentRoom==gym){
                frame.setVisible(false);
                new FourxFourTicTacToe(frame,strength,intelligence,statsLabel);
            } else if(e.getSource()==clickerButton&&currentRoom==library){
                if(objectives.get("hasRareBook")){
                    intelligence.value+=10;
                }
                else
                    intelligence.value+=5;
                statsLabel.setText("Strength: "+strength.value+"          Intelligence: "+intelligence.value);
            } else{
                strength.value+=10;
                statsLabel.setText("Strength: "+strength.value+"          Intelligence: "+intelligence.value);
            }
            inputField.requestFocus();
        }
    }
    // Main method to start the game

    private void printWelcome(){
        animateMessage("Welcome to City Adventure, "+playerName+".\n\n" +
                "You woke up in a mysterious city with no memory of your home address. A cryptic note in your pocket " +
                "reads:\n" +
                "\"Find the Mystery Building at the City Center to uncover your address. But beware, the path will " +
                "test your strength and intellect. Seek the Excalibur, and all will be revealed.\" " +
                "Alternate between the GYM and Library and increase your stats to reveal more secrets and progress " +
                "further.\n\n" +
                "You're in the " + currentRoom.getRoomName()+"\n"+ currentRoom.getRoomDescription());
        currentRoom.updateVisited();
    }
    public static void main(String[] args) {
        new Game();
    }
}
package trafficsim;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


/**
 * JFrame Class which builds the main window of the Traffic Simulation Program *
 *
 * @author Tristan Davey
 */
public class mainWindow extends javax.swing.JFrame {



    //Main Menu Bar
    private JMenuBar mainMenuBar;
    private JMenu fileMenu;
    private JMenu settingsMenu;
    
    //File Menu
    private JMenuItem runMenuItem;
    private JMenuItem runMultiMenuItem;
    private JCheckBoxMenuItem simRunMenuItem;
    private JMenuItem exitMenuItem;

    //Settings Menu
    private JMenuItem setHLanesMenuItem;
    private JMenuItem setVLanesMenuItem;
    private JMenuItem setHProbMenuItem;
    private JMenuItem setVProbMenuItem;

    //Drawing Area
    private SimulationGUI simGUI;

    //Lower Control Panel
    private JPanel controlPanel;
    private JButton runSimulationButton;
    private JButton stopSimulationButton;
    private JButton lightsCycleButton;

    public mainWindow(Intersection modelIntersection, SimulationEnvironment simulation, String title) {

        super(title);

        //Main Menubar Initialisation
        mainMenuBar = new JMenuBar();
        fileMenu = new JMenu();
        settingsMenu = new JMenu();

        //File Menu Initialisation
        runMenuItem = new JMenuItem();
        runMultiMenuItem = new JMenuItem();
        simRunMenuItem = new JCheckBoxMenuItem();
        exitMenuItem = new JMenuItem();
       
        //Settings Menu Initialisation
        setHLanesMenuItem =  new JMenuItem();
        setVLanesMenuItem = new JMenuItem();
        setHProbMenuItem = new JMenuItem();
        setVProbMenuItem = new JMenuItem();

        //Set Window Settings
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        //Set Main MenuBar Text
        fileMenu.setText("File");
        settingsMenu.setText("Settings");

        //Set File Menu Text
        runMenuItem.setText("Run One Cycle");
        runMultiMenuItem.setText("Run Many Cycles");
        simRunMenuItem.setText("Run Simulation");
        exitMenuItem.setText("Exit");

        //Set Settings Menu Text
        setHLanesMenuItem.setText("No. Horizontal Lanes");
        setVLanesMenuItem.setText("No. Vertical Lanes");
        setHProbMenuItem.setText("Horizontal Car Regularity");
        setVProbMenuItem.setText("Vertical Car Regularity");

        //Construct File Menu
        fileMenu.add(runMenuItem);
        fileMenu.add(runMultiMenuItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(simRunMenuItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitMenuItem);

        //Construct Settings Menu
        settingsMenu.add(setHLanesMenuItem);
        settingsMenu.add(setVLanesMenuItem);
        settingsMenu.add(new JSeparator());
        settingsMenu.add(setHProbMenuItem);
        settingsMenu.add(setVProbMenuItem);

        //Construct Menubar
        mainMenuBar.add(fileMenu);
        mainMenuBar.add(settingsMenu);
        setJMenuBar(mainMenuBar);

        simGUI = new SimulationGUI(modelIntersection);
        getContentPane().add(BorderLayout.CENTER, simGUI);

        // Construct Lower JPanel
        controlPanel = new JPanel();
        runSimulationButton = new JButton("Run");
        stopSimulationButton = new JButton("Stop");
        lightsCycleButton = new JButton("Cycle Lights");

        controlPanel.add(BorderLayout.CENTER, runSimulationButton);
        controlPanel.add(BorderLayout.CENTER, stopSimulationButton);
        controlPanel.add(BorderLayout.EAST, lightsCycleButton);
        stopSimulationButton.setVisible(false);
        add(controlPanel, BorderLayout.SOUTH);

        //Add Event Handlers
        stopSimulationButton.addActionListener(new simulationToggle(this, simulation));
        runSimulationButton.addActionListener(new simulationToggle(this, simulation));
        simRunMenuItem.addActionListener(new simulationToggle(this, simulation));
        lightsCycleButton.addActionListener(new lightsCycleListener(this, simulation));


        //Pack the window
        pack();
    }
    
    public void toggleSimulation(Boolean condition) {
        simRunMenuItem.setSelected(condition);
        runSimulationButton.setVisible(!condition);
        stopSimulationButton.setVisible(condition);
    }

    public void lightCycle() {
        lightsCycleButton.setEnabled(false);
    }

}

/**
 * ActionListener implementing class to toggle the simulation from running to stopped or vica-versa.
 *
 * @author Tristan Davey
 */
 class simulationToggle implements ActionListener {

     mainWindow window;
     SimulationEnvironment simulation;

    simulationToggle(mainWindow window, SimulationEnvironment simulation) {
        this.window = window;
        this.simulation = simulation;
    }

    public void actionPerformed(ActionEvent e) {
        window.toggleSimulation(!Settings.getSimSettings().getSimulationRunning());
        if(Settings.getSimSettings().getSimulationRunning()) {
            try {
                simulation.stop();
            } catch (InterruptedException ex) {
                Logger.getLogger(simulationToggle.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                simulation.run();
            } catch (InterruptedException ex) {
                Logger.getLogger(simulationToggle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

 }


 /**
 * ActionListener implementing class which handles button presses on the buttons to make the traffic lights change
 *
 * @author Tristan Davey
 */
 class lightsCycleListener implements ActionListener {

     mainWindow window;
     SimulationEnvironment simulation;

    public lightsCycleListener(mainWindow window, SimulationEnvironment simulation) {
        this.window = window;
        this.simulation = simulation;
    }

    public void actionPerformed(ActionEvent e) {
        simulation.lightCycle();
    }

}


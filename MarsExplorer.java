import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class MarsExplorer extends Frame implements ActionListener {
	
	
	//Model parameters.
	private int numOfIterations = 1000;
	private int numOfAgents = 5;
	private int numOfSamples = 5;	
	
	
	//declare variables.
	private Canvas c = new Canvas();
	MenuItem setupMenuItem = new MenuItem("Setup Model");
	MenuItem runMenuItem = new MenuItem("Run Model");
	private ArrayList <Agent> agents = new ArrayList <Agent>();
	private Environment env = new Environment();
	
	
	//MarsExplorer - called from main method.
	public MarsExplorer() {
		makeGUI();
	}
	
	
	//makeGUI - makes the gui and adds action listeners to components.
	public void makeGUI() {
		add(c);
		setSize((env.getWidth() * 15 + 30), (env.getHeight() * 15 + 70));
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				((Frame)e.getSource()).dispose();
				//System.exit(0);.
			}
		});
		MenuBar menuBar = new MenuBar();
		Menu menu1 = new Menu("Menu");
		menuBar.add(menu1);
		menu1.add(setupMenuItem);
		setupMenuItem.addActionListener(this);
		setupMenuItem.setEnabled(true);
		menu1.add(runMenuItem);
		runMenuItem.addActionListener(this);
		runMenuItem.setEnabled(false);
		setMenuBar(menuBar);
		setLocation(
			(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (getWidth() / 2), 
			(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (getHeight() / 2)
		);
		setVisible(true);
		
	}
	
	
	//paint - draws environment from image and agents to canvas in gui.
	public void paint(Graphics g) {
		Image image = env.getDataAsImage(); 
		Graphics cg = c.getGraphics();
		Graphics2D cg2 = (Graphics2D) cg;
		cg2.scale(15.0, 15.0);
		if (image != null) {
			cg2.drawImage(image, 0, 0, this);
		}
		for (Agent agent : agents) {
			cg2.setColor(Color.BLACK);
			if (agent.getHasSample() == true) {
				cg2.setColor(Color.GREEN);
			}			
			cg2.fillOval(agent.getX(), agent.getY(), 1, 1);
		}
	}
	
	
	//makeAgents - populates agents array list with agents contructed using the agent class, pass pointer to env variable.
	private void makeAgents() {
		agents.clear();
		for (int i = 0; i < numOfAgents; i++) {
			agents.add(new Agent(env,agents));
		}
		
	}
	
	
	//makeEnvironment - populates env 2D int array with values.
	private void makeEnvironment() {
		
		//initially set all values in env to 'surface'.
		for (int i = 0; i < env.getHeight(); i++) {
			for (int j = 0; j < env.getWidth(); j++) {
				env.setDataValue(j, i, 1);
			}
		}
		
		
		//place 'obstacles' in env
		for (int i = 1; i < env.getWidth() - 1; i++) {
			for (int j = 1; j < env.getHeight() - 1; j++) {
				if (j != (env.getHeight() / 2)) {
					double m = 0;
					m = Math.random();
					//if random number > 0.95 (low chance).
					if (m > 0.95) {
						env.setDataValue(i, j, 2);
						//maybe add branching of obstacles.
					}					
				}	
			}
		}
		
		//clear space for 'mothership/base' in env.
		for (int i = (env.getHeight() / 2 - 4); i < (env.getHeight() / 2 + 4); i++) {
			for (int j = (env.getWidth() / 2 - 4); j < (env.getWidth() / 2 + 4); j++) {
				env.setDataValue(j, i, 1);
			}
		}
		
		//place 'mothership/base' in env.
		for (int i = (env.getHeight() / 2 - 1); i < (env.getHeight() / 2 + 1); i++) {
			for (int j = (env.getWidth() / 2 - 1); j < (env.getWidth() / 2 + 1); j++) {
				env.setDataValue(j, i, 4);
			}
		}
		
		//place 'rock samples' in env.
		for (int i = 0; i < numOfSamples; i++) {
			int ranHeight;
			int ranWidth;
			do {
				Random ran1 = new Random();
				ranHeight = ran1.nextInt(env.getHeight());
				
				Random ran2 = new Random();
				ranWidth = ran2.nextInt(env.getWidth());
			
			} while ((ranHeight < 3) || (ranHeight > (env.getHeight() - 4)) || (ranWidth < 3) || (ranWidth > (env.getWidth() - 4 )) || (env.getDataValue(ranWidth, ranHeight) != 1));
			env.setDataValue(ranWidth, ranHeight, 3);
		}		
	}
	
	
	//runModel - iterates over running each agent in environment until all samples collected or iterations exceeded.
	private void runModel() {
		for (int i = 0; i < numOfIterations; i++) {
			Collections.shuffle(agents);
			for (int j = 0; j < agents.size(); j++) {
				agents.get(j).run();
			}
			update(getGraphics());
			repaint();
			//attempt to mitigate flickering of graphics.
			try
			{
				Thread.sleep(150);
			}
			catch(InterruptedException ex)
			{
				Thread.currentThread().interrupt();
			}
			//check stopping criteria, retrun true if all samples collected.
			if (modelFinishedCheck() == true) {
				break;
			}
		}
		setupMenuItem.setEnabled(true);
		System.out.println("Finished");
	}
	
	
	//modelFinishedCheck - checks to see if all rock samples have been collected and returned to base.
	private boolean modelFinishedCheck() {
		int samplesCollected = 0;
		for (Agent agent : agents) {
			samplesCollected = samplesCollected + agent.getNumSamplesAgentCollected();
		}
		if (samplesCollected == numOfSamples) {
			return true;
		}
		return false;
	}
	
	
	//Event listener for menu items.
	public void actionPerformed(ActionEvent e) {
		MenuItem chosenMenuItem = (MenuItem)e.getSource();
		if (chosenMenuItem.getLabel().equals("Setup Model")) {
			makeEnvironment();
			makeAgents();
			runMenuItem.setEnabled(true);
		}
		if (chosenMenuItem.getLabel().equals("Run Model")) {
			runMenuItem.setEnabled(false);
			setupMenuItem.setEnabled(false);
			runModel();
		}		
		repaint();
		
	}
	
	
	//main - calls MarsExplorer.
	public static void main (String args[]) {
		new MarsExplorer();
	}
	
	
}
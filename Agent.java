import java.awt.*;
import java.util.*;


public class Agent {
	
	
	//Agent parameters.
	protected Environment env = null;
	protected ArrayList<Agent> agents = null;
	protected int x = 0;
	protected int y = 0;
	protected boolean hasSample = false;
	protected int sightRange = 7;
	protected int numSamplesAgentCollected;
	
	
	//Agent - constructor for Agent.
	public Agent (Environment envIn, ArrayList<Agent> agentsIn) {
		env = envIn;
		agents = agentsIn;
		x = env.getWidth() / 2;
		y = env.getHeight() / 2;
		numSamplesAgentCollected = 0;
	}
	
	
	//run - determines which behaviour the agent will perform during current iteration.
	//based on subsumption architecture.
	public void run() {		
		//Behaviour 1 - if obstacle - move around obstacle (highest priority). NOT USED.
		/*
		if (true == false) {
			moveAroundObstacle();
		}
		*/		
		
		//Behaviour 2 - if carrying sample and at base - drop sample.
		if ((this.getHasSample() == true) && (env.getDataValue(this.x, this.y) == 4)) {
			dropSample();
		}
		
		//Behaviour 3 - if carrying sample and not at base - move towards base.
		else if (this.getHasSample() == true) {
			bringSample();
		}
		
		//Behaviour 4 - if on top of sample - pick up sample.
		else if (agentOnSample() == true) {
			pickUpSample();
		}
		
		//Behaviour 5 - if can 'see' sample - move toward sample.
		else if (sampleInSight() == true) {
			moveTowardSample();
		}
		
		//Behaviour 6 - if true move randomly (lowest priority).
		else {
			moveRandom();
		}
	}
	
	
	//Behaviour 1 - moveAroundObstacle - move agent to space towards goal that does not contain an obstacle. NOT USED.
	public void moveAroundObstacle() {
		
	}
	
	
	//Behaviour 2 - dropSample - set agents state of hasSample to false, increment numSamplesAgentCollected.
	public void dropSample() {
		this.setHasSampleFalse();
		this.numSamplesAgentCollected++;
	}
	
	
	//Behaviour 3 - bringSample - agent move, with sample, towards base.
	public void bringSample() {
		//get agent's location relative to base.
		int tempYDiff = this.y - (env.getHeight() / 2);
		int tempXDiff = this.x - (env.getWidth() / 2);
		
		//structure for determining which direction to move agent.
		if (tempYDiff < 0) {
			//go south
			if (env.getDataValue(this.x, this.y + 1) != 2) {
				this.y = this.y + 1;
			}
			else {
				moveRandom();
			}
		}
		else if (tempYDiff > 0) {
			//go north
			if (env.getDataValue(this.x, this.y - 1) != 2) {
				this.y = this.y - 1;
			}
			else {
				moveRandom();
			}
		}
		else {
			//tempYDiff == 0
			if (tempXDiff < 0) {
				//go east
				if (env.getDataValue(this.x + 1, this.y) != 2) {
					this.x = this.x + 1;
				}
				else {
					moveRandom();
					
				}
			}
			else if (tempXDiff > 0) {
				//go west
				if (env.getDataValue(this.x - 1, this.y) != 2) {
					this.x = this.x - 1;
				}
				else {
					moveRandom();
				}
			}
			else {
				
			}
		}
	}
	
	
	//agentOnSample - checks to see if agent is on-top-of sample.
	public boolean agentOnSample() {
		if (env.getDataValue(this.x, this.y) == 3) {
			return true;
		}
		return false;
	}
	
	
	//Behaviour 4 - pickUpSample - removes sample from environment, set agent hasSample to true.
	public void pickUpSample() {
		env.setDataValue(this.x, this.y, 1);
		setHasSampleTrue();
	}
	
	
	//Behaviour 5 - moveTowardSample - agent moves towards sample that is in sight range.
	public void moveTowardSample() {
		//loop through all environment spaces in sight range.
		for (int i = (this.x - sightRange); i < (this.x + sightRange); i++) {
			for (int j = (this.y - sightRange); j < (this.y + sightRange); j++) {
				//avoid out of bounds error thrown if agent near edge of environment.
				if ((j >= 0) && (j < env.getHeight()) && (i >= 0) && (i < env.getWidth())) {
					//take first sample found in agent range.
					if (env.getDataValue(i, j) == 3) {
						
						//get agent's location relative to sample.
						int tempYDiff = this.y - j;
						int tempXDiff = this.x - i;
						
						//structure for determining which direction to move agent.
						if (tempYDiff < 0) {
							//go south
							if (env.getDataValue(this.x, this.y + 1) != 2) {
								this.y = this.y + 1;
							}
							else {
								moveRandom();
							}
						}
						else if (tempYDiff > 0) {
							//go north
							if (env.getDataValue(this.x, this.y - 1) != 2) {
								this.y = this.y - 1;
							}
							else {
								moveRandom();
							}
						}
						else {
							if (tempXDiff < 0) {
								//go east
								if (env.getDataValue(this.x + 1, this.y) != 2) {
									this.x = this.x + 1;
								}
								else {
									//redirect up
									if (env.getDataValue(this.x, this.y - 1) != 2) {
										this.y = this.y - 1;
										//redirect right
										if (env.getDataValue(this.x + 1, this.y) !=2) {
											this.x = this.x + 1;
											//redirect right again
											if (env.getDataValue(this.x + 1, this.y) !=2) {
												this.x = this.x + 1;
											}
										}
									}
									//redirect down
									if (env.getDataValue(this.x, this.y + 1) != 2) {
										this.y = this.y + 1;
										//redirect right
										if (env.getDataValue(this.x + 1, this.y) !=2) {
											this.x = this.x + 1;
											//redirect right again
											if (env.getDataValue(this.x + 1, this.y) !=2) {
												this.x = this.x + 1;
											}
										}
									}
									else {
										
									}
								}
							}
							else if (tempXDiff > 0) {
								//go west
								if (env.getDataValue(this.x - 1, this.y) != 2) {
									this.x = this.x - 1;
								}
								else {
									//redirect up
									if (env.getDataValue(this.x, this.y - 1) != 2) {
										this.y = this.y - 1;
										//redirect left
										if (env.getDataValue(this.x - 1, this.y) !=2) {
											this.x = this.x - 1;
											//redirect left again
											if (env.getDataValue(this.x - 1, this.y) !=2) {
												this.x = this.x - 1;
											}
										}
									}
									//redirect down
									if (env.getDataValue(this.x, this.y + 1) != 2) {
										this.y = this.y + 1;
										//redirect left
										if (env.getDataValue(this.x - 1, this.y) !=2) {
											this.x = this.x - 1;
											//redirect left again
											if (env.getDataValue(this.x - 1, this.y) !=2) {
												this.x = this.x - 1;
											}
										}
									}
								}
							}
							else {
								
							}
						}
						//agent only moves one space toward first sample found per iteration.
						return;
					}
				}
			}
		}
	}
	
	
	//Behaviour 6 - moveRandom - randomly move agent one space per iteration in environment.
	public void moveRandom() {
		//declare int variable.
		int ranDirection;
		int tempX;
		int tempY;
		
		do {
			//get agents current x,y.
			tempX = this.x;
			tempY = this.y;
			
			//assign variable random int 1/2/3/4.
			Random ranNum = new Random();
			ranDirection = ranNum.nextInt(4);
			
			//switch statement.
			switch (ranDirection) {
				case 0:
					//move north.
					tempY--;
					break;
				case 1:
					//move east.
					tempX++;
					break;
				case 2:
					//move south.
					tempY++;
					break;
				case 3:
					//move west.
					tempX--;
					break;
			}
		//while no obstacle at chosen location && location is in bounds of environment.
		} while ((tempX < 0) || (tempX > env.getWidth() - 1) || (tempY < 0) || (tempY > env.getHeight() - 1) || (env.getDataValue(tempX, tempY) != 1));
		//set agent new location.
		this.x = tempX;
		this.y = tempY;
	}
	
	
	//getX - return agent x location.
	public int getX() {
		return x;
	}
	
	
	//getY - return agent y location.
	public int getY() {
		return y;
	}
	
	
	//getHasSample - return agent hasSample variable.
	public boolean getHasSample() {
		return hasSample;
	}
	
	
	//setHasSampleTrue - set agent hasSample variable to true.
	public void setHasSampleTrue() {
		this.hasSample = true;
	}
	
	
	//setHasSampleFalse - set agent hasSample variable to false.
	public void setHasSampleFalse() {
		this.hasSample = false;
	}
	
	
	//getNumSamplesAgentCollected - return the value of agent numSamplesAgentCollected. called from modelFinishedCheck in MarsExplorer class.
	public int getNumSamplesAgentCollected() {
		return this.numSamplesAgentCollected;
	}
	
	
	//sampleInSight - check to see if a sample is in agent sight range.
	public boolean sampleInSight() {
		for (int i = (this.x - sightRange); i < (this.x + sightRange); i++) {
			for (int j = (this.y - sightRange); j < (this.y + sightRange); j++) {
				if ((j >= 0) && (j < env.getHeight()) && (i >= 0) && (i < env.getWidth())) {
					if (env.getDataValue(i, j) == 3) {
						return true;
					}
				}
			}
		}		
		return false;
	}
	
	
}
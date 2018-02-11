import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class ItemEffect
{
	//timer that times how long item is in effect
	CustomTimer usageTimer;
	
	//time item will be in use in total (in milliseconds)
	int timeActive;
	
	//which player got the item (NOT that player's index)
	int playerNumber;
	
	//whether this item should run on a timer
	boolean timeBased;
	
	public int timeRemaining()
	{
		if (timeBased)
		{
			//rounds up time left if the item is time based
			return (int) Math.ceil((timeActive - usageTimer.getTime()) / 1000.);
		}
		else
		{
			//if the item is not time based, return a time of zero
			return 0;
		}
	}
	
	//constructor that accepts total time in use, player that created the item, and boolean that
	//states whether the item is time based
	public ItemEffect(int time, int player, boolean timeBool)
	{
		usageTimer = new CustomTimer();
		this.timeActive = time;
		this.playerNumber = player;
		this.timeBased = timeBool;
		usageTimer.startTimer();
	}
	
	//marks non-timebased items for removal
	//this works because the item's time is set as zero
	public void remove()
	{
		timeBased = true;
	}
	
	//if it is time to remove the item and the item is time based, remove it
	public boolean timeToRemove()
	{
		if (usageTimer.getTime() > timeActive && timeBased)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//runs items routine (not used for all items)
	public void routine(Square[] players)
	{
		
	}
	
	//paints things to screen (not used for all items)
	public void paint(Graphics2D g2d)
	{
		
	}
}

//Item that creates a frame that other players must get into in time
class PictureTime extends ItemEffect
{
	//this is the class that holds the picture frame
	class PictureFrame
	{
		//rectangle coordinates
		int r1X;
		int r1Y;
		int r1W;
		int r1H;
		
		//rectangle coordinates
		int r2X;
		int r2Y;
		int r2W;
		int r2H;
		
		//circle one coordinates
		int cX;
		int cY;
		int cR;
		
		public PictureFrame()
		{
			//creates both rectangles and the internal circle
			//the outer rectangle's location is randomly determines, as long as it is fully within the screen
			r1W = 600;
			r1H = 400;
			r1X = (int) (Math.random() * (GameMain.GAME_WIDTH - r1W));
			r1Y = (int) (Math.random() * (GameMain.GAME_HEIGHT - r1H));
			
			r2W = 500;
			r2H = 300;
			//centers the smaller rectangle within the larger one
			r2X = ((r1X + (r1X + r1W)) / 2) - (r2W / 2);
			r2Y = ((r1Y + (r1Y + r1H)) / 2) - (r2H / 2);
			
			cR = 25;
			//centers the circle within the Rectangle
			cX = ((r1X + (r1X + r1W)) / 2) - cR;
			cY = ((r1Y + (r1Y + r1H)) / 2) - cR;
		}
		
		public void framePaint(Graphics2D g2d)
		{
			//paints all components of the frame
			g2d.setColor(Color.BLACK);
			g2d.drawRect(r1X, r1Y, r1W, r1H);
			g2d.drawRect(r2X, r2Y, r2W, r2H);
			g2d.drawOval(cX, cY, cR * 2, cR * 2);
			
		}
	}
	
	//frame that users must get into in time
	PictureFrame frame;
	
	public PictureTime(int player)
	{
		super(5000, player, true);
		frame = new PictureFrame();
	}
	
	public void routine(Square[] players)
	{
		//if the item is still in effect
		if (this.timeRemaining() <= 0)
		{
			//play camera flash sound effect
			GameMain.cameraFlash.play();
			
			//starts timer (causes screen to flash white
			GameMain.flashTimer.startTimer();
			
			//iterates through all players
			for (int i = 0; i < players.length; i++)
			{
				//if the time is up and a players is not in the bounds and it is not the player that used the item,
				//they will be killed
				if ((!players[i].getBounds().intersects(new Rectangle(frame.r1X,frame.r1Y,frame.r1W,frame.r1H))) && (i != playerNumber - 1))
				{
					players[i].kill();
				}
			}
		}
	}
	
	//paints the frame on the screen
	public void paint(Graphics2D g2d)
	{
		frame.framePaint(g2d);
	}
}

//Item that swaps enemy locations
class TeleSwap extends ItemEffect
{
	public TeleSwap(int player)
	{
		super(0, player, false);
	}
	
	//swaps enemy locations
	public void routine(Square[] players)
	{
		//holds indexes of players to be swapped
		ArrayList <Integer> aliveIndexes = new ArrayList<Integer>();
		//temporarily holds coordinates of players to be swapped
		int[][] coordinates = new int[PlayerAndDifficultySelect.numAlivePlayers - 1][2];
		
		for (int i = 0; i < players.length; i++)
		{
			//if the player is alive and is not one of the players ot be swapped, they are added
			//to the index of players to be swapped
			if (players[i].alive && i != playerNumber - 1)
			{
				aliveIndexes.add(i);
			}
		}
		
		//used to add coordinates to coordinates array in order
		int index = 0;
		
		for (int i = 0; i < players.length; i++)
		{
			//if the player is in the alive indexes and they are alive
			if (aliveIndexes.contains(i) && players[i].alive)
			{
				//save the players coordinates
				coordinates[index][0] = players[i].x;
				coordinates[index][1] = players[i].y;
				index++;
			}
		}
		
		//this is used to ensure that all players are moved
		ArrayList <Integer> previousList = new ArrayList <Integer>(aliveIndexes);
		boolean shuffled = true;
		
		//this loop checks to see if any players are in the same location as they were before,
		//and reshuffles the locations if so
		do
		{
			shuffled = true;
			Collections.shuffle(aliveIndexes);
			for (int i = 0; i < aliveIndexes.size(); i++)
			{
				if (aliveIndexes.get(i).equals(previousList.get(i)))
				{
					shuffled = false;
				}
			}
		} while(!shuffled);
		
		index = 0;
		
		//finally, this swaps the player locations
		for (int i = 0; i < PlayerAndDifficultySelect.numAlivePlayers - 1; i++)
		{
			players[aliveIndexes.get(index)].x = coordinates[i][0];
			players[aliveIndexes.get(index)].y = coordinates[i][1];
			index++;
		}
		
		//this item removes itself
		this.remove();
	}
}

//allows player to kill other players with one touch (also turns the player red)
class TouchOfDeath extends ItemEffect
{
	public TouchOfDeath(int player)
	{
		super(3000, player, true);
	}
}
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

abstract class Ball
{
	//location
	int x;
	int y;
	
	//initial y location
	int initialY;
	
	//timer used to determine location
	CustomTimer BallTimer;
	
	//radius of ball
	final int radius;
	
	//downwards speed
	final int yVelocity;
	
	//color of ball
	final Color color;
	
	public Ball(int r, int v, Color c)
	{
		radius = r;
		yVelocity = v;
		color = c;
		//x is set to a random value between zero and the games width
		x = (int) (Math.random() * (GameMain.GAME_WIDTH - 2 * radius));
		//y is set to above the top of the screen
		y = 0 - (radius * 2);
		initialY = y;
		BallTimer = new CustomTimer();
	}
	
	public boolean moveBall(Square[] players)
	{
		//moves the ball based on time passed
		y = (int) (initialY + yVelocity * (BallTimer.getTime() / 1000.));
		
		//if the ball moves past the screen, the method returns true so the ball can be removed
		//from the arraylist
		if (initialY + yVelocity * (BallTimer.getTime() / 1000.) > GameMain.GAME_HEIGHT)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public void paint(Graphics2D g2d)
	{
		//paints ball
		g2d.setColor(color);
		g2d.fillOval(x, y, radius * 2, radius * 2);
		
		if (PlayerAndDifficultySelect.specialmode && !(this instanceof BallItem))
		{
			g2d.drawImage(GameMain.specialPicture, x, y, 2 * radius, 2 * radius, null);
		}
		
		//sets color to black so the border can be painted
		g2d.setColor(Color.BLACK);
		
		//set thickness of border
		g2d.setStroke(new BasicStroke(1));
		
		//draw border
		g2d.drawOval(x, y, radius * 2, radius * 2);
	}
}

class BallRegular extends Ball
{
	//this class makes the following variables the following values
	//final int radius = 20;
	//final int yVelocity = 160;
	BallRegular()
	{
		super(20, 160, Color.RED);
	}
}

class BallFast extends Ball
{
	//this class makes the following variables the following values
	//final int radius = 20;
	//final int yVelocity = 800;
	BallFast()
	{
		super(20, 800, Color.GREEN);
	}
}

class BallLarge extends Ball
{
	//this class makes the following variables the following values
	//final int radius = 50;
	//final int yVelocity = 400;
	BallLarge()
	{
		super(50,400, Color.ORANGE);
	}
}

class BallSideToSide extends Ball
{
	//holds initial X Location
	int initialXLocation;
	
	//initial speed in x direction
	int initialXVelocity;
	
	//acceleration (must be oppposite XVelocity as it will switch signs after ball passes initial point)
	int acceleration;
	
	//saves initial time (used for elapsed time)
	CustomTimer SideToSideTimer;
	
	//prevents ball from being locked in place initially
	boolean canReverse;
	
	//this class makes the following variables the following values
	//final int radius = 20;
	//final int yVelocity = 1;
	BallSideToSide()
	{
		super(20,160, Color.YELLOW);
		
		//sets initial x location to the initial x location
		initialXLocation = x;
		
		//this generates an initial x velocity between 50 and 1000 and an acceleration
		//greater than -50 and less than two times the initial X Velocity
		do
		{
			initialXVelocity = (int) (1000 * Math.random());
			acceleration = (int) (-1 * initialXVelocity * 2 * Math.random());
		}while (initialXVelocity < 50 || acceleration > -50);
		
		//instantiate and start timer
		SideToSideTimer = new CustomTimer();
		SideToSideTimer.startTimer();
		
		//this is set to true because the ball will otherwise constantly switch directions in the center
		//and will never move
		canReverse = true;
	}
	
	//overwrite base class's moveBall
	public boolean moveBall(Square[] players)
	{
		
		//used to see if signs changed
		int previousXLocation = x;
		
		//this insures that the ball is a sufficient distance from the center before the acceleration even can reverse
		//this ensures that the ball will not be stuck in the center
		if (x - initialXLocation > 10)
		{
			canReverse = false;
		}
		
		//formula for finding displacement (x) given initial speed (vi), elapsed time (t), and acceleration (a) is
		//x = vi * t + .5 * a * t * t
		x = (int)(initialXLocation + initialXVelocity * (SideToSideTimer.getTime() / 1000.) + (.5 * acceleration * Math.pow((SideToSideTimer.getTime() / 1000.), 2)));
		
		//if the coordinate passed the initial X Location and the canReverse boolean is true
		if (((x > initialXLocation && previousXLocation <= initialXLocation) || (x < initialXLocation && previousXLocation >= initialXLocation)) && !canReverse)
		{
			//restart the timer and reverse the XVelocity and acceleration
			SideToSideTimer.startTimer();
			initialXVelocity *= -1;
			acceleration *= -1;
		}
		
		//moves the ball in the y direction
		return super.moveBall(players);
	}
}

class BallHunter extends Ball
{
	//speed of the BallHunter
	int speed;
	
	BallHunter()
	{
		//0 because it has no yVelocity;
		super (20, 0, Color.BLACK);
		
		//speed is set to 3
		speed = 2;
		
		//start at bottom of screen rather than the top
		y = GameMain.GAME_HEIGHT;
	}
	
	public boolean moveBall(Square[] players)
	{	
		//finds closest player
		int closestPlayerIndex = 0;
		//this uses the distance formula
		double lowestDistance = 0;
		//finds if it is closer than anyone other than the first player and if the other player is alive
		for (int i = 0; i < players.length; i++)
		{
			if (lowestDistance == 0 && players[i].alive)
			{
				lowestDistance = Math.sqrt(Math.pow(((players[i].x + Square.size / 2) - (x + radius)), 2) + Math.pow(((players[i].y + Square.size / 2) - (y + radius)), 2));
				closestPlayerIndex = i;
			}
			if ((lowestDistance > Math.sqrt(Math.pow(((players[i].x + Square.size / 2) - (x + radius)), 2) + Math.pow(((players[i].y + Square.size / 2) - (y + radius)), 2))) && players[i].alive)
			{
				lowestDistance = Math.sqrt(Math.pow(((players[i].x + Square.size / 2) - (x + radius)), 2) + Math.pow(((players[i].y + Square.size / 2) - (y + radius)), 2));
				closestPlayerIndex = i;
			}
		}
		
		//calculates x and y distance from player
		int xDistance = (players[closestPlayerIndex].x + Square.size / 2) - (x + radius);
		int yDistance = (players[closestPlayerIndex].y + Square.size / 2) - (y + radius);
		
		//this is the ratio of the distance in each direction
		double ratio = ((double) (yDistance)) / xDistance;
		
		//the formula to find x velocity given the ratio is:
		//x = speed / sqrt(1 + ratio * ratio) the reason that the sign of xDistance is checked is because
		//the derivation of this formula introduces a sqrt, which yields a +- sign
		
		if (xDistance > 0)
		{
			x += Math.round(speed / Math.sqrt(1 + Math.pow(ratio, 2)));
		}
		else
		{
			x -= Math.round(speed / Math.sqrt(1 + Math.pow(ratio, 2)));
		}
		
		//the formula to find y velocity given the ratio is:
		//y = speed *  sqrt(1 - (1/ (1 + ratio * ratio))) the reason that the sign of yDistance is checked is because
		//the derivation of this formula includes a sqrt, which yields a +- sign
		if (yDistance > 0)
		{
			y += Math.round(speed * Math.sqrt(1 - (1 / (1 + Math.pow(ratio,2)))));
		}
		else
		{
			y -= Math.round(speed * Math.sqrt(1 - (1 / (1 + Math.pow(ratio,2)))));
		}
		
		
		//this ball will never be deleted
		return false;
	}
}

//this is the actual ball that falls that represents an item
class BallItem extends Ball
{
	BallItem()
	{
		super(20, 160, Color.ORANGE);
	}
	
	public void paint(Graphics2D g2d)
	{
		super.paint(g2d);
		
		//write the word item on the ball
		g2d.setFont(new Font("Arial", Font.BOLD, 15));
		g2d.drawString("Item", x + 5, y + radius + 4);
	}
}
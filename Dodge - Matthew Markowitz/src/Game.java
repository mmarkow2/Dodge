import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel{
	
	//Item in effect
	public static ItemEffect item;
	
	//used to regulate ball drop interval speed
	CustomTimer BallSendTimer;
	long nextTime;
	
	//int to hold user's score
	int score;
	
	//holds Balls
	ArrayList<Ball> Ball;
	
	//holds users
	Square User[];
	
	//boolean that ensures that the hunter ball is only sent once
	boolean hunterBallSent;
	
	//modifies ball locations at top to ensure there is valid space for square to pass through
	void reAdjustBalls (ArrayList<Ball> ballInput)
	{
		for (int i = 0; i < ballInput.size(); i++)
		{
			// checks if the ball is at the top (only balls at top above screen will be moved)
			if (!ballInput.get(i).BallTimer.hasStarted())
			{
				for (int k = 0; k < ballInput.size(); k++)
				{
					//gets y location of ball to compare current ball against (ball 2)
						int y2 = (int) (ballInput.get(k).initialY + ballInput.get(k).yVelocity * (ballInput.get(k).BallTimer.getTime() / 1000.));
					
						//this insures that we dont check the ball against itself
						if (i != k)
						{
						
							//if the distance between the two centers is less than the sum of the two radiuses plus MIN_DISTANCE, then the two circles are within MIN_DISTANCE pixels of each other
							if (Math.sqrt(Math.pow((ballInput.get(i).x + ballInput.get(i).radius) - (ballInput.get(k).x + ballInput.get(k).radius),2) + Math.pow((ballInput.get(i).y + ballInput.get(i).radius) - (y2 + ballInput.get(k).radius), 2)) < ballInput.get(i).radius + ballInput.get(k).radius + GameMain.MIN_DISTANCE)
							{
							
								//if the distance is less than MIN_DISTANCE between the circles, delete the original ball
									ballInput.remove(i);
									//decrement the i because remove function shifts indexes
									i--;
							}
						}
				}
			}
		}
		for (int i = 0; i < ballInput.size(); i++)
		{
			//since this ball passed the check for distance, it will be started down the screen
			if (!ballInput.get(i).BallTimer.hasStarted())
			{
				ballInput.get(i).BallTimer.startTimer();
			}
		}
	}
	
	//initial chances of getting a fast ball, a large ball, a side-to-side ball, and the chance of getting an item
	//item chance is higher than other chances because it is a static number, while the chances of other balls constantly increases
	double fastBallchance = .01;
	double largeBallchance = .01;
	double sideToSideBallChance = .01;
	double itemChance = .1;
	
	//returns the score
	int getScore()
	{
		//returns score
		return score;
	}
	
	//increments the score
	public void increaseScore()
	{
		//increases the user's score
		score++;
	}
	
	//this checks if there are any items currently falling
	//this is used to ensure that there is only ever one ball item falling at a time
	public boolean containsItem()
	{
		for (int i = 0; i < Ball.size(); i++)
		{
			if (Ball.get(i) instanceof BallItem)
			{
				return true;
			}
		}
		return false;
	}
	
	//this paints the game
	public void paintGame(Graphics2D g)
	{
		//paints the balls
		g.setColor(Color.RED);
		for (int i = 0; i < Ball.size(); i++)
		{
			Ball.get(i).paint(g);
		}
		
		//paints the user
		g.setColor(Color.GREEN);
		
		//this checks all players to see if any player has the touch of death item so the player can be displayed
		//as red
		for (int i = 0; i < User.length; i++)
		{
			if (item instanceof TouchOfDeath)
			{
				if (i == item.playerNumber - 1)
				{
					//the true indicates that the player should be painted as red
					User[i].paintSquare(g, true);
				}
				else
				{
					//user painted normal
					User[i].paintSquare(g, false);
				}
			}
			else
			{
				//user painted normal
				User[i].paintSquare(g, false);
			}
		}
	}
	
	//routine that updates the entire game
	public void routine()
	{
		//runs item's routine if there is one
		if (item != null)
		{
			item.routine(User);
			
			//if it is time to remove the item, the item is removed
			if (item.timeToRemove())
			{
				item = null;
			}
		}
		
		//if the user dies, move them gradiually down the screen
		for (int i = 0; i < User.length; i++)
		{
			if (!User[i].getLifeStatus())
			{
				User[i].moveSquareDead();
			}
		}
		
		//this iterates through every ball and checks for collisions
		for (int i = 0; i < Ball.size(); i++)
		{
			//if the ball collides with the user, give the user a game over
			for (int k = 0; k < User.length; k++)
			{	
				if (collision(Ball.get(i), User[k]))
				{
					//dont kill the player if the collided ball was an item
					if ((Ball.get(i) instanceof BallItem) && (User[k].alive))
					{
						//remove the item from the screen
						Ball.remove(i);
						i--;
						
						//double used to randomly pick item
						double chance;
						
						do
						{
							chance = Math.random();
							//if there is less than 3 people, dont pick the teleswap item
						} while((chance < .66 && chance >= .33) && PlayerAndDifficultySelect.numAlivePlayers <= 2);
						
						if (chance < .33)
						{
							item = new PictureTime(k + 1);
						}
						else if (chance < .66)
						{
							item = new TeleSwap(k + 1);
						}
						else
						{
							item = new TouchOfDeath(k + 1);
						}
					}
					else
					{
						//if the user did not hit an item, kill the user
						User[k].kill();
					}
				}
				if (i < 0)
				{
					//this prevents out of bounds errors
					break;
				}
			}
			
			if (i < 0)
			{
				//this prevents out of bounds errors
				break;
			}
			
			//move the ball and check if ball moves off the screen and if so, remove it from the ball arraylist
			if (Ball.get(i).moveBall(User))
			{
				Ball.remove(i);
				//if the ball is removed, the i must be decremented because the indexes will shift
				i--;
			}
		}
		
		//displays the game over screen if it is a single player game.
		//it otherwise displays the winner
		if (PlayerAndDifficultySelect.numPlayers != 1)
		{
			if (PlayerAndDifficultySelect.numAlivePlayers <= 1)
			{
				for (int n = 0; n < User.length; n++)
				{
					if (User[n].getLifeStatus())
					{
						JOptionPane.showMessageDialog(this, "Congratulations Player " + (n + 1) + ". You win.", "Congratulations", JOptionPane.INFORMATION_MESSAGE );
						
						//output the score to the score file
						try {
							if (score > GameMain.highScore)
							{
								GameMain.scoreOutput.write(String.valueOf(score));
							}
							else
							{
								GameMain.scoreOutput.write(String.valueOf(GameMain.highScore));
							}
							GameMain.scoreOutput.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.exit(0);
					}
				}
			}
		}
		else
		{
			if (PlayerAndDifficultySelect.numAlivePlayers <= 0)
			{
				JOptionPane.showMessageDialog(this,
			    		"Your Final Score is " + score + ".",
			    		"Game Over",
			    		JOptionPane.INFORMATION_MESSAGE);
				
				//output the score to the score file
				try {
					if (score > GameMain.highScore)
					{
						GameMain.scoreOutput.write(String.valueOf(score));
					}
					else
					{
						GameMain.scoreOutput.write(String.valueOf(GameMain.highScore));
					}
					GameMain.scoreOutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.exit(0);
			}
		}
		
		//if it is time to send another ball
		if (BallSendTimer.getTime() > nextTime)
		{
			//restart the timer
			BallSendTimer.startTimer();
			
			//make next ball be sent in a quicker period of time
			//since the time is being decreased untill it will be decreased again,
			//this is sort of an exponential function, which accounts for the sharp and sudden
			//increase in difficulty
			if (nextTime > 10)
			{
				nextTime -= 10;
			}
			
			
			//decides if a which ball should be added to the screen
			double randdouble = Math.random();
			if (randdouble < fastBallchance)
			{
				Ball.add(new BallFast());
			}
			else if (randdouble < fastBallchance + largeBallchance)
			{
				Ball.add(new BallLarge());
			}
			else if (randdouble < fastBallchance + largeBallchance + sideToSideBallChance)
			{
				Ball.add(new BallSideToSide());
			}
			//in addition to seeing if the item was picked, it is also checked if there is more than one player
			//and if there are no items already in effect, and it ensures that there are no other items already on the screen
			else if ((randdouble < fastBallchance + largeBallchance + sideToSideBallChance + itemChance) && (PlayerAndDifficultySelect.numAlivePlayers >= 2) && (item == null) && (!containsItem()))
			{
				Ball.add(new BallItem());
			}
			else
			{
				Ball.add(new BallRegular());
			}
			
			//deletes ball locations to ensure valid space for player to pass through
			reAdjustBalls(Ball);
			
			//increases the chance of getting certain balls as long as the total chance of a special ball is less than 60 % with the other 40% being normal balls
			if (.6 > fastBallchance + largeBallchance + sideToSideBallChance + itemChance)
			{
				fastBallchance += .005;
				largeBallchance += .001;
				sideToSideBallChance += .001;
			}
		}
		
		//send the infamous hunter Ball when the player reaches 3000 points
		//this ball follows the player and only appears once
		if (score > 10000 && !hunterBallSent)
		{
			//ensure that the hunter ball is only sent once
			hunterBallSent = true;
			
			Ball.add(new BallHunter());
		}
	}
	
	public Game()
	{
		//will change from null when a player gets an item
		item = null;
		
		//starts timer that determines ball drop interval speed
		BallSendTimer = new CustomTimer();
		BallSendTimer.startTimer();
		//initial delay is one second
		nextTime = 1000;
		
		//initializes score and objects to defaults
		score = 0;
		Ball = new ArrayList<Ball>();
		User = new Square[PlayerAndDifficultySelect.numPlayers];
		//false because hunter ball has not been sent once
		hunterBallSent = false;
		
		//puts all users in correct locations
		User[0] = new Square(GameMain.GAME_WIDTH / 4 - Square.size / 2, GameMain.GAME_HEIGHT / 4 + Square.size / 2, Color.GREEN);
		
		if (PlayerAndDifficultySelect.numPlayers >= 2)
		{
			User[1] = new Square(GameMain.GAME_WIDTH * 3 / 4 - Square.size / 2, GameMain.GAME_HEIGHT / 4 + Square.size / 2, Color.BLUE);
			
			if (PlayerAndDifficultySelect.numPlayers >= 3)
			{
				User[2] = new Square(GameMain.GAME_WIDTH / 4 - Square.size / 2, GameMain.GAME_HEIGHT * 3 / 4 + Square.size / 2, Color.ORANGE);
				
				if (PlayerAndDifficultySelect.numPlayers >= 4)
				{
					User[3] = new Square(GameMain.GAME_WIDTH * 3 / 4 - Square.size / 2, GameMain.GAME_HEIGHT * 3 / 4 + Square.size / 2, Color.BLACK);
				}
			}
		}
	}

	//returns true if the ball collides with the square
	//it works by determining the closest point on the square to the circle,
	//and sees if the distance between that point and the circle's center point
	//is less than the radius's length
	public boolean collision(Ball Ball, Square Squareoriginal)
	{
		//makes a rectangle object equal to the squares values
		Rectangle square = Squareoriginal.getBounds();
		
		//coordinates of points on the square that is closest to the ball
		int closex;
		int closey;
		
		//if the square is past the ball's center on the right, the closest point is on the left of the square
		if (square.x > Ball.x + Ball.radius)
		{
			closex = square.x;
		}
		//if the square is to the left of the ball's center, the closest point is the right of the square
		else if (square.x + square.width < Ball.x + Ball.radius)
		{
			closex = square.x + square.width;
		}
		//otherwise, the closest x point is at the x point of the ball's center
		else
		{
			closex = Ball.x + Ball.radius;
		}
		
		
		//if the square is below the ball on the screen, the closest y point is at the top of the square
		if (square.y > Ball.y + Ball.radius)
		{
			closey = square.y;
		}
		//if the square is above the ball on the screen, the closest y point is on the bottom of the square
		else if (square.y + square.height < Ball.y + Ball.radius)
		{
			closey = square.y + square.height;
		}
		//otherwise, the closest x point is equal to the ball's center y point
		else
		{
			closey = Ball.y + Ball.radius;
		}
		
		//if the distance between the closest point on the square and the center of the ball is less than
		//the radius of the ball, the objects are colliding
		if (Math.sqrt(Math.pow((closex - (Ball.x + Ball.radius)), 2) + Math.pow((closey - (Ball.y + Ball.radius)),2)) < Ball.radius)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
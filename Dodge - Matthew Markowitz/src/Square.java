import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

class Square
{
	//holds coordinates and speeds
	int x;
	int y;
	final int xVelocity = 8;
	final int yVelocity = 8;
	//dimension of square
	final static int size = 40;
	
	//determines if the user is not dead
	boolean alive = true;
	
	//color of player
	Color color;
	
	//constructor that accepts x and y locations and color of square
	public Square(int x, int y, Color color)
	{
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	//returns rectangle that is equivalent to square locations and size
	//this is used to check collision with built in rectangle collision function
	public Rectangle getBounds()
	{
		return new Rectangle(x, y, size, size);
	}
	
	//Red boolean indicates if the user should be painted as red because of TouchOfDeath Item
	public void paintSquare(Graphics2D g, boolean red)
	{
		if (red)
		{
			g.setColor(Color.RED);
		}
		else
		{
			g.setColor(color);
		}
		g.fillRect(x, y, size, size);
		
		//this paints border of square
		g.setColor(Color.BLACK);
		
		//set thickness of border
		g.setStroke(new BasicStroke(1));
		
		//draw border
		g.drawRect(x, y, size, size);
	}
	
	//moves square in the x direction
	//if direction is positive, the user moves right
	public void moveSquareX(int direction, Square player[])
	{
		if (alive)
		{
			//if the user should move right
			if (direction > 0)
			{
				//move the user right
				x += xVelocity;
				//if the user moves off the screen, move them back
				if (x  + size > GameMain.GAME_WIDTH)
				{
					x = GameMain.GAME_WIDTH - size;
				}
				
				for (int i = 0; i <player.length; i++)
				{
					//if two users intersected
					if (this.getBounds().intersects(player[i].getBounds()) && !this.equals(player[i]))
					{
						//move player to outside other player
						x = player[i].x - size;
						//if the collided enemy had a touch of death item, the user is killed
						if (Game.item instanceof TouchOfDeath)
						{
							for (int k = 0; k < player.length; k++)
							{
								//finds this players index in k and sees if this user has the touch of death item
								//if so the other player is killed
								if (this.equals(player[k]))
								{
									if (Game.item.playerNumber == k + 1)
									{
										player[i].kill();
									}
									
									break;
								}
							}
							
							//if the user hit someone with the item, they are killed
							if (Game.item.playerNumber == i + 1)
							{
								this.kill();
							}
						}
					}
				}
			}
			else
			{
				//move the user left
				x -= xVelocity;
				//if the user moves off the screen, move them back
				if (x < 0)
				{
					x = 0;
				}
				
				//if two users intersected (this function is mostly the same as the one above
				for (int i = 0; i <player.length; i++)
				{
					if (this.getBounds().intersects(player[i].getBounds()) && !this.equals(player[i]))
					{
						x = player[i].x + size;
						if (Game.item instanceof TouchOfDeath)
						{
							for (int k = 0; k < player.length; k++)
							{
								if (this.equals(player[k]))
								{
									if (Game.item.playerNumber == k + 1)
									{
										player[i].kill();
									}
									
									break;
								}
							}
							
							if (Game.item.playerNumber == i + 1)
							{
								this.kill();
							}
						}
					}
				}
			}
		}
	}
	
	//moves square in the y direction
	public void moveSquareY(int direction, Square player[])
	{
		if (alive)
		{
			//if the user should move down
			if (direction > 0)
			{
				//move the user down
				y += yVelocity;
				//if the user moves off the screen, move them back
				if (y  + size > GameMain.GAME_HEIGHT)
				{
					y = GameMain.GAME_HEIGHT - size;
				}
				
				for (int i = 0; i <player.length; i++)
				{
					//same as above function
					if (this.getBounds().intersects(player[i].getBounds()) && !this.equals(player[i]))
					{
						y = player[i].y - size;
						if (Game.item instanceof TouchOfDeath)
						{
							for (int k = 0; k < player.length; k++)
							{
								if (this.equals(player[k]))
								{
									if (Game.item.playerNumber == k + 1)
									{
										player[i].kill();
									}
									
									break;
								}
							}
							
							if (Game.item.playerNumber == i + 1)
							{
								this.kill();
							}
						}
					}
				}
			}
			else
			{
				//move the user up
				y -= yVelocity;
				//if the user moves too high up, move them back
				if (y < 50)
				{
					y = 50;
				}
				
				for (int i = 0; i <player.length; i++)
				{
					//same as above function
					if (this.getBounds().intersects(player[i].getBounds()) && !this.equals(player[i]))
					{
						y = player[i].y + size;
						if (Game.item instanceof TouchOfDeath)
						{
							for (int k = 0; k < player.length; k++)
							{
								if (this.equals(player[k]))
								{
									if (Game.item.playerNumber == k + 1)
									{
										player[i].kill();
									}
									
									break;
								}
							}
							
							if (Game.item.playerNumber == i + 1)
							{
								this.kill();
							}
						}
					}
				}
			}
		}
	}
	
	//moves square down when dead
	public void moveSquareDead()
	{
		y += 5;
	}
	
	public void kill()
	{
		//this insures that numAlivePlayers will only be decremented once for each player
		if (alive)
		{
			PlayerAndDifficultySelect.numAlivePlayers--;
		}
		alive = false;
	}
	
	//tells if the player is alive or not
	public boolean getLifeStatus()
	{
		return alive;
	}
}
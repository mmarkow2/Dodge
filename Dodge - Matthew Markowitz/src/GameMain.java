import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;


public class GameMain extends JFrame{
	//used to input and save score
	BufferedReader scoreInput;
	public static BufferedWriter scoreOutput;
	
	//used for special mode
	public static BufferedImage specialPicture;
	
	public static int highScore;
	
	//music
	static AudioClip clip;
	//flash noise
	static AudioClip cameraFlash;
	
	//this is timer that times the amount of time white should appear on the screen to represent the flash
	public static CustomTimer flashTimer;
	
	public Game game;
	
	//code for the following two objects come from the website http://www.realapplets.com/tutorial/doublebuffering.html
	//it is used to double buffer the screen
	Image offscreen;
	Graphics2D offscreenGraphics;
	
	//dimensions of the game. It is the screen size by default
	public static int GAME_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height - 40;
	public static int GAME_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	
	//minimum distance between balls
	public static final int MIN_DISTANCE = 80;
	
	//label to hold score
	public Label ScoreLabel;
	
	//ArrayList to hold buttons pressed
	static ArrayList<Integer> ButtonsPressed;
	
	public void paint(Graphics g)
	{
		//code for the following two objects come from the website http://www.realapplets.com/tutorial/doublebuffering.html
		//it is used to double buffer the screen
		offscreen = createImage(GAME_WIDTH, GAME_HEIGHT);
		offscreenGraphics = (Graphics2D) offscreen.getGraphics();
		
		
		//resets the score label
		if (Game.item != null)
		{
			//if there is an item, give time remaining
			ScoreLabel.setText("Score: " + String.valueOf(game.getScore()) + "    High Score: " + (highScore > game.getScore() ? highScore : game.getScore()) + "          Time: " + String.valueOf(Game.item.timeRemaining()) + " seconds");
		}
		else
		{
			//otherwise, just give the score and high score
			ScoreLabel.setText("Score: " + String.valueOf(game.getScore()) + "    High Score: " + (highScore > game.getScore() ? highScore : game.getScore()));
		}
		
		//shows white if the camera went off
		if (flashTimer.hasStarted())
		{
			offscreenGraphics.setColor(Color.WHITE);
			offscreenGraphics.fillRect(0, 0, GAME_WIDTH, GAME_WIDTH);
		}
		else
		{
			//runs the game's paint method
			game.paintGame(offscreenGraphics);
		
			//if there is a picture time item, the frame must be painted
			if (Game.item instanceof PictureTime)
			{
				//paint the frame on the game if the PictureTime item is active
				Game.item.paint(offscreenGraphics);
			}
		}
		//repaints the screen
		g.drawImage(offscreen,0,0,this);
	}
	
	//from website http://www.realapplets.com/tutorial/doublebuffering.html
    public void update(Graphics g) 
    { 
         paint(g); 
    } 
	
	//from tetris game
    //used to find user buttons pressed
	class TAdapter extends KeyAdapter {
	    
	    @Override
	    public void keyPressed(KeyEvent e) {

	        switch (e.getKeyCode()) {
	            
	        //if the user presses a button, it is added to the ButtonsPressed arraylist to be processed later
	        case KeyEvent.VK_LEFT:
	        	if (!ButtonsPressed.contains(KeyEvent.VK_LEFT))
	        	{
	        		ButtonsPressed.add(new Integer(KeyEvent.VK_LEFT));
	        	}
	            break;
	            
	        case KeyEvent.VK_RIGHT:
	        	if (!ButtonsPressed.contains(KeyEvent.VK_RIGHT))
	        	{
	        		ButtonsPressed.add(new Integer(KeyEvent.VK_RIGHT));
	        	}
	        	break;
	        	
		        case KeyEvent.VK_UP:
		        	if (!ButtonsPressed.contains(KeyEvent.VK_UP))
		        	{
		        		ButtonsPressed.add(new Integer(KeyEvent.VK_UP));
		        	}
		            break;
		            
		        case KeyEvent.VK_DOWN:
		        	if (!ButtonsPressed.contains(KeyEvent.VK_DOWN))
		        	{
		        		ButtonsPressed.add(new Integer(KeyEvent.VK_DOWN));
		        	}
		        	break;
		        	
		        case 'A':
		        	if (!ButtonsPressed.contains(new Integer('A')))
		        	{
		        		ButtonsPressed.add(new Integer('A'));
		        	}
		        	break;
		        	
		        case 'D':
		        	if (!ButtonsPressed.contains(new Integer('D')))
		        	{
		        		ButtonsPressed.add(new Integer('D'));
		        	}
		        	break;
		        	
		        case 'S':
		        	if (!ButtonsPressed.contains(new Integer('S')))
		        	{
		        		ButtonsPressed.add(new Integer('S'));
		        	}
		        	break;
		        	
		        case 'W':
		        	if (!ButtonsPressed.contains(new Integer('W')))
		        	{
		        		ButtonsPressed.add(new Integer('W'));
		        	}
		        	break;
		        	
		        case 'G':
		        	if (!ButtonsPressed.contains(new Integer('G')))
		        	{
		        		ButtonsPressed.add(new Integer('G'));
		        	}
		        	break;
		        	
		        case 'J':
		        	if (!ButtonsPressed.contains(new Integer('J')))
		        	{
		        		ButtonsPressed.add(new Integer('J'));
		        	}
		        	break;
		        	
		        case 'H':
		        	if (!ButtonsPressed.contains(new Integer('H')))
		        	{
		        		ButtonsPressed.add(new Integer('H'));
		        	}
		        	break;
		        	
		        case 'Y':
		        	if (!ButtonsPressed.contains(new Integer('Y')))
		        	{
		        		ButtonsPressed.add(new Integer('Y'));
		        	}
		        	break;
		        	
		        case 'L':
		        	if (!ButtonsPressed.contains(new Integer('L')))
		        	{
		        		ButtonsPressed.add(new Integer('L'));
		        	}
		        	break;
		        	
		        case KeyEvent.VK_QUOTE:
		        	if (!ButtonsPressed.contains(KeyEvent.VK_QUOTE))
		        	{
		        		ButtonsPressed.add(new Integer(KeyEvent.VK_QUOTE));
		        	}
		        	break;
		        	
		        case 'P':
		        	if (!ButtonsPressed.contains(new Integer('P')))
		        	{
		        		ButtonsPressed.add(new Integer('P'));
		        	}
		        	break;
		        	
		        case KeyEvent.VK_SEMICOLON:
		        	if (!ButtonsPressed.contains(KeyEvent.VK_SEMICOLON))
		        	{
		        		ButtonsPressed.add(new Integer(KeyEvent.VK_SEMICOLON));
		        	}
		        	break;
		        	
	        }
	    }
	    
	    public void keyReleased(KeyEvent e)
	    {
	        switch (e.getKeyCode()) {
            
	        //checks which keys were released and removes them from the buttonspressed arraylist
	        case KeyEvent.VK_LEFT:
	        	ButtonsPressed.remove(ButtonsPressed.indexOf(KeyEvent.VK_LEFT));
	            break;
	            
	        case KeyEvent.VK_RIGHT:
	        	ButtonsPressed.remove(ButtonsPressed.indexOf(KeyEvent.VK_RIGHT));
	        	break;
	        	
		        case KeyEvent.VK_UP:
		        	ButtonsPressed.remove(ButtonsPressed.indexOf(KeyEvent.VK_UP));
		            break;
		            
		        case KeyEvent.VK_DOWN:
		        	ButtonsPressed.remove(ButtonsPressed.indexOf(KeyEvent.VK_DOWN));
		        	break;
		        	
		        case 'A':
		        	ButtonsPressed.remove(new Integer('A'));
		        	break;
		        	
		        case 'D':
		        	ButtonsPressed.remove(new Integer('D'));
		        	break;
		        	
		        case 'S':
		        	ButtonsPressed.remove(new Integer('S'));
		        	break;
		        	
		        case 'W':
		        	ButtonsPressed.remove(new Integer('W'));
		        	break;
		        	
		        case 'G':
		        	ButtonsPressed.remove(new Integer('G'));
		        	break;
		        	
		        case 'J':
		        	ButtonsPressed.remove(new Integer('J'));
		        	break;
		        	
		        case 'H':
		        	ButtonsPressed.remove(new Integer('H'));
		        	break;
		        	
		        case 'Y':
		        	ButtonsPressed.remove(new Integer('Y'));
		        	break;
		        	
		        case 'L':
		        	ButtonsPressed.remove(new Integer('L'));
		        	break;
		        	
		        case KeyEvent.VK_QUOTE:
		        	ButtonsPressed.remove(new Integer(KeyEvent.VK_QUOTE));
		        	break;
		        	
		        case 'P':
		        	ButtonsPressed.remove(new Integer('P'));
		        	break;
		        	
		        case KeyEvent.VK_SEMICOLON:
		        	ButtonsPressed.remove(new Integer(KeyEvent.VK_SEMICOLON));
		        	break;
	        }
	    }
	    
	    
	}
	
	public GameMain()
	{
		try
		{
			//opens score.txt
			scoreInput = new BufferedReader(new FileReader("Score.txt"));
			
			//set high score equal to int in scoreInput
			String highScoreString;
			if ((highScoreString = scoreInput.readLine()) != null)
			{
				highScore = Integer.parseInt(highScoreString);
			}
			else
			{
				highScore = 0;
			}
		}
		catch (FileNotFoundException err)
		{
			highScore = 0;
		}
		catch (IOException err)
		{
			highScore = 0;
		}
		catch (NumberFormatException err)
		{
			highScore = 0;
		}
		
		//open output that will replace the high score
		try {
			scoreOutput = new BufferedWriter(new FileWriter("Score.txt"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			specialPicture = ImageIO.read(new File("res/specialpicture.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			//loads music and plays it
			clip = Applet.newAudioClip(new File("multitaskmusic.wav").toURI().toURL());
			clip.loop();
		} catch (MalformedURLException e) {
			System.err.println("Unable to Load Music");
		}
		
		try {
			//loads music and plays it
			cameraFlash = Applet.newAudioClip(new File("camerasound.wav").toURI().toURL());
		} catch (MalformedURLException e) {
			System.err.println("Unable to Load flash");
		}
		
		flashTimer = new CustomTimer();
		
		//creates label to hold user score
		ScoreLabel = new Label();
		
		//sets up JFrame with default settings
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Dodge");
		this.setLayout(new BorderLayout());
		this.add(ScoreLabel,BorderLayout.NORTH);
		this.setSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		
		//allows program to recieve key input
		this.addKeyListener(new TAdapter());
		
		//holds all buttons that are being pressed
		ButtonsPressed = new ArrayList<Integer>();
		
		//game that actually runs
		game = new Game();
		
		//timer that checks every millisecond to check user input and move square accordingly
		Timer mainTimer = new Timer(10, new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				//repaints the screen
				repaint();
				
				//stops the flash after 250 milliseconds
				if (flashTimer.getTime() > 250)
				{
					flashTimer.stopTimer();
				}
				
				//increases the user's score
				game.increaseScore();
				//moves square according to buttons held in buttonpressed collection
				for (int i = 0; i < ButtonsPressed.size(); i++)
				{
					if (ButtonsPressed.get(i).equals(KeyEvent.VK_LEFT))
					{
						//moves square to the left
						game.User[0].moveSquareX(-1, game.User);
					}
					if (ButtonsPressed.get(i).equals(KeyEvent.VK_RIGHT))
					{
						//moves square to the right
						game.User[0].moveSquareX(1, game.User);
					}
					if (ButtonsPressed.get(i).equals(KeyEvent.VK_UP))
					{
						//moves square up
						game.User[0].moveSquareY(-1, game.User);
					}
					if (ButtonsPressed.get(i).equals(KeyEvent.VK_DOWN))
					{
						//moves square down
						game.User[0].moveSquareY(1, game.User);
					}
					
					if (PlayerAndDifficultySelect.numPlayers >= 2)
					{
						if (ButtonsPressed.get(i).equals(new Integer('A')))
						{
							//moves square to the left
							game.User[1].moveSquareX(-1, game.User);
						}
						if (ButtonsPressed.get(i).equals(new Integer('D')))
						{
							//moves square to the right
							game.User[1].moveSquareX(1, game.User);
						}
						if (ButtonsPressed.get(i).equals(new Integer('W')))
						{
							//moves square up
							game.User[1].moveSquareY(-1, game.User);
						}
						if (ButtonsPressed.get(i).equals(new Integer('S')))
						{
							//moves square down
							game.User[1].moveSquareY(1, game.User);
						}
						
						
						if (PlayerAndDifficultySelect.numPlayers >= 3)
						{
							if (ButtonsPressed.get(i).equals(new Integer('G')))
							{
								//moves square to the left
								game.User[2].moveSquareX(-1, game.User);
							}
							if (ButtonsPressed.get(i).equals(new Integer('J')))
							{
								//moves square to the right
								game.User[2].moveSquareX(1, game.User);
							}
							if (ButtonsPressed.get(i).equals(new Integer('Y')))
							{
								//moves square up
								game.User[2].moveSquareY(-1, game.User);
							}
							if (ButtonsPressed.get(i).equals(new Integer('H')))
							{
								//moves square down
								game.User[2].moveSquareY(1, game.User);
							}
							
							
							if (PlayerAndDifficultySelect.numPlayers >= 4)
							{
								if (ButtonsPressed.get(i).equals(new Integer('L')))
								{
									//moves square to the left
									game.User[3].moveSquareX(-1, game.User);
								}
								if (ButtonsPressed.get(i).equals(KeyEvent.VK_QUOTE))
								{
									//moves square to the right
									game.User[3].moveSquareX(1, game.User);
								}
								if (ButtonsPressed.get(i).equals(new Integer('P')))
								{
									//moves square up
									game.User[3].moveSquareY(-1, game.User);
								}
								if (ButtonsPressed.get(i).equals(KeyEvent.VK_SEMICOLON))
								{
									//moves square down
									game.User[3].moveSquareY(1, game.User);
								}
							}
						}
					}
				}
				//moves all objects in the game
				game.routine();
			}
		});
		//starts the mainTimer
		mainTimer.start();
	}
}

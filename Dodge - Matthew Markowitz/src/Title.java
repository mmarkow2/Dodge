import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class Title extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Title frame = new Title();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Title() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Dodge");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel Title = new JLabel("Dodge");
		Title.setBounds(103, 0, 246, 94);
		Title.setForeground(Color.BLUE);
		Title.setFont(new Font("Tahoma", Font.BOLD, 72));
		contentPane.add(Title);
		
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem exit = new JMenuItem("Exit");
		
		JMenu resolution = new JMenu("Resolution");
		JMenuItem setResolution = new JMenuItem("Set Resolution");
		
		file.add(exit);
		resolution.add(setResolution);
		
		menu.add(file);
		menu.add(resolution);
		
		exit.addActionListener(new ActionListener()
		{
			//if the user hits the exit button, quit the game

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		
		setResolution.addActionListener(new ActionListener()
		{
			//if the user hits the set resolution button, bring up a dialog box to enter the desired width and height
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try
				{
					GameMain.GAME_WIDTH = Integer.parseInt(JOptionPane.showInputDialog("Enter Width (in Pixels)"));
					GameMain.GAME_HEIGHT = Integer.parseInt(JOptionPane.showInputDialog("Enter Height (in Pixels)"));
					if ((GameMain.GAME_WIDTH <= 0) || (GameMain.GAME_WIDTH > java.awt.Toolkit.getDefaultToolkit().getScreenSize().width) || (GameMain.GAME_HEIGHT <= 0) || (GameMain.GAME_HEIGHT > java.awt.Toolkit.getDefaultToolkit().getScreenSize().height))
					{
						throw new InvalidDimensionException();
					}
				}
				catch (NumberFormatException err)
				{
					//if the user enters a letter, set the dimensions to default
					GameMain.GAME_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
					GameMain.GAME_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height - 40;
				}
				catch (InvalidDimensionException err)
				{
					GameMain.GAME_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
					GameMain.GAME_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height - 40;
				}
			}
		});
		
		this.setJMenuBar(menu);
		
		JButton btnStart = new JButton("Start Game");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
        		PlayerAndDifficultySelect menu = new PlayerAndDifficultySelect();
        		setVisible(false);
        		menu.setVisible(true);
			}
		});
		btnStart.setBounds(123, 128, 198, 52);
		contentPane.add(btnStart);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(123, 191, 198, 52);
		contentPane.add(btnExit);
	}
}

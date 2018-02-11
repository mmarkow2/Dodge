import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PlayerAndDifficultySelect extends JFrame {
	
	JRadioButton radOnePlayers;
	JRadioButton radTwoPlayers;
	JRadioButton radThreePlayers;
	JRadioButton radFourPlayers;
	
	JCheckBox specialModeCheckBox;
	
	public static int numPlayers = 1;
	public static int numAlivePlayers = 1;
	
	//whether the user is in special mode
	public static boolean specialmode;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public PlayerAndDifficultySelect() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Dodge");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelPlayerSelect = new JLabel("How many players?");
		labelPlayerSelect.setBounds(163, 11, 128, 14);
		contentPane.add(labelPlayerSelect);
		
		radOnePlayers = new JRadioButton("One Player");
		radOnePlayers.setBounds(163, 32, 109, 23);
		contentPane.add(radOnePlayers);
		
		radTwoPlayers = new JRadioButton("Two Players");
		radTwoPlayers.setBounds(163, 58, 109, 23);
		contentPane.add(radTwoPlayers);
		
		radThreePlayers = new JRadioButton("Three Players");
		radThreePlayers.setBounds(163, 84, 109, 23);
		contentPane.add(radThreePlayers);
		
		radFourPlayers = new JRadioButton("Four Players");
		radFourPlayers.setBounds(163, 110, 109, 23);
		contentPane.add(radFourPlayers);
		
		ButtonGroup playerSelectGroup = new ButtonGroup();
		playerSelectGroup.add(radOnePlayers);
		playerSelectGroup.add(radTwoPlayers);
		playerSelectGroup.add(radThreePlayers);
		playerSelectGroup.add(radFourPlayers);
		
		radOnePlayers.setSelected(true);
		
		JLabel labelDifficulty = new JLabel("Mode");
		labelDifficulty.setBounds(200, 154, 93, 14);
		contentPane.add(labelDifficulty);
		
		specialModeCheckBox = new JCheckBox("Special Mode");
		specialModeCheckBox.setBounds(160,170,109,23);
		contentPane.add(specialModeCheckBox);
		
		JButton buttonStart = new JButton("Start");
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//gets values of all selected radio buttons
				if (radOnePlayers.isSelected())
				{
					numPlayers = 1;
				}
				else if (radTwoPlayers.isSelected())
				{
					numPlayers = 2;
				}
				else if (radThreePlayers.isSelected())
				{
					numPlayers = 3;
				}
				else if (radFourPlayers.isSelected())
				{
					numPlayers = 4;
				}
				else
				{
					numPlayers = 1;
				}
				
				numAlivePlayers = numPlayers;
				
				//if the user hit the special mode check box
				if (specialModeCheckBox.isSelected())
				{
					specialmode = true;
				}
				else
				{
					specialmode = false;
				}
				
				GameMain game = new GameMain();
				setVisible(false);
				game.setVisible(true);
			}
		});
		buttonStart.setBounds(107, 204, 200, 50);
		contentPane.add(buttonStart);
	}
}

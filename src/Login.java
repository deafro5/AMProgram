

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener{
	//WINDOW DIMENSIONS DEFAULT IS WIDTH = 350 HEIGHT = WIDTH/3
	private final int WINDOW_WIDTH = 350,WINDOW_HEIGHT = WINDOW_WIDTH/3;
	
	//Initialize all components
	private JPanel inputPanel,grid;
	private JLabel nameLabel,passwordLabel;
	private JTextField nameBox;
	private JPasswordField passwordBox;
	private JButton loginButton, newUserButton;
	
	public Login(){
		//set Attributes for our window
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		setTitle("Login");
		setResizable(false);
		
		//create login button with Text
		loginButton = new JButton("Login");
		//create button if there is a new user.
		newUserButton = new JButton("New User");
		
		//Label our input boxes
		nameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		
		//create our input boxes
		nameBox = new JTextField(10);
		passwordBox = new JPasswordField(10);
		
		//create Panel to harness our grid
		inputPanel = new JPanel();
		
		//Create a 2x2 Grid
		grid = new JPanel();
		grid.setLayout(new GridLayout(2,2));
		
		//add components to grid
		grid.add(nameLabel);
		grid.add(nameBox);
		grid.add(passwordLabel);
		grid.add(passwordBox);
		
		//add grid to a separate component
		inputPanel.add(grid);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2,0,30));
		buttonPanel.add(loginButton);
		buttonPanel.add(newUserButton);
		
		//add everything to our main window
		getContentPane().add(inputPanel,BorderLayout.CENTER);
		getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		
		//add action listener to the button so it reacts to user click.
		loginButton.addActionListener(this);
		newUserButton.addActionListener(this);
		
		//reveal the window
		setVisible(true);
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Login"){
		System.out.println("Username: " + nameBox.getText());
		
		String temp = "";
		//stores our char[] password into a string
		for(int i = 0;i<passwordBox.getPassword().length;i++){
			temp = temp + passwordBox.getPassword()[i];
		}
		System.out.println("Password: " + temp);
		}else if(e.getActionCommand() == "New User"){
			System.out.println("New User Button Pressed");
		}
		
		//insert file reader hash check here for login
		
		//if expression validating hashes, if match, login.
		
	}
}

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener {
	// WINDOW DIMENSIONS DEFAULT IS WIDTH = 350 HEIGHT = WIDTH/3
	private final int WINDOW_WIDTH = 350, WINDOW_HEIGHT = 120;

	// Boolean that changes the MODE
	private boolean newUserMode = false;
	private boolean usernameExists = false;
	// Initialize all components
	private JPanel inputPanel, grid;
	private JLabel nameLabel, passwordLabel, passCheckLabel;
	private JTextField nameBox;
	private JPasswordField passwordBox, passCheckBox;
	private JButton loginButton, newUserButton;
	private FileReader loginReader;
	
	//get user's screen info
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public Login() {
		// set Attributes for our window
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		//Center the Program Window
		Dimension dim = new Dimension(toolkit.getScreenSize());
		setLocation((int)dim.getWidth()/2 - WINDOW_WIDTH/2,(int)dim.getHeight()/2 - WINDOW_HEIGHT/2);
		
		setTitle("Login");
		setResizable(false);

		// create login button with Text
		loginButton = new JButton("Login");
		// create button if there is a new user.
		newUserButton = new JButton("New User");

		// Label our input boxes
		nameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");

		// create our input boxes
		nameBox = new JTextField(10);
		passwordBox = new JPasswordField(10);

		passCheckLabel = new JLabel("Repeat Password: ");
		passCheckBox = new JPasswordField(10);

		passCheckLabel.setVisible(false);
		passCheckBox.setVisible(false);
		// create Panel to harness our grid
		inputPanel = new JPanel();

		// Create a 2x2 Grid
		grid = new JPanel();
		grid.setLayout(new GridLayout(3, 2));

		// add components to grid
		grid.add(nameLabel);
		grid.add(nameBox);
		grid.add(passwordLabel);
		grid.add(passwordBox);
		grid.add(passCheckLabel);
		grid.add(passCheckBox);

		// add grid to a separate component
		inputPanel.add(grid);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2, 0, 30));
		buttonPanel.add(loginButton);
		buttonPanel.add(newUserButton);

		// add everything to our main window
		getContentPane().add(inputPanel, BorderLayout.CENTER);
		getContentPane().add(BorderLayout.SOUTH, buttonPanel);

		// add action listener to the button so it reacts to user click.
		loginButton.addActionListener(this);
		newUserButton.addActionListener(this);

		loginReader = new FileReader();

		// reveal the window
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == loginButton.getActionCommand()) {
			String usernameEntered = nameBox.getText();
			String passwordEntered;
			String passwordRepeat;
			String usernameHashed;
			String passwordHashed;
			System.out.println("Username: " + usernameEntered);


			// UNNEEDED WHEN FULL PROGRAM EXISTS
			String temp = "";
			// stores our char[] password into a string
			for (int i = 0; i < passwordBox.getPassword().length; i++) {
				temp = temp + passwordBox.getPassword()[i];
			}

			passwordEntered = temp;
			System.out.println("Password: " + passwordEntered);

			// repeat Password box
			temp = "";
			// stores our char[] password into a string
			for (int i = 0; i < passwordBox.getPassword().length; i++) {
				temp = temp + passwordBox.getPassword()[i];
			}

			passwordRepeat = temp;
			System.out.println("PasswordRepeat: " + passwordRepeat);
			// UNNEEDED WHEN FULL PROGRAM EXISTS

			usernameHashed = Integer.toString(usernameEntered.hashCode());
			passwordHashed = Integer.toString(passwordEntered.hashCode());
			hashChecks(usernameHashed,passwordHashed);
			if (newUserMode) {
				if(usernameExists){
					LoginInfo info = new LoginInfo("Username already Exists in Database\n" + 
							"Please hit the New User Button and try logging in with your password");
				} else if(passwordEntered.hashCode() == passwordRepeat.hashCode()) {
						System.out.println("Your Password Matches");
						// output New user Created Window
						LoginInfo info = new LoginInfo("New User being created, Welcome " + usernameEntered + "!");
						// add new user to Login file Database
						try {
							loginReader.writeNewUser(usernameHashed,passwordHashed);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
						usernameHashed = Integer.toString(usernameEntered.hashCode());
						passwordHashed = Integer.toString(passwordEntered.hashCode());

						System.out.println(usernameHashed + ", " + passwordHashed);
						// Open Student window
						Student myStudent = new Student();
						myStudent.setHashName(usernameHashed);
						try {
							myStudent.loadStudentClasses();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						this.setVisible(false);
				}else{
					System.out.println("Your Passwords Do Not Match");
					LoginInfo info = new LoginInfo("Your passwords do not match");
				}
			}else{
				usernameHashed = Integer.toString(usernameEntered.hashCode());
				passwordHashed = Integer.toString(passwordEntered.hashCode());

				System.out.println(usernameHashed + ", " + passwordHashed);
				// The method below is called to check to see if the username
				// and password hashes match any in the database
				// hashChecks(userNameHashed, passwordHashed);

				
				if (hashChecks(usernameHashed, passwordHashed)) {
					Student newStudent = null;

					newStudent = new Student();
					newStudent.setHashName(usernameHashed);
					try {
						newStudent.loadStudentClasses();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				
				setVisible(false);

				}else{
					LoginInfo info = new LoginInfo("Username or Password is incorrect");
				}
			}

		} else if (e.getActionCommand() == newUserButton.getActionCommand()) {
			System.out.println("New User Button Pressed");
			if (newUserMode) {
				passCheckLabel.setVisible(false);
				passCheckBox.setVisible(false);
				
				loginButton.setText("Login");
				newUserButton.setText("New User");
				newUserMode = false;
			} else {
				LoginInfo info = new LoginInfo("Please enter the following: \n" +
						"1. A valid username \n" + "2. A Valid Password \n" + "3. Repeat your password");
				passCheckLabel.setVisible(true);
				passCheckBox.setVisible(true);
				
				loginButton.setText("Create");
				newUserButton.setText("Return");
				
				newUserMode = true;
			}

		}

		// insert file reader hash check here for login

		// if expression validating hashes, if match, login.

	}

	/*
	 * This method takes the hashed username and password as parameters. It then
	 * calls the FileReader's fetchLoginInfo method which will return a 2D array
	 * with all hashed-usernames and passwords. It will compare the hashes from
	 * the login with the database to try and find a match.
	 */
	public boolean hashChecks(String someHashName, String someHashPass) {
		// Need to set this to null and surround it with try catch block so it
		// doesn't get an error.
		String[][] loginInfoToCompare = null;
		try {
			loginInfoToCompare = loginReader.fetchLoginInfo(); // Fetch the 2D
																// array
																// database
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int loginFileSize = loginReader.fetchLoginInfoSize();// Fetch the size
																// of the
																// database
		boolean userFound = false;

		// Each 'i' represents a new student. Their user-hash is stored in the
		// first element of 'j' and the password in the second element.
		for (int i = 0; i < loginFileSize; i++) {
			int j = 0;

			// Checks to see if this is the right user
			// Need to use the compareTo method which returns -1 if false
			if (loginInfoToCompare[i][j].compareTo(someHashName) == 0) {
				userFound = true; // User exists in the database so change
									// boolean
				System.out.println("Username Exists. Checking password...");
				usernameExists = true;
				// If correct user, compare the passwords (the second element of
				// 'j': [j+1] )
				// Need to use the compareTo method which returns -1 if false
				if (loginInfoToCompare[i][j + 1].compareTo(someHashPass) == 0) {
					System.out.println("Passwords matched");// Found a match
					return true; // Return true because everything is a match
				} else {
					System.out.println("Passwords not a match");// Not a match
					return false; // Return false because it was not a match and
									// they need to re-enter password
				}
			}
			// If the user-password combo was not matched, 'i' will increment to
			// check the next user in the database.
		}

		// Checks to see if the user was found (i.e. the user is in the
		// database)
		if (userFound == false) {
			System.out.println("User not in the database");
			usernameExists = false;
			return false;
		}

		return true; // temporary until all cases are handled
	}
}

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class Student extends JFrame implements ActionListener {
	private final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 300;

	private JButton addButton,removeButton,saveButton,exitButton,compareButton;
	private JPanel selectPanel,functionPanel,classButtons;
	private JComboBox subjectBox,numberBox;
	private JList studentClasses;
	private DefaultListModel<String> transcript;
	private FileReader studentWriter;
	private String hashedUser;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public Student(){
		//Window Attributes
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		setTitle("Academic Major Selection");
		
		//Center the Program Window
		Dimension dim = new Dimension(toolkit.getScreenSize());
		setLocation((int)dim.getWidth()/2 - WINDOW_WIDTH/2,(int)dim.getHeight()/2 - WINDOW_HEIGHT/2);

		//do not allow resizing of window
		setResizable(false);

		//create arrays for classes
		//input file reader here
		String[] subjectArray = {"CSC","MTH","ENG","THE","PHY"};
		String[] numberArray = {"100","200","300","400","500"};

		//create boxes for dropdown
		subjectBox = new JComboBox(subjectArray);
		numberBox = new JComboBox(numberArray);

		//creates a panel for our dropdown panels
		selectPanel = new JPanel();
		selectPanel.setLayout(new GridLayout(1,2,25,0));
		selectPanel.add(subjectBox);
		selectPanel.add(numberBox);

		//places dropdowns into another panel for formatting
		JPanel dropdownBoxes = new JPanel();

		//add dropdown boxes to select panel
		dropdownBoxes.add(selectPanel,BorderLayout.CENTER);

		//create add and remove function buttons
		addButton = new JButton("Add Class ===>");
		removeButton = new JButton("<=== Remove Class");
		compareButton = new JButton("Run Comparison");

		//place add and remove buttons into 2 panels for formatting
		JPanel classButtons = new JPanel();
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2,1,0,25));
		buttons.add(addButton);
		buttons.add(removeButton);

		//add our buttons to main panel
		classButtons.add(buttons,BorderLayout.CENTER);

		//demo list for our list box
		transcript = new DefaultListModel<String>();
		//transcript.addElement("CSC305");
		//transcript.addElement("MTH212");
		
		//Create panels for our output list
		//output list attributes
		studentClasses = new JList(transcript);
		studentClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentClasses.setLayoutOrientation(JList.VERTICAL);
		studentClasses.setVisibleRowCount(-1);

		//places main functions save and exit into a panel
		functionPanel = new JPanel();
		JPanel saveExitButtons = new JPanel();
		saveButton = new JButton("SAVE");
		exitButton = new JButton("EXIT");

		//place in another panel for formatting
		functionPanel.setLayout(new GridLayout(2,1,0,25));
		functionPanel.add(saveButton);
		functionPanel.add(exitButton);
		functionPanel.add(compareButton);
		saveExitButtons.add(functionPanel,BorderLayout.CENTER);

		//Grid for the entire Main Window. "" represents blank grid spaces.
		// grid adds left to right, top to bottom in a 3x3 grid. 1 2 3
		//                                                       4 5 6
		//                                                       7 8 9
		getContentPane().setLayout(new GridLayout(3,3));
		getContentPane().add(new JLabel(""));
		getContentPane().add(new JLabel(""));
		getContentPane().add(new JLabel(""));
		getContentPane().add(dropdownBoxes);
		getContentPane().add(classButtons);
		getContentPane().add(studentClasses);
		getContentPane().add(new JLabel(""));
		getContentPane().add(new JLabel(""));
		getContentPane().add(saveExitButtons);

		//add the Action Listener to all Buttons and list
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		saveButton.addActionListener(this);
		exitButton.addActionListener(this);
		compareButton.addActionListener(this);

		studentWriter = new FileReader();

		//show the window
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//What button is pressed?
		if(e.getActionCommand() == addButton.getText()){
			System.out.println("Add button Pressed");
			if(classAlreadyThereCheck()){
				System.out.println("You've already added that class.");
			}
			else{
				transcript.addElement(new StringBuilder("").append(subjectBox.getSelectedItem()).append(numberBox.getSelectedItem()).toString());
			}
		}else if(e.getActionCommand() == removeButton.getText()){
			System.out.println("Remove button Pressed");
			int s = studentClasses.getSelectedIndex();
			if(s!=-1){
				transcript.remove(s);	
			}
		}else if(e.getActionCommand() == saveButton.getText()){
			System.out.println("Save button Pressed");
			Object[] takenClassArray = new Object[transcript.getSize()]; //Makes an array of objects
			System.out.println("Taken classes: " + takenClassArray.toString());
			takenClassArray = transcript.toArray();//Copies classes into array
			
			try {
				//Calls FileWriter to save the array of classes to a file of the user's hashedName
				studentWriter.writeUserSave(hashedUser, takenClassArray, transcript.getSize());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getActionCommand() == exitButton.getText()){
			System.out.println("Exit button Pressed");
			System.exit(0);
		}else if(e.getActionCommand() == compareButton.getText()){
			System.out.println("Comparison button Pressed");
			CompareEngine compare = new CompareEngine();
			try {
				DefaultListModel<String> copytranscript = new DefaultListModel<String>();
				for(int i=0; i<transcript.getSize();i++){
					copytranscript.addElement(transcript.get(i));
				}
				compare.compare(copytranscript);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
	
	/**
	 *This method checks to see if the class trying to be added is already in the list of taken classes.
	 *If so, returns true. If not, returns false.
	 */
	boolean classAlreadyThereCheck(){
		Object[] classesTakenObj = new Object[transcript.getSize()];
		String[] classesTakenStr = new String[transcript.getSize()]; 
		classesTakenObj = transcript.toArray();//Puts elements of transcript into array of objects
		
		//For loop below copies the object array into the String array as strings
		for(int j=0; j<transcript.getSize(); j++){
			classesTakenStr[j] = classesTakenObj[j].toString();
		}
		
		//someClass is a String containing the current class trying to be added
		String someClass = new StringBuilder("").append(subjectBox.getSelectedItem()).append(numberBox.getSelectedItem()).toString();
		for(int i=0; i<transcript.getSize(); i++){
			if(classesTakenStr[i].compareTo(someClass) == 0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets variable equal to the current user's hashed name.
	 * Used to send to the filewriter so it knows where to save a student's classes
	 * @param someHash (the user name hashed)
	 */
	 public void setHashName(String someHash){
		hashedUser = someHash;
	}
	 
	public void loadStudentClasses() throws FileNotFoundException{
		 String[] tempLoadClass = null;
		 tempLoadClass = studentWriter.readLoadClasses(hashedUser);
		 
		 for(int i=0; i<tempLoadClass.length; i++){
			 transcript.addElement(tempLoadClass[i]);
		 }
	}
		
}

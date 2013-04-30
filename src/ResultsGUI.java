import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;



public class ResultsGUI extends JFrame implements ActionListener{
  private int WIDTH = 600,HEIGHT = 300;
	private JTextArea label;
	private JButton exit;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private DefaultListModel<String> missingOutput;
	private JList studentClasses2;

	public ResultsGUI(ArrayList<String> missingClasses){

		missingOutput = new DefaultListModel<String>();
		for(int j=0; j<missingClasses.size(); j++){
			missingOutput.addElement(missingClasses.get(j));
		}

		studentClasses2 = new JList(missingOutput);
		studentClasses2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentClasses2.setLayoutOrientation(JList.VERTICAL);
		studentClasses2.setVisibleRowCount(-1);

		setSize(WIDTH,HEIGHT);
		setTitle("Classes Left To Complete");

		//Center the Program Window
		Dimension dim = new Dimension(toolkit.getScreenSize());
		setLocation((int)dim.getWidth()/2 - WIDTH/2,(int)dim.getHeight()/2 - HEIGHT/2);
		exit = new JButton("Okay");
		setResizable(false);
		exit.addActionListener(this);
		JPanel temp = new JPanel();
		JScrollPane scrollableList = new JScrollPane(studentClasses2);
		getContentPane().add(scrollableList,BorderLayout.CENTER);
		temp.add(exit);
		getContentPane().add(temp,BorderLayout.SOUTH);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == exit.getActionCommand()){
			setVisible(false);
		}

	}
}

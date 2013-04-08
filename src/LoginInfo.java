import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class LoginInfo extends JFrame implements ActionListener{
	private int WIDTH = 400,HEIGHT = 200;
	private JTextArea label;
	private JButton exit;
	
	public LoginInfo(String message){
		setSize(WIDTH,HEIGHT);
		label = new JTextArea(message);
		exit = new JButton("Okay");
		label.setEditable(false);
		setResizable(false);
		exit.addActionListener(this);
		JPanel temp = new JPanel();
		temp.add(label,BorderLayout.CENTER);
		temp.add(exit,BorderLayout.SOUTH);
		getContentPane().add(temp,BorderLayout.CENTER);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == exit.getActionCommand()){
			setVisible(false);
		}
		
	}
}

package VirtualClass;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import PortChannel.*;

public class TeacherLogin {

	private JFrame frmTeacherLogin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TeacherLogin window = new TeacherLogin();
					window.frmTeacherLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TeacherLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTeacherLogin = new JFrame();
		frmTeacherLogin.setTitle("Teacher Login");
		frmTeacherLogin.setBounds(100, 100, 450, 300);
		frmTeacherLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTeacherLogin.getContentPane().setLayout(null);
		
		Label label = new Label("Enter Port Number ");
		label.setBounds(81, 98, 126, 24);
		frmTeacherLogin.getContentPane().add(label);
		
		TextField getPortNum = new TextField();
		getPortNum.setBounds(244, 98, 100, 24);
		frmTeacherLogin.getContentPane().add(getPortNum);
		
		Button button = new Button("Login");
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int port = Integer.parseInt(getPortNum.getText());
				Teacher t = new Teacher(port);
				frmTeacherLogin.dispose();
				t.setVisible(true);
			}
		});
		button.setBounds(164, 167, 79, 24);
		frmTeacherLogin.getContentPane().add(button);
	}
}

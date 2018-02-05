package VirtualClass;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Label;
import java.awt.TextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StudentLogin {

	private JFrame frmStudentlogin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentLogin window = new StudentLogin();
					window.frmStudentlogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StudentLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStudentlogin = new JFrame();
		frmStudentlogin.setTitle("StudentLogin");
		frmStudentlogin.setBounds(100, 100, 450, 300);
		frmStudentlogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStudentlogin.getContentPane().setLayout(null);
		
		Label label = new Label("Enter Your ID");
		label.setAlignment(Label.RIGHT);
		label.setBounds(27, 78, 122, 24);
		frmStudentlogin.getContentPane().add(label);
		
		TextField studentField = new TextField();
		studentField.setBounds(183, 78, 155, 24);
		frmStudentlogin.getContentPane().add(studentField);
		
		TextField ipField = new TextField();
		ipField.setBounds(183, 122, 155, 24);
		frmStudentlogin.getContentPane().add(ipField);
		
		Label label_1 = new Label("Enter IP Address");
		label_1.setAlignment(Label.RIGHT);
		label_1.setBounds(27, 122, 122, 24);
		frmStudentlogin.getContentPane().add(label_1);
		
		TextField portField = new TextField();
		portField.setBounds(183, 164, 155, 24);
		frmStudentlogin.getContentPane().add(portField);
		
		Label label_2 = new Label("Enter Section Address");
		label_2.setAlignment(Label.RIGHT);
		label_2.setBounds(10, 164, 139, 24);
		frmStudentlogin.getContentPane().add(label_2);
		
		JLabel lblWelcomeToVirtual = new JLabel("Welcome to Virtual Classroom");
		lblWelcomeToVirtual.setFont(new Font("Sitka Subheading", Font.BOLD | Font.ITALIC, 13));
		lblWelcomeToVirtual.setBounds(122, 24, 189, 16);
		frmStudentlogin.getContentPane().add(lblWelcomeToVirtual);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String studentId = studentField.getText();
				String ipAddress = ipField.getText();
				int portAddress = Integer.parseInt(portField.getText());
				Student s = new Student(studentId, ipAddress, portAddress);
				/*s.sendDetails();*/
				frmStudentlogin.dispose();
				s.setVisible(true);
			}
		});
		btnLogin.setBounds(149, 215, 97, 25);
		frmStudentlogin.getContentPane().add(btnLogin);
	}
}

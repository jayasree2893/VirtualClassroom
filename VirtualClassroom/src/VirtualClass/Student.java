package VirtualClass;

import java.awt.EventQueue;

import javax.swing.JFrame;

import PortChannel.BoardMessage;
import PortChannel.ChannelEndPoint;

import java.awt.TextArea;
import java.awt.TextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Student {

	private JFrame studentFrame;
	String studentId;
	String ipAddress;
	int port;
	ChannelEndPoint cEPoint;
	TextArea notesArea = new TextArea();
	TextField questionInput = new TextField();
	JButton quesButton = new JButton("Question");
	boolean connected = false;
	

	public void setVisible(boolean b){
		if(b=true)
		{
			studentFrame.setVisible(true);
		}
		else{
			studentFrame.setVisible(false);
		}
	}
	/**
	 * Launch the application.
	 */
	public void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Student window = new Student(studentId, ipAddress, port);
					window.studentFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Student(String stuID, String ipAddress, int port) {
		this.studentId = stuID;
		this.ipAddress = ipAddress;
		this.port = port;
		initialize();
		notesArea.setEditable(false);
		questionInput.setEnabled(false);
		quesButton.setEnabled(true);
		cEPoint = new ChannelEndPoint(studentId,ipAddress,port);
		Thread client = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				cEPoint.initialize();
				connected = true;
				
				BoardMessage msg = new BoardMessage(studentId, "Login", "OK");
				cEPoint.send(msg);
				while(true){
					BoardMessage returnmsg = (BoardMessage)cEPoint.receive();
					addNotes(returnmsg);
					filter(returnmsg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		client.start();
	}

	public synchronized void addNotes(BoardMessage msg){
		notesArea.append(msg.toString());
	}
	
	public synchronized void filter(BoardMessage msg){
		if (msg.getMessageType().equals("Approved")){
			questionInput.setEnabled(true);
			quesButton.setEnabled(false);
		}
	}
	
	public void send(BoardMessage msg){
		cEPoint.send(msg);
	}
	public void sendText(){
		String text = questionInput.getText();
		if(!text.isEmpty()){
			BoardMessage msg = new BoardMessage(studentId, "Speak", text);
			send(msg);
		}
		questionInput.setEnabled(false);
		quesButton.setEnabled(true);
		questionInput.setText("");
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		studentFrame = new JFrame();
		studentFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				cEPoint.close();
			}
		});
		studentFrame.setTitle(studentId);
		studentFrame.setBounds(100, 100, 467, 314);
		studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		studentFrame.getContentPane().setLayout(null);
		
		
		notesArea.setBounds(0, 0, 433, 209);
		studentFrame.getContentPane().add(notesArea);
		
		
		questionInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keycode = e.getKeyCode();
				if (keycode == 10){
					sendText();
				}
			}
		});
		questionInput.setBounds(108, 224, 325, 24);
		studentFrame.getContentPane().add(questionInput);
		
		
		quesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (connected == false)
					return;
				BoardMessage msg = new BoardMessage(studentId, "Question","Raised");
				send(msg);
			}
		});
		quesButton.setBounds(6, 223, 96, 31);
		studentFrame.getContentPane().add(quesButton);
	}

}

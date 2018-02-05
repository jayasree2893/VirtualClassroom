package VirtualClass;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import PortChannel.BoardMessage;
import PortChannel.ChannelPort;

import java.awt.TextArea;
import java.awt.List;
import java.awt.Button;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JList;

public class Teacher {
	ChannelPort cPort;
	
	private JFrame frmTeacher;
	TextArea messageArea = new TextArea();
	Button quesButton = new Button("Approve Question");
	TextField boardInput = new TextField();
	private final Button sendButton = new Button("SEND");
	private final JList quesList = new JList();
	
	private int portAddress;
	DefaultListModel<String> questionModel = new DefaultListModel<String>();
	DefaultListModel<String> StudentModel = new DefaultListModel<String>();
	
	public int getPort(){
		return portAddress;
	}
	
	public void setVisible(boolean b){
		if(b=true)
		{
			frmTeacher.setVisible(true);
		}
		else{
			frmTeacher.setVisible(false);
		}
		
	}
	/**
	 * Launch the application.
	 */
	public void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Teacher window = new Teacher(portAddress);
					window.frmTeacher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Teacher( int port) {
		this.portAddress = port;
		initialize();
		try {
			InetAddress ip = InetAddress.getLocalHost();
			messageArea.append("Class started at Address : ");
			messageArea.append(ip.toString() + "\n");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		quesButton.setEnabled(false);
		
		portAddress = getPort();
		
		cPort = new ChannelPort(portAddress);
		new Thread(cPort).start();
		
		quesList.setModel(questionModel);
		
		Thread messagecheck = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					BoardMessage receivedmsg = cPort.receive();
					String messageType = receivedmsg.getMessageType();
					
					if (messageType.equals("Login")){
						addStudent(receivedmsg);
					}
					
					else if (messageType.equals("Logout")){
						removeStudent(receivedmsg);}
					
					else {
						if (messageType.equals("Question")){
							addQuestion(receivedmsg);
						}
						cPort.broadcast(receivedmsg);
					}
					
					addNotesToMessageArea(receivedmsg);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		messagecheck.start();
		System.out.println("All Fine " + getPort());
		
	}
	
	public synchronized void addNotesToMessageArea(BoardMessage msg){
		messageArea.append(msg.toString());
		boolean empty = questionModel.isEmpty();
		quesButton.setEnabled(!empty);
	}
	
	public synchronized void addQuestion(BoardMessage msg){
		String studentId = msg.getPersonId();
		if (questionModel.contains(studentId)){
			return;
		}
		else{
			questionModel.addElement(studentId);
		}
	}
	
	public synchronized void addStudent(BoardMessage msg){
		StudentModel.addElement(msg.getPersonId());
	}
	
	public synchronized void removeStudent(BoardMessage msg){
		StudentModel.removeElement(msg.getPersonId());
	}
	
	public void sendMessage(){
		String message = boardInput.getText();
		if (!message.isEmpty()){
			BoardMessage msg = new BoardMessage("Teacher", "Notes", message);
			addNotesToMessageArea(msg);
			cPort.broadcast(msg);
		}
		boardInput.setText("");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		portAddress = getPort();
		frmTeacher = new JFrame();
		frmTeacher.setTitle("Teacher");
		frmTeacher.setBounds(100, 100, 671, 542);
		frmTeacher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTeacher.getContentPane().setLayout(null);
		
		
		messageArea.setBounds(10, 138, 633, 347);
		frmTeacher.getContentPane().add(messageArea);
		
		quesList.setBounds(484, 10, 157, 88);
		
		frmTeacher.getContentPane().add(quesList);
		
		quesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = quesList.getSelectedIndex();
				if (index == -1)
					return;
				
				String st = questionModel.getElementAt(index);
				
				int stuindex = StudentModel.indexOf(st);
				
				BoardMessage msg = new BoardMessage("Teacher","Approved","OK");
				addNotesToMessageArea(msg);
				cPort.send(stuindex, msg);
				questionModel.clear();
			}
		});
		quesButton.setBounds(496, 108, 125, 24);
		frmTeacher.getContentPane().add(quesButton);
		boardInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				int keycode = arg0.getKeyCode();
				if (keycode == 10){
					sendMessage();
				}
			}
		});
		
		
		boardInput.setBounds(10, 10, 455, 54);
		frmTeacher.getContentPane().add(boardInput);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		sendButton.setBounds(20, 89, 445, 24);
		
		frmTeacher.getContentPane().add(sendButton);
	}
}

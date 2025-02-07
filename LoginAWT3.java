package net;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

class LoginAWT3 extends JFrame implements ActionListener {

	TextField idTf, pwTf;
	Label logo, idl, pwl, msgl;
	Button logBtn;
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	String id;
	String host = "127.0.0.1";
	int port = 8003;
	String title = "MyChat3.0";
	String label[] = { "ID와 PWD를 입력하세요.", 
			                       "ID와 PWD를 확인하세요.", 
			                       "이중 접속입니다." };

	public LoginAWT3() {
		setSize(450, 400);
		getContentPane().setBackground(new Color(100, 200, 100));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setTitle(title);
		logo = new Label(title);
		logo.setFont(new Font("Dialog", Font.BOLD, 50));

		idl = new Label("ID");
		pwl = new Label("PWD");
		idTf = new TextField("aaa");
		pwTf = new TextField("1234");
		logBtn = new Button("로그인");
		msgl = new Label(label[0]);
		logo.setBounds(100, 50, 250, 100);
		idl.setBounds(150, 200, 50, 20);
		idTf.setBounds(200, 200, 100, 20);
		pwl.setBounds(150, 230, 50, 20);
		pwTf.setBounds(200, 230, 100, 20);
		logBtn.setBounds(150, 260, 150, 40);
		msgl.setBounds(150, 320, 150, 40);
		logBtn.addActionListener(this);
		add(logo);
		add(idl);
		add(idTf);
		add(pwl);
		add(pwTf);
		add(logBtn);
		add(msgl);
		setVisible(true);
		validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==logBtn) {
			if(sock==null) {
				//처음시도
				connect();
			}
			id = idTf.getText().trim();
			out.println(ChatProtocol3.ID+ChatProtocol3.MODE1+
					id+";"+pwTf.getText().trim());
			try {
				String line = in.readLine();//ID:T,F,C
				int idx = line.indexOf(ChatProtocol3.MODE1);
				String cmd = line.substring(0, idx); 
				String data = line.substring(idx + 1); 
				if(cmd.equals(ChatProtocol3.ID)) {
					if(data.equals("F")) {
						msgl.setForeground(Color.RED);
						msgl.setText(label[1]);
					}else if(data.equals("C")) {
						msgl.setForeground(Color.BLUE);
						msgl.setText(label[2]);
					}else if(data.equals("T")) {
						dispose();//LoginAWT3 사라짐.
						new ChatClient3(in, out, id);
						//msgl.setForeground(Color.ORANGE);
						//msgl.setText("로그인 성공");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void connect() {
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true/* auto flush */);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// --connect

	public static void main(String[] args) {
		new LoginAWT3();
	}
}

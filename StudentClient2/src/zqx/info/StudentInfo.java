package zqx.info;

import javax.swing.*;

import zqx.bchandler.BCHandler;
import zqx.filesend.FileSendHandler;
import zqx.fileupload.FileUpload;

import java.awt.*;  
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class StudentInfo extends JFrame implements ActionListener{
	
	JButton jb1, jb2, jb3, jb4=null;
	JLabel jlb1,jlb2,jlb3=null;
	JPanel jp1, jp2, jp3, jp4, jp5=null;
	JTextArea jta1 = null;
	private String id, username, ip;
	Socket socket = null;
	Socket socket2 = null;
    Socket socket3 = null;
    Socket socket4 = null;
      
	
	public static void main(String[] args) {  
        
    }  
	
	public StudentInfo(String id, String username, String ip){
		jb2 = new JButton("文件上传");
		jb4 = new JButton("退出系统");
		jta1 = new JTextArea(40,40);
		
		//设置监听   
        jb2.addActionListener(this);
	    jb4.addActionListener(this);
        
		this.id = id;
		this.username = username;
		this.ip = ip;
		
		jp1=new JPanel();  
        jp2=new JPanel();  
        jp3=new JPanel();  
        jp4=new JPanel();
        jp5=new JPanel();
		
		jlb1 = new JLabel("学号："+id);
		jlb2 = new JLabel("姓名："+username);
		jlb3 = new JLabel("当前ip："+ip);

		jp1.add(jlb1);
		jp1.add(jlb2);
		jp1.add(jlb3);
		jp1.setLayout(new GridLayout(1,3));
		
		jp2.add(jb2);
		jp2.add(jb4);
		jp2.setLayout(new FlowLayout());
		
		//jp3.add(jb2);
		jp3.add(jta1);
		
		//jp4.add(jb3);
		
		//jp5.add(jb4);
		
		//加入JFrame中  
        this.add(jp1);  
        this.add(jp2);  
        this.add(jp3);  
        //this.add(jp4);
        //this.add(jp5);
        //设置布局管理器  
        this.setLayout(new GridLayout(3,1));  
        //给窗口设置标题  
        this.setTitle("远程桌面监控系统");  
        //设置窗体大小  
        this.setSize(462, 500);  
        //设置窗体初始位置  
        this.setLocation(200, 150);
        //设置当关闭窗口时，保证JVM也退出  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        //显示窗体  
        this.setVisible(true);  
        this.setResizable(true);
	}
	
	public void initialize(String ip, int port ){

        Robot robot = null; //Used to capture the screen
        Rectangle rectangle = null; //Used to represent screen dimensions

        try {
            System.out.println("Connecting to server ..........");
            socket = new Socket(ip, port);
            socket2 = new Socket(ip, 6666);
            socket3 = new Socket(ip, 7777);
            socket4 = new Socket(ip, 10000);
            jta1.append("----------------------------------------------------公告----------------------------------------------------\n");
            new BCHandler(socket4, jta1);
            new FileSendHandler(socket3);
            System.out.println("Connection Established.");

            //Get default screen device
            GraphicsEnvironment gEnv=GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev=gEnv.getDefaultScreenDevice();

            //Get screen dimensions
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            rectangle = new Rectangle(dim);

            //Prepare Robot object
            robot = new Robot(gDev);

            //ScreenSpyer sends screenshots of the client screen
            new ScreenSpyer(socket,robot,rectangle);
            //ServerDelegate recieves server commands and execute them
            //new ServerDelegate(socket,robot);
            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (AWTException ex) {
                ex.printStackTrace();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="退出系统"){
			System.exit(0);
        }else if(e.getActionCommand()=="文件上传"){
        	new FileUpload(socket2);
        }
	}
}

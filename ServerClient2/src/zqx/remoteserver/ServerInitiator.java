package zqx.remoteserver;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;

import zqx.fileupload.FileUploadHandler;

/**
 * This is the entry class of the server
 */
public class ServerInitiator extends JFrame implements ActionListener{
    //Main server frame
    //private JFrame frame = new JFrame();
    //JDesktopPane represents the main container that will contain all
    //connected clients' screens
    private JDesktopPane desktop = null;
    private Container contentPane = null;
    private JButton jb1 = null;
    private JButton jb2 = null;
    private JTextArea jta1 = null;
    private JPanel jp = null;
    private File file;
    private FileInputStream fis;
    
    private static List<Socket> fileclients = new ArrayList<Socket>();//�ļ�����--�ͻ��˼���
    private static List<Socket> sos=new ArrayList<Socket>();//����㲥--�ͻ��˼���
    
    public static void main(String args[]){
    	String port = "5000";				//ͼ�񴫲�
    	String port2 = "6666";				//�ļ��ϴ�
        String port3 = "7777";				//�ļ�����
        String port4 = "10000";				//����㲥
        new ServerInitiator().initialize(Integer.parseInt(port), Integer.parseInt(port2), Integer.parseInt(port3),Integer.parseInt(port4));
    }
    
    public ServerInitiator(){
    	desktop = new JDesktopPane();
        contentPane = this.getContentPane();
        jb1 = new JButton("����㲥");
        jb2 = new JButton("�ļ�����");
        jta1 = new JTextArea(2, 60);
        
        jp = new JPanel();
        
        jb2.addActionListener(this);
        jb1.addActionListener(this);
        
        jp.add(jb1);
        jp.add(jta1);
        jp.add(jb2);
        
        jp.setLayout(new FlowLayout());
             
        contentPane.add(desktop);
        contentPane.add(jp,BorderLayout.SOUTH);

        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Show the frame in a maximized state
        this.setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        
    }

    public void initialize(int port,int port2, int port3, int port4){

        try {
            ServerSocket sc = new ServerSocket(port);			//ͼ�񴫲�
            ServerSocket sc2 = new ServerSocket(port2);		//�ļ��ϴ�
            ServerSocket sc3 = new ServerSocket(port3);			//�ļ�����
            ServerSocket sc4 = new ServerSocket(port4);			//����㲥
            
            //DataInputStream dis;
            //FileOutputStream fos;
            //Listen to server port and accept clients connections
            while(true){
                Socket client = sc.accept();			//ͼ�񴫲�
                Socket client2 = sc2.accept();			//�ļ��ϴ�
                Socket client3 = sc3.accept();			//�ļ�����
                Socket client4 = sc4.accept();			//����㲥
                sos.add(client4);
                fileclients.add(client3);

                System.out.println("New client Connected to the server");
                                
                //Per each client create a ClientHandler
                new ClientHandler(client,desktop);
                new FileUploadHandler(client2);
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="�ļ�����"){
			//��ʼ���ļ�ѡ��
    		JFileChooser fDialog = new JFileChooser();
    		//�����ļ�ѡ���ı��� 
    		fDialog.setDialogTitle("��ѡ���ϴ����ļ�");
    		//����ѡ���
    		int returnVal = fDialog.showOpenDialog(null);
    		// �����ѡ�����ļ�
    		if(JFileChooser.APPROVE_OPTION == returnVal){
    		    
    			//��ӡ���ļ���·����������޸�λ ��·��ֵ д�� textField ��
    			//System.out.println(fDialog.getSelectedFile());
    			//String filename = String(fDialog.getSelectedFile());
    			//�����˴����ļ�
                //File file =new File("c:/test.doc");
                
    			//�ļ�·��ת��
    			String filename = fDialog.getSelectedFile().getAbsolutePath().replace('\\', '/');
    			//String filename = "D:/��dalao��ͷ.txt";	ʾ��	
               
    			System.out.println(filename);
                file =new File(filename);

                /**
                 * �ļ�����
                 */
                Iterator<Socket> i=fileclients.iterator();
                Socket temps = null;
                FileInputStream fis = null;
       		 	DataOutputStream dos = null;
                
       		 	while(i.hasNext()){
	       		 	temps=(Socket)i.next();
	       		 	try{
	       		 		dos =new DataOutputStream(temps.getOutputStream());
	       		 		//�ļ����ͳ���
	                    dos.writeUTF(file.getName());
	                    dos.flush();
	                    dos.writeLong(file.length());
	                    dos.flush();
	                    fis =new FileInputStream(file);
	                    //�����ļ�
	                    byte[] sendBytes =new byte[1024];
	                    int length =0;
	                    while((length = fis.read(sendBytes,0, sendBytes.length)) >0){
	                        dos.write(sendBytes,0, length);
	                        dos.flush();
	                    }
       		 		}catch(IOException e1){
       		 			fileclients.remove(temps);
       		 			e1.printStackTrace();
       		 		}
       		 	}
               
    		}
		}else if(e.getActionCommand()=="����㲥"){
			
			/**
			 * ����㲥��Ϣ����
			 */
			Iterator<Socket> i=sos.iterator();
			Socket temps=null;
			PrintWriter os = null;
			String[] lineString = jta1.getText().split("\n");
			while(i.hasNext()){
			        temps=(Socket)i.next();
			        try {
						os=new PrintWriter(temps.getOutputStream());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						sos.remove(temps);
						e1.printStackTrace();
					}
			        for(String s : lineString){
						//message_list.add(s);
			        	os.println(s+"\n");
				        os.flush();
					}
			}
			jta1.setText("");
		
		}
	}
}

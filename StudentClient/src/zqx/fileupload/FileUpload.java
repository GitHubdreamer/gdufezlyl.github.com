package zqx.fileupload;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import javax.swing.JFileChooser;

public class FileUpload extends Thread{
	 private Socket client;
	 private FileInputStream fis;
	 private DataOutputStream dos;
	 
	 public FileUpload(Socket socket){
		 this.client = socket;
		 start();
	 }
	 
	 public void run(){
		 try {
	            try {        
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
	                    String filename = fDialog.getSelectedFile().getAbsolutePath().replace('\\', '/');
	        			//String filename = "D:/��dalao��ͷ.txt";
	                    System.out.println(filename);
	                    File file =new File(filename);
	                    //System.out.println(filename);
	                    fis =new FileInputStream(file);
	                    dos =new DataOutputStream(client.getOutputStream());
	                     
	                    //�ļ����ͳ���
	                    dos.writeUTF(file.getName());
	                    dos.flush();
	                    dos.writeLong(file.length());
	                    dos.flush();
	                     
	                    //�����ļ�
	                    byte[] sendBytes =new byte[1024];
	                    int length =0;
	                    while((length = fis.read(sendBytes,0, sendBytes.length)) >0){
	                        dos.write(sendBytes,0, length);
	                        dos.flush();
	                    }
	        		}
	        		               
	            }catch (Exception e) {
	                e.printStackTrace();
	            }finally{
	                if(fis !=null)
	                    fis.close();
	                if(dos !=null)
	                    dos.close();
	                client.close();
	            }
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	 }
}

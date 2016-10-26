package zqx.filesend;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class FileSendHandler extends Thread{
	private Socket client = null;
	private DataInputStream dis;
    private FileOutputStream fos;
    
	public FileSendHandler(Socket socket){
		this.client = socket;
		start();
	}
	
	public void run(){
		try {
			dis =new DataInputStream(client.getInputStream());
			while(true){
				//�ļ����ͳ���
		        String fileName = dis.readUTF();
		        long fileLength = dis.readLong();
		        fos =new FileOutputStream(new File("F:/" + fileName));
		         
		        byte[] sendBytes =new byte[1024];
		        int transLen =0;
		        System.out.println("----��ʼ�����ļ�<" + fileName +">,�ļ���СΪ<" + fileLength +">----");
		        while(true){
		            int read =0;
		            read = dis.read(sendBytes);
		            if(read == -1)
		                break;
		            transLen += read;
		            System.out.println("�����ļ�����" +100 * transLen/fileLength +"%...");
		            fos.write(sendBytes,0, read);
		            fos.flush();
		        }
		        //String user = JOptionPane.showInputDialog("����������");
		        //System.out.println("----�����ļ�<" + fileName +">�ɹ�-------");
			}
	        //dis.close();
	        //fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(dis !=null)
				try {
					dis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            if(fos !=null)
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

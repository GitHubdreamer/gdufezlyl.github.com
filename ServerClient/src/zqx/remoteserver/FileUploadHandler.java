package zqx.remoteserver;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FileUploadHandler extends Thread{
	private Socket client = null;
	private DataInputStream dis;
    private FileOutputStream fos;
    
	public FileUploadHandler(Socket socket){
		this.client = socket;
		start();
	}
	
	public void run(){
		try {
			dis =new DataInputStream(client.getInputStream());
			//�ļ����ͳ���
	        String fileName = dis.readUTF();
	        long fileLength = dis.readLong();
	        fos =new FileOutputStream(new File("D:/BIG_File2333/" + fileName));
	         
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
	        System.out.println("----�����ļ�<" + fileName +">�ɹ�-------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

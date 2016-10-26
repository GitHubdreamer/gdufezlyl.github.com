package zqx.bchandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

public class BCHandler extends Thread{
	private Socket client = null;
    private JTextArea jta1 = null;
    
    OutputStream os = null;  
    PrintWriter pw = null;
    
    InputStream is = null;  
    BufferedReader br = null;  
    
    public BCHandler(Socket client, JTextArea jta1){
    	this.client = client;
    	this.jta1 = jta1;
 	
        start();
    	
    }
    
    @Override
    public void run() {
    	try {  
            //2.�õ�socket��д��  
            OutputStream os=client.getOutputStream();  
            PrintWriter pw=new PrintWriter(os);  
            //������  
            InputStream is=client.getInputStream();  
            BufferedReader br=new BufferedReader(new InputStreamReader(is));  

            //���շ���������Ӧ  
            String reply=null;
            while(true){
            	reply=br.readLine();
            	if(reply != null){
            		jta1.append(reply+"\n");
            	}
            }
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {
        	//4.�ر���Դ  
        	try{
	            br.close();  
	            is.close();  
	            pw.close();  
	            os.close();  
	            client.close(); 
        	} catch(IOException e){
        		e.printStackTrace();
        	}
        }
    }
    
}



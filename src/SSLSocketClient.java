
import java.io.*;
import javax.net.ssl.*;

public class SSLSocketClient {
	
	public static void main(String[] args) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		PrintStream out = System.out;
		
		SSLSocketFactory f = null;
		SSLSocket c = null;
		
		BufferedWriter w = null;
		BufferedReader r = null;

		String password = null;
		
		String realCDKey = "123456";
		String myCDKey = null;
		
		String sServer = "";
		int sPort = -1;
		
		if (args.length != 2) {
			System.out.println("Usage: Classname ServerName securePort");
			System.exit(1);
		}
		sServer = args[0];
		sPort = Integer.parseInt(args[1]);
		try {
			System.out.println("Write CD-KEY : ");
			myCDKey = is.readLine();
			if(realCDKey.equals(myCDKey)) {
				password = "asd123";
			}
			else password = "false";
			
			System.out.println(password);
		} catch(IOException io) {
			System.out.println(io);
			
		}
		
		try {
			System.setProperty("javax.net.ssl.trustStore", "trustedcerts");
			System.setProperty("javax.net.ssl.trustStorePassword", password);
			
			System.out.println("1");
			
			f = (SSLSocketFactory) SSLSocketFactory.getDefault();
			System.out.println("1.5");
			c = (SSLSocket)f.createSocket(sServer, sPort);
			
			System.out.println("2");
			
			String[] supported = c.getSupportedCipherSuites();
			c.setEnabledCipherSuites(supported);
			printSocketInfo(c);
			
			System.out.println("3");
			
			c.startHandshake();
			
			System.out.println("4");
			
			w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
			r = new BufferedReader(new InputStreamReader(c.getInputStream()));
			
			System.out.println("5");
			
			String m = null;
			
			while((m=r.readLine())!=null) {
				out.println(m);
				m = in.readLine();
				w.write(m,0,m.length());
				w.newLine();
				w.flush();
			}
		} catch(IOException io) {
			System.out.println(io);
			try {
				w.close();
				r.close();
				c.close();
			} catch(IOException i) {
				System.out.println(i);
			}
		}
	}
	
	private static void printSocketInfo(SSLSocket s) {
		System.out.println("Socket class: "+s.getClass());
		System.out.println("   Remote address = "
				+s.getInetAddress().toString());
		System.out.println("   Remote port = "+s.getPort());
		System.out.println("   Local socket address = "
				+s.getLocalSocketAddress().toString());
		System.out.println("   Local address = "
				+s.getLocalAddress().toString());
		System.out.println("   Local port = "+s.getLocalPort());
		System.out.println("   Need client authentication = "
				+s.getNeedClientAuth());
		SSLSession ss = s.getSession();
		System.out.println("   Cipher suite = "+ss.getCipherSuite());
		System.out.println("   Protocol = "+ss.getProtocol());
	}
}
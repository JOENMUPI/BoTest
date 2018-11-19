package ORB;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ArgsParser {
	 public static String[] serverInfo(Properties p) {
		 InetAddress address = null;
		 String[] s = new String[4];
		try { address = InetAddress.getLocalHost(); } 
		catch (UnknownHostException e) { System.out.println("ERROR: " + e); e.printStackTrace(); }
		s[0] ="-ORBInitialPort";
		s[1] = p.getProperty("port");
		s[2] = " -ORBInitialHost";
		s[3] = address.getHostAddress();
		return s;
	}
}
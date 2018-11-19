package ORB;

import TEGApp.*; 
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class ClientOrb {
	public static XtoD getXtoDImpl(ORB o) {
		try {
			return XtoDHelper.narrow(NamingContextExtHelper.narrow(o.resolve_initial_references("NameService")).resolve_str("XtoD"));
		}
		
		catch (Exception e) { System.out.println("Error: " + e); e.printStackTrace(System.out); return null;}
	}
	
	public static CtoB getStoPImpl(ORB o) {
		try {
			return CtoBHelper.narrow(NamingContextExtHelper.narrow(o.resolve_initial_references("NameService")).resolve_str("CtoB"));
		} 
		
		catch (Exception e) { System.out.println("Error: " + e); e.printStackTrace(System.out); return null; }
	}
}
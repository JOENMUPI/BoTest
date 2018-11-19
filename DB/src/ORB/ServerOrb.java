package ORB;
import TEGApp.*;
import Utilities.Props;
import Utilities.Serial;

import org.omg.CosNaming.*;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import ORG.DB;
import ORG.PoolManager;
import ORG.PropertiesMap;

class XtoDImpl extends XtoDPOA {
	private ORB orb; 
		
	public void setORB(ORB orb_val) { orb = orb_val; }
	public boolean test() { return true; }
	public void shutdown() { this.orb.shutdown(false); }
	public byt dataRequest(XD data) {
		byt b = new byt();
		if(data.params == null) { 
			b.obj = Serial.serializeDS(new DB().query(data.queryId, data.schema)); 
		} else { 
			b.obj = Serial.serializeDS(new DB().query(data.queryId, data.schema, Serial.deserializeParams(data.params)));
		}
		
		return b; 
	}
}

public class ServerOrb {
	public static void main(String args[]) {
		try {
			start();
			ORB orb = ORB.init(args, null); 
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			XtoDImpl XtoDImpl = new XtoDImpl(); 
			XtoDImpl.setORB(orb);  
			NamingContextExt ncRef = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
			NameComponent path[] = ncRef.to_name("XtoD");
			ncRef.rebind(path, XtoDHelper.narrow(rootpoa.servant_to_reference(XtoDImpl)));
			System.out.println("ServidorOrb DB listo y en espera");
			orb.run();
		}
		
		catch (Exception e) { System.err.println("Error: " + e); e.printStackTrace(System.out); }
		System.out.println("Adios, cerrando servidor DB");
	}
	
	private static void start() {
		PoolManager.InitializePool(Props.getPropertiesFile("config", "db"));
		new PropertiesMap().loadProperties("config", "elementaries");//insrtar los .properties de b.o aqui
	}
}
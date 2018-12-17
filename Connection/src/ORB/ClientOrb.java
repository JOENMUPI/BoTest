package ORB;

import TEGApp.*; 
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class ClientOrb {
	public static XtoD getXtoDImpl(ORB o) {
		try {
			return XtoDHelper.narrow(NamingContextExtHelper.narrow(o.resolve_initial_references("NameService")).resolve_str("XtoD"));
		} catch (Exception e) { e.printStackTrace(System.out); return null; }
	}
	
	public static CtoB getStoPImpl(ORB o) {
		try {
			return CtoBHelper.narrow(NamingContextExtHelper.narrow(o.resolve_initial_references("NameService")).resolve_str("CtoB"));
		} catch (Exception e) { e.printStackTrace(System.out); return null; }
	}
}
	
	
	
	
	//DII
//    public static void main (String[] args) {   
//    	ORB myORB = ORB.init(args, null); 
//		try { 	  
//		    NameComponent[] path = { new NameComponent("NASDAQ", "") }; 
//		    NVList argList = myORB.create_list(1); 
//		    Any argument = myORB.create_any(); 
//		    argument.insert_string("MY_COMPANY"); 
//		    
//		    Any result = myORB.create_any(); 
//		    result.insert_float(0); 
//		    
//		    Request thisReq = NamingContextHelper.narrow(myORB.resolve_initial_references("NameService")).resolve (path)._create_request (null, "get_price", argList, myORB.create_named_value ("result", result, org.omg.CORBA.ARG_OUT.value)); 
//		    thisReq.send_deferred(); 
//		   
//		    result = thisReq.get_response(); 
//		    System.out.println ("get_price() returned: " + result.extract_float()); 
//		} catch(Exception e) { e.printStackTrace(); } 
//    } 
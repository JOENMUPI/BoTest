package TEGApp;


/**
* TEGApp/XtoCHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from TEG.idl
* lunes 17 de diciembre de 2018 09:34:54 AM VET
*/

abstract public class XtoCHelper
{
  private static String  _id = "IDL:TEGApp/XtoC:1.0";

  public static void insert (org.omg.CORBA.Any a, TEGApp.XtoC that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static TEGApp.XtoC extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (TEGApp.XtoCHelper.id (), "XtoC");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static TEGApp.XtoC read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_XtoCStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, TEGApp.XtoC value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static TEGApp.XtoC narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof TEGApp.XtoC)
      return (TEGApp.XtoC)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      TEGApp._XtoCStub stub = new TEGApp._XtoCStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static TEGApp.XtoC unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof TEGApp.XtoC)
      return (TEGApp.XtoC)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      TEGApp._XtoCStub stub = new TEGApp._XtoCStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}

package TEGApp;


/**
* TEGApp/CtoBHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from TEG.idl
* domingo 18 de noviembre de 2018 09:33:50 PM VET
*/

abstract public class CtoBHelper
{
  private static String  _id = "IDL:TEGApp/CtoB:1.0";

  public static void insert (org.omg.CORBA.Any a, TEGApp.CtoB that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static TEGApp.CtoB extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (TEGApp.CtoBHelper.id (), "CtoB");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static TEGApp.CtoB read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_CtoBStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, TEGApp.CtoB value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static TEGApp.CtoB narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof TEGApp.CtoB)
      return (TEGApp.CtoB)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      TEGApp._CtoBStub stub = new TEGApp._CtoBStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static TEGApp.CtoB unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof TEGApp.CtoB)
      return (TEGApp.CtoB)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      TEGApp._CtoBStub stub = new TEGApp._CtoBStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}

package TEGApp;

/**
* TEGApp/XtoDHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from TEG.idl
* lunes 17 de diciembre de 2018 09:34:54 AM VET
*/

public final class XtoDHolder implements org.omg.CORBA.portable.Streamable
{
  public TEGApp.XtoD value = null;

  public XtoDHolder ()
  {
  }

  public XtoDHolder (TEGApp.XtoD initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = TEGApp.XtoDHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    TEGApp.XtoDHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TEGApp.XtoDHelper.type ();
  }

}

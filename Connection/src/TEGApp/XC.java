package TEGApp;


/**
* TEGApp/XC.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from TEG.idl
* domingo 18 de noviembre de 2018 09:33:50 PM VET
*/

public final class XC implements org.omg.CORBA.portable.IDLEntity
{
  public String sessionId = null;
  public String typeResponse = null;
  public byte Response[] = null;

  public XC ()
  {
  } // ctor

  public XC (String _sessionId, String _typeResponse, byte[] _Response)
  {
    sessionId = _sessionId;
    typeResponse = _typeResponse;
    Response = _Response;
  } // ctor

} // class XC

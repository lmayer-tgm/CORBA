package cb;

import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;

/**
 * Client.java
 *
 * Obtains a reference of a remote object and creates also a POA where the Server can "callback" the client
 * The callback methode gets invoked every 4 seconds and prints a message out
 * Created: Mon Sep 3 19:28:34 2008
 *
 * @author Nicolas Noffke, Lukas Mayer
 */

public class Client extends CallBackPOA
{
    public Client ()
    {
    }

    public void call_back (String message)
    {
        System.out.println ("Client callback object received hello message >"
                + message + '<');
    }

    /**
    * obtains a reference over a naming service and also starts a POA
    * "registers" the callback at the server and shuts the server down if a user input happens
    */
    public static void main (String[] args) throws Exception
    {
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, null); //args = address of the naming service

	Object o = orb.resolve_initial_references("NameService");
	NamingContextExt rootContext = NamingContextExtHelper.narrow(o); //casting
	NameComponent[] name = new NameComponent[2];
	name[0] = new NameComponent("test","my_context");
	name[1] = new NameComponent("Server", "Object");

        Server server = ServerHelper.narrow (rootContext.resolve(name)); //obtaining a reference

        POA root_poa = (POA) orb.resolve_initial_references ("RootPOA");
        root_poa.the_POAManager().activate(); //starting the root POA

	//Creating and casting a new Callback-Object
        CallBack ccb = CallBackHelper.narrow(root_poa.servant_to_reference(new Client()));
	server.register(ccb, "worked! methods gets invoked every 4 seconds", (short) 4);

	while (System.in.read() == '\n'){
        	server.shutdown();
		System.exit(1);
	}
    }
}// Client

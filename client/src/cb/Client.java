package org.jacorb.demo.bidir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

import org.omg.BiDirPolicy.BIDIRECTIONAL_POLICY_TYPE;
import org.omg.BiDirPolicy.BOTH;
import org.omg.BiDirPolicy.BidirectionalPolicyValueHelper;
import org.omg.CORBA.Any;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;

/**
 * Client.java
 *
 *
 * Created: Mon Sep 3 19:28:34 2001
 *
 * @author Nicolas Noffke, Lukas Mayer
 */

public class Client extends ClientCallbackPOA
{
    public Client ()
    {
    }

    public void hello (String message)
    {
        System.out.println ("Client callback object received hello message >"
                + message + '<');
    }

    public static void main (String[] args) throws Exception
    {
	//Property-File loeschen, da ein Nameing-Service verwendet wird
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, null); //adresse des naming-service

	Object o =orb.resolve_initial_references("NameService");
	NamingContextExt rootContext = NamingContextExtHelper.narrow(o);
	NameComponent[] name = new NameComponent[2];
	name[0] = new NameComponent("test","my_context");
	name[1] = new NameComponent("Server", "Object");

        Server server = ServerHelper.narrow (rootContext.resolve(name));

        POA root_poa = (POA) orb.resolve_initial_references ("RootPOA");
        root_poa.the_POAManager().activate();

        CallBack ccb = ClientCallbackHelper.narrow(root_poa.servant_to_reference(new Client()));
	server.register(ccb, "every 4 seconds", 4);

	while (System.in.read() == '\n'){
        	server.shutdown();
	}
    }
}// Client

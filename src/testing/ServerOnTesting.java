package testing;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import server.Server;

public class ServerOnTesting {

    @Test
	void test1() { // checks if the port is reachable while server is up
		TestCase.assertTrue(Server.serverListening("localhost",9999));
	}

}
package factory;

import server.CustomServer;
import server.impl.MultiThreadedServer;
import server.impl.SingleThreadedServer;
import server.impl.ThreadPooledServer;
import configuration.ServerConfiguration;

public class ServerFactory {
	ServerConfiguration conf;
	
	public ServerFactory(ServerConfiguration conf){
		this.conf = conf;
	}
	
	public CustomServer getServerInstance(){
		CustomServer server = null;
		if(!conf.isThreadedServer())
			return new SingleThreadedServer(conf);
		else if(!conf.isThreadPooledServer())
			return new MultiThreadedServer(conf);
		else
		return new ThreadPooledServer(conf);
	}
}

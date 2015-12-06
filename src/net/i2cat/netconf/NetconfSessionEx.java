package net.i2cat.netconf;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.i2cat.netconf.errors.TransportException;
import net.i2cat.netconf.errors.TransportNotRegisteredException;
import net.i2cat.netconf.rpc.RPCElement;
import net.i2cat.netconf.rpc.Reply;
import net.i2cat.netconf.transport.TransportFactory;

public class NetconfSessionEx extends NetconfSession {

	private Log	logEx		= LogFactory.getLog(NetconfSessionEx.class);
	
	public NetconfSessionEx(SessionContext sessionContext)
			throws TransportNotRegisteredException, ConfigurationException {
		super(sessionContext);
	}
	
	public NetconfSessionEx(SessionContext sessionContext, TransportFactory transportFactory)
			throws TransportNotRegisteredException, ConfigurationException {
		super(sessionContext, transportFactory);
	}
	
	@SuppressWarnings("serial")
	public Reply sendSyncRequest(final String request) throws TransportException {

		logEx.info("Sending request (" + request + ")");

		transport.sendAsyncQuery(new RPCElement() {
			public String toXML() {
				return request;
			}
		});

		logEx.info("Sent. Waiting for response...");
		Reply reply = null;
		try {
			if (sessionContext.containsKey(SessionContext.TIMEOUT)) {
				reply = (Reply) messageQueue.blockingConsume();
			}
		} 
		catch (Exception e) {
			throw new TransportException("Error getting reply to request: " + e.getMessage(), e);
		}
		logEx.debug("--------------------------------------------------");
		logEx.debug("receiving REPLY + " + reply);
		
		logEx.debug(reply.getContain());
		logEx.debug("--------------------------------------------------");

		logEx.info("Reply received");

		return reply;
	}

}

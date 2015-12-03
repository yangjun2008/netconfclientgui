package com.anonymous.tool.netconfclient;

import java.net.URI;

import org.apache.commons.configuration.ConfigurationException;

import com.anonymous.tool.netconfclient.execption.LoginFailedException;
import com.anonymous.tool.netconfclient.execption.RequestSendFailedException;

import net.i2cat.netconf.NetconfSession;
import net.i2cat.netconf.SessionContext;
import net.i2cat.netconf.SessionContext.AuthType;
import net.i2cat.netconf.errors.TransportException;
import net.i2cat.netconf.rpc.Error;
import net.i2cat.netconf.rpc.Operation;
import net.i2cat.netconf.rpc.Query;
import net.i2cat.netconf.rpc.Reply;

public class NetConfClient {
	
	private NetconfSession session;
	private SessionContext sessionContext;
	
	public void login(String ipAddress, String userName, String password) throws LoginFailedException {
		try {
			sessionContext = new SessionContext();
			sessionContext.setAuthenticationType(AuthType.PASSWORD);
			
			StringBuffer uriBuffer = new StringBuffer();
			uriBuffer.append("ssh://");
			uriBuffer.append(userName);
			uriBuffer.append(":");
			uriBuffer.append(password);
			uriBuffer.append("@");
			uriBuffer.append(ipAddress);
			uriBuffer.append(":22");
			sessionContext.setURI(new URI(uriBuffer.toString()));
	
			session = new NetconfSession(sessionContext);
			session.connect();
		} catch (Exception e) {
			throw new LoginFailedException(e.getMessage());
		}
	}
	
	public void logout() {
		sessionContext = null;
		session.disconnect();
		session = null;
	}

	public String send(String request) throws RequestSendFailedException {
		Query query = new Query();
		query.setOperation(Operation.GET_CONFIG);
		query.setSource(request);
		
		try {
			Reply reply = session.sendSyncQuery(query);
			/* check first messages */
			if (reply.containsErrors()) {
				String msg = sortErrors(reply);
				throw new RequestSendFailedException(msg);
			}
			return reply.toXML();
		} catch (TransportException e) {
			throw new RequestSendFailedException(e.getMessage());
		}
		
	}
	
	private String sortErrors(Reply reply) {
		String msg = "";
		for (Error error : reply.getErrors()) {
			msg += error.getMessage() + '\n';
		}
		return msg;

	}


}

package com.anonymous.tool.netconfclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.anonymous.tool.netconfclient.execption.LoginFailedException;
import com.anonymous.tool.netconfclient.execption.RequestSendFailedException;

public class NetConfShellClient {
	
	private NetConfClient netconfClient;
	
	private StringBuffer requestBuffer = new StringBuffer();

	public static void main(String[] args) throws IOException  {
		Locale.setDefault(new Locale("en", "US"));
		System.out.println("Welcome to use netconf shell client!");
		printlnSign();
		
		NetConfShellClient shellClient = new NetConfShellClient();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while((line = reader.readLine()) != null) {
			shellClient.handleInput(line);
		}
	}
	
	private void handleInput(String line) {
		String msg = line.trim();
		if(msg.equals("")) {
			printlnSign();
			return;
		}
		else if(netconfClient == null && !msg.startsWith("connect")) {
			System.out.println("Error: no connection. please connect first. ");
			System.out.println("For example: connect ip 10.10.10.10 user admin password admin port 22");
			printlnSign();
		}
		else if(msg.startsWith("<rpc") || requestBuffer.length() > 0) {
			hanldeRequest(msg);
		}
		else if(msg.startsWith("connect")) {
			handleConnection(msg);
		}
	}
	
	private void hanldeRequest(String msg) { 
		requestBuffer.append(msg+"\n");
		if(msg.endsWith("</rpc>")) {
			try {
				String response = netconfClient.send(requestBuffer.toString());
				System.out.println(response);
			} catch (RequestSendFailedException e) {
				System.out.println("Error: "+e.getMessage());
				e.printStackTrace();
			}
			requestBuffer.delete(0, requestBuffer.length()-1);
			printlnSign();
		}
		
	}
	
	/**
	 * connect ip 10.10.10.10 user admin password sssss port 222
	 * @param msg
	 */
	private void handleConnection(String msg) {
		String[] params = msg.split(" ");
		int i = 0;
		Map<String, String> connInfoMap = new HashMap<String, String>();
		while(i < params.length-1) {
			if(fetchParamValue("ip", params, i, connInfoMap)) {
				i++;
				continue;
			}
			if(fetchParamValue("user", params, i, connInfoMap)) {
				i++;
				continue;
			}
			if(fetchParamValue("password", params, i, connInfoMap)) {
				i++;
				continue;
			}
			if(fetchParamValue("port", params, i, connInfoMap)) {
				i++;
				continue;
			}
			i++;
		}
		String ip = connInfoMap.get("ip");
		String user = connInfoMap.get("user");
		String password = connInfoMap.get("password");
		String port = connInfoMap.get("port");
		
		if(ip == null) {
			System.out.println("Error: ip == null.");
			netconfClient = null;
		}
		else if(user== null) {
			System.out.println("Error: user == null.");
			netconfClient = null;
		}
		else if(password == null) {
			System.out.println("Error: password == null.");
			netconfClient = null;
		}
		else if(port == null) {
			System.out.println("Error: port == null.");
			netconfClient = null;
		}
		else {
			netconfClient = new NetConfClient();
			try {
				netconfClient.login(ip, user, password, port);
			} catch (LoginFailedException e) {
				System.out.println("Error: login falied, " + e.getMessage());
				e.printStackTrace();
				netconfClient = null;
			}
		}
		printlnSign();
	}
	
	private boolean fetchParamValue(String paramName, String[] allParams, int index, Map<String, String> map) {
		String curParamName = allParams[index].trim();
		if(paramName.equals(curParamName)) {
			map.put(paramName, allParams[index+1]);
			return true;
		}
		return false;
	}
	
	private static void printlnSign() {
		System.out.print(">>> ");
	}
}

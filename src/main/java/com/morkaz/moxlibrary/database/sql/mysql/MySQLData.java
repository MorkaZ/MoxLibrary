package com.morkaz.moxlibrary.database.sql.mysql;

import java.sql.Connection;

public class MySQLData {

	private String host;
	private String port;
	private String databaseName;
	private String user;
	private String password;
	public Connection connection;


	public MySQLData(String host, String port, String databaseName, String user, String password) {
		this.host = host;
		this.port = port;
		this.databaseName = databaseName;
		this.user = user;
		this.password = password;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	public String getDatabaseName() {
		return databaseName;
	}


	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Connection getConnection() {
		return connection;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}

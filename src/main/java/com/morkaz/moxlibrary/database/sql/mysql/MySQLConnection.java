package com.morkaz.moxlibrary.database.sql.mysql;

import java.sql.*;

public class MySQLConnection extends MySQLData {

	public MySQLConnection(String host, String port, String databaseName, String user, String password) {
		super(host, port, databaseName, user, password);
		try {
			super.connection = openConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	public Connection openConnection() throws SQLException, ClassNotFoundException {
		if (checkConnection()) {
			return connection;
		}

		String connectionURL = "jdbc:mysql://" + super.getHost() + ":" + super.getPort();
		if (getDatabaseName() != null) {
			connectionURL = connectionURL + "/" + this.getDatabaseName();
		}

		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(connectionURL, super.getUser(), super.getPassword());
		return connection;
	}


	public boolean checkConnection() throws SQLException {
		return connection != null && !connection.isClosed();
	}


	public Connection getConnection() {
		return connection;
	}


	public boolean closeConnection() throws SQLException {
		if (connection == null) {
			return false;
		}
		connection.close();
		return true;
	}


	public ResultSet querySQL(String query) throws SQLException,
			ClassNotFoundException {
		if (!checkConnection()) {
			openConnection();
		}
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result;
	}


	public int updateSQL(String query) throws SQLException,
			ClassNotFoundException {
		if (!checkConnection()) {
			openConnection();
		}
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(query);
		return result;
	}

}




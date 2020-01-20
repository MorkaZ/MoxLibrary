package com.morkaz.moxlibrary.database.sql.sqlite;


import com.morkaz.moxlibrary.api.QueryUtils;
import com.morkaz.moxlibrary.database.sql.SQLDatabase;
import com.morkaz.moxlibrary.database.sql.SQLDatabaseType;
import com.morkaz.moxlibrary.database.sql.SQLScheduler;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.List;

public class SQLiteDatabase extends SQLScheduler implements SQLDatabase {

	private Plugin plugin;
	public Connection connection;
	private String fileLocation;


	public SQLiteDatabase(Plugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public SQLDatabaseType getDatabaseType() {
		return SQLDatabaseType.SQLITE;
	}

	@Override
	public void updateAsync(String query) {
		super.scheduleRunnable(new Runnable() {
			public void run() {
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
					Bukkit.getConsoleSender().sendMessage("[MoxLibrary] SQL query error: "+e.getSQLState()+" ---- "+e.getCause()
							+" ---- "+e.getMessage()+" ---- "+e.getErrorCode()+" ---- USED QUERY:"+query);
				}
			}
		});
	}

	@Override
	public Boolean updateSync(String query) {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[MoxLibrary] SQL query error: "+e.getSQLState()+" ---- "+e.getCause()
					+" ---- "+e.getMessage()+" ---- "+e.getErrorCode()+" ---- USED QUERY: "+query);
		}
		return false;
	}

	@Override
	public ResultSet getResult(String query) {
		ResultSet set = null;
		try {
			Statement statement = connection.createStatement();
			set = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[MoxLibrary] SQL get data from query error: ["+e.getErrorCode()+"] "+e.getMessage()+"\n" +
					"Used query: "+query);
		}
		return set;
	}

	/**
	 * @param data Data of connection.
	 *             <p>data[0] - filename</p>
	 * @return
	 */
	@Override
	public Connection createConnection(String... data) {
		this.fileLocation = data[0];
		Connection connection = null;
		String url = "jdbc:sqlite:" + fileLocation;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(url);
			if (connection == null) {
				Bukkit.getLogger().info("[MoxLibrary] Creating SQLite connection to file failed. " +
						"Check if given location is correct and required access is given: "+fileLocation+"");
			}
			Bukkit.getLogger().info("[MoxLibrary] URL ("+url+") Succesfully connected! Driver version: "+connection.getMetaData().getDriverVersion());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.connection = connection;
		return connection;
	}


	@Override
	public Connection getConnection() {
		try {
			if (this.connection != null && !this.connection.isClosed()){
				return this.connection;
			} else if (fileLocation != null){
				return createConnection(fileLocation);
			}
		} catch (SQLException e) {
			if (fileLocation != null){
				return createConnection(fileLocation);
			}
		}
		return null;
	}

	@Override
	public Boolean isConnected() {
		if (connection != null){
			try {
				return !connection.isClosed();
			} catch (SQLException e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public void closeConnection() {
		if (connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				connection = null;
			}
		}
	}

	@Override
	public void removeObjectSync(String table, String keyColumn, Object keyValue, String objectColumn) {
		String query = QueryUtils.constructQueryValueRemove(table, keyColumn, keyValue, objectColumn, SQLDatabaseType.SQLITE);
		this.updateSync(query);
	}

	@Override
	public void removeObjectAsync(String table, String keyColumn, Object keyValue, String objectColumn) {
		String query = QueryUtils.constructQueryValueRemove(table, keyColumn, keyValue, objectColumn, SQLDatabaseType.SQLITE);
		this.updateAsync(query);
	}

	@Override
	public void setObjectSync(String table, String keyColumn, Object keyValue, String objectColumn, Object objectValue, Boolean hasTablePrimaryKey) {
		QueryUtils.constructQuerySingleValueSet(table, keyColumn, keyValue, objectColumn, objectValue, hasTablePrimaryKey, SQLDatabaseType.SQLITE)
				.forEach(this::updateAsync);
	}

	@Override
	public void setObjectAsync(String table, String keyColumn, Object keyValue, String objectColumn, Object objectValue, Boolean hasTablePrimaryKey) {
		QueryUtils.constructQuerySingleValueSet(table, keyColumn, keyValue, objectColumn, objectValue, hasTablePrimaryKey, SQLDatabaseType.SQLITE)
				.forEach(this::updateAsync);
	}

	@Override
	public void setObjectsSync(String table, List<Pair<String, Object>> pairs, Boolean hasTablePrimaryKey) {
		QueryUtils.constructQueryMultipleValuesSet(table, pairs, hasTablePrimaryKey, SQLDatabaseType.SQLITE)
				.forEach(this::updateSync);
	}

	@Override
	public void setObjectsAsync(String table, List<Pair<String, Object>> pairs, Boolean hasTablePrimaryKey) {
		QueryUtils.constructQueryMultipleValuesSet(table, pairs, hasTablePrimaryKey, SQLDatabaseType.SQLITE)
				.forEach(this::updateSync);
	}

	@Override
	public ResultSet getRow(String table, String keyColumn, String keyValue) {
		String query = QueryUtils.constructQueryRowGet(table, keyColumn, keyValue, SQLDatabaseType.SQLITE);
		return this.getResult(query);
	}

	@Override
	public ResultSet getRows(String table, String keyColumn, String keyValue, int limit) {
		String query = QueryUtils.constructQueryRowsGet(table, keyColumn, keyValue, limit, SQLDatabaseType.SQLITE);
		return this.getResult(query);
	}

	@Override
	public void deleteRowSync(String table, String keyColumn, String keyValue) {
		String query = QueryUtils.constructQueryRowRemove(table, keyColumn, keyValue, SQLDatabaseType.SQLITE);
		this.updateSync(query);
	}

	@Override
	public void deleteRowAsync(String table, String keyColumn, String keyValue) {
		String query = QueryUtils.constructQueryRowRemove(table, keyColumn, keyValue, SQLDatabaseType.SQLITE);
		this.updateAsync(query);
	}

	@Override
	public void deleteRowsSync(String table, String keyColumn, String keyValue, int limit) {
		String query = QueryUtils.constructQueryRowsRemove(table, keyColumn, keyValue, limit, SQLDatabaseType.SQLITE);
		this.updateSync(query);
	}

	@Override
	public void deleteRowsAsync(String table, String keyColumn, String keyValue, int limit) {
		String query = QueryUtils.constructQueryRowsRemove(table, keyColumn, keyValue, limit, SQLDatabaseType.SQLITE);
		this.updateAsync(query);
	}

}

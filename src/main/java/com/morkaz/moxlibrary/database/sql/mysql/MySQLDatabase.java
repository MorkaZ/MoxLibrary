package com.morkaz.moxlibrary.database.sql.mysql;


import com.morkaz.moxlibrary.api.QueryUtils;
import com.morkaz.moxlibrary.database.sql.SQLDatabase;
import com.morkaz.moxlibrary.database.sql.SQLDatabaseType;
import com.morkaz.moxlibrary.database.sql.SQLScheduler;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MySQLDatabase extends SQLScheduler implements SQLDatabase {

	private MySQLConnection mySQLConnection;
	private Plugin plugin;


	public MySQLDatabase(Plugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public SQLDatabaseType getDatabaseType() {
		return SQLDatabaseType.MYSQL;
	}

	@Override
	public void updateAsync(String query) {
		if (mySQLConnection == null){
			Bukkit.getLogger().warning("["+plugin.getName()+"/MoxLibrary] MySQLConnection object is null. It must be initialized by method createConnection(data..).");
			return;
		}
		super.scheduleRunnable(new Runnable() {
			public void run() {
				try {
					Statement statement = mySQLConnection.getConnection().createStatement();
					statement.executeUpdate(query);

				} catch (SQLException e) {
					e.printStackTrace();
					Bukkit.getConsoleSender().sendMessage("[MoxCore] SQL query error: "+e.getSQLState()+" ---- "+e.getCause()
							+" ---- "+e.getMessage()+" ---- "+e.getErrorCode()+" ---- USED QUERY:"+query);
				}
			}
		});
	}

	@Override
	public Boolean updateSync(String query) {
		if (mySQLConnection == null){
			Bukkit.getLogger().warning("["+plugin.getName()+"/MoxLibrary] MySQLConnection object is null. It must be initialized by method createConnection(data..).");
			return null;
		}
		try {
			Statement statement = mySQLConnection.getConnection().createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[MoxCore] SQL query error: "+e.getSQLState()+" ---- "+e.getCause()
					+" ---- "+e.getMessage()+" ---- "+e.getErrorCode()+" ---- USED QUERY:"+query);
		}
		return false;
	}

	@Override
	public ResultSet getResult(String query) {
		if (mySQLConnection == null){
			Bukkit.getLogger().warning("["+plugin.getName()+"/MoxLibrary] MySQLConnection object is null. It must be initialized by method createConnection(data..).");
			return null;
		}
		ResultSet set = null;
		try {
			Statement statement = mySQLConnection.getConnection().createStatement();
			set = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[MoxCore] MySQL get data from query error: "+e.getErrorCode());
		}
		try {
			if (set != null) {
				set.beforeFirst();
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return set;
	}

	/**
	 * @param data Data of mySQLConnection.getConnection()
	 *             <p> data[0] - host </p>
	 *             <p> data[1] - port </p>
	 *             <p> data[2] - database name </p>
	 *             <p> data[3] - user </p>
	 *             <p> data[4] - password </p>
	 * @return opened mySQLConnection.getConnection()
	 */
	@Override
	public Connection createConnection(String... data) {
		try {
			if (mySQLConnection.getConnection() != null && !this.mySQLConnection.getConnection().isClosed()){
				return this.mySQLConnection.getConnection();
			}
		} catch (SQLException e) {
			return null;
		}
		MySQLConnection mySQL = new MySQLConnection(data[0], data[1], data[2], data[3], data[4]);
		try {
			mySQLConnection.setConnection(mySQL.openConnection());
			if (mySQLConnection.getConnection().isClosed()){
				return null;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public Connection getConnection() {
		if (mySQLConnection == null){
			Bukkit.getLogger().warning("["+plugin.getName()+"/MoxLibrary] MySQLConnection object is null. It must be initialized by method createConnection(data..).");
			return null;
		}
		try {
			if (mySQLConnection.getConnection() != null && !this.mySQLConnection.getConnection().isClosed()){
				return mySQLConnection.getConnection();
			} else {
				return createConnection(mySQLConnection);
			}
		} catch (SQLException e) {
			return createConnection(mySQLConnection);
		}
	}

	public Connection createConnection(MySQLData mySQLData){
		return createConnection(mySQLData.getHost(), mySQLData.getPort(), mySQLData.getDatabaseName(), mySQLData.getUser(), mySQLData.getPassword());
	}

	public MySQLConnection getMySQLConnection(){
		return this.mySQLConnection;
	}

	@Override
	public Boolean isConnected() {
		if (mySQLConnection == null){
			Bukkit.getLogger().warning("["+plugin.getName()+"/MoxLibrary] MySQLConnection object is null. It must be initialized by method createConnection(data..).");
			return null;
		}
		if (mySQLConnection.getConnection() != null){
			try {
				return !mySQLConnection.getConnection().isClosed();
			} catch (SQLException e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public void closeConnection() {
		if (mySQLConnection.getConnection() != null){
			try {
				mySQLConnection.getConnection().close();
			} catch (SQLException e) {
				mySQLConnection.setConnection(null);
			}
		}
	}

	@Override
	public void removeObjectSync(String table, String keyColumn, Object keyValue, String objectColumn) {
		String query = QueryUtils.constructQueryValueRemove(table, keyColumn, keyValue, objectColumn, SQLDatabaseType.MYSQL);
		this.updateSync(query);
	}

	@Override
	public void removeObjectAsync(String table, String keyColumn, Object keyValue, String objectColumn) {
		String query = QueryUtils.constructQueryValueRemove(table, keyColumn, keyValue, objectColumn, SQLDatabaseType.MYSQL);
		this.updateAsync(query);
	}

	@Override
	public void setObjectSync(String table, String keyColumn, Object keyValue, String objectColumn, Object objectValue, Boolean hasTablePrimaryKey) {
		QueryUtils.constructQuerySingleValueSet(table, keyColumn, keyValue, objectColumn, objectValue, hasTablePrimaryKey, SQLDatabaseType.MYSQL)
				.forEach(this::updateAsync);
	}

	@Override
	public void setObjectAsync(String table, String keyColumn, Object keyValue, String objectColumn, Object objectValue, Boolean hasTablePrimaryKey) {
		QueryUtils.constructQuerySingleValueSet(table, keyColumn, keyValue, objectColumn, objectValue, hasTablePrimaryKey, SQLDatabaseType.MYSQL)
				.forEach(this::updateAsync);
	}

	@Override
	public void setObjectsSync(String table, List<Pair<String, Object>> pairs, Boolean hasTablePrimaryKey) {
		QueryUtils.constructQueryMultipleValuesSet(table, pairs, hasTablePrimaryKey, SQLDatabaseType.MYSQL)
				.forEach(this::updateAsync);
	}

	@Override
	public void setObjectsAsync(String table, List<Pair<String, Object>> pairs, Boolean hasTablePrimaryKey) {
		QueryUtils.constructQueryMultipleValuesSet(table, pairs, hasTablePrimaryKey, SQLDatabaseType.MYSQL)
				.forEach(this::updateAsync);
	}

	@Override
	public ResultSet getRow(String table, String keyColumn, String keyValue) {
		String query = QueryUtils.constructQueryRowGet(table, keyColumn, keyValue, SQLDatabaseType.MYSQL);
		return this.getResult(query);
	}

	@Override
	public ResultSet getRows(String table, String keyColumn, String keyValue, int limit) {
		String query = QueryUtils.constructQueryRowsGet(table, keyColumn, keyValue, limit, SQLDatabaseType.MYSQL);
		return this.getResult(query);
	}

	@Override
	public void deleteRowSync(String table, String keyColumn, String keyValue) {
		String query = QueryUtils.constructQueryRowRemove(table, keyColumn, keyValue, SQLDatabaseType.MYSQL);
		this.updateSync(query);
	}

	@Override
	public void deleteRowAsync(String table, String keyColumn, String keyValue) {
		String query = QueryUtils.constructQueryRowRemove(table, keyColumn, keyValue, SQLDatabaseType.MYSQL);
		this.updateAsync(query);
	}

	@Override
	public void deleteRowsSync(String table, String keyColumn, String keyValue, int limit) {
		String query = QueryUtils.constructQueryRowsRemove(table, keyColumn, keyValue, limit, SQLDatabaseType.MYSQL);
		this.updateSync(query);
	}

	@Override
	public void deleteRowsAsync(String table, String keyColumn, String keyValue, int limit) {
		String query = QueryUtils.constructQueryRowsRemove(table, keyColumn, keyValue, limit, SQLDatabaseType.MYSQL);
		this.updateAsync(query);
	}


}

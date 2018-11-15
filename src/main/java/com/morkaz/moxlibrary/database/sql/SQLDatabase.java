package com.morkaz.moxlibrary.database.sql;

import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public interface SQLDatabase {

	public SQLDatabaseType getDatabaseType();

	/**
	 * <p>Send query through connection asynchronously in background.
	 * It will not affect TPS, but it will work asynchronously.</p>
	 * <p>Also, database will be updated in delay and data will not be synchronous.
	 * It will be unable to get data from database instantly after update in another line of code.</p>
	 */
	public void updateAsync(String query);

	/**
	 * <p>Send query through connection with synchronization with main thread.
	 * It will affect TPS depends of connection quality.</p>
	 */
	public Boolean updateSync(String query);

	/**
	 * <p>Data from ResultSet can be get using ResultSetManager from this plugin.</p>
	 */
	public ResultSet getResult(String query);

	/**
	 * <p>data values are different depend on type of SQL connection.</p>
	 */
	public Connection createConnection(String... data);

	public Connection getConnection();

	public Boolean isConnected();

	public void closeConnection();

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code removeObjectSync("SQLTable", "nick", "Steve", "money");}</p>
	 */
	public void removeObjectSync(String table, String keyColumn, Object keyValue, String objectColumn);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code removeObjectAsync("SQLTable", "nick", "Steve", "money");}</p>
	 */
	public void removeObjectAsync(String table, String keyColumn, Object keyValue, String objectColumn);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code setObjectSync("SQLTable", "nick", "Steve", "money", 3000);}</p>
	 */
	public void setObjectSync(String table, String keyColumn, Object keyValue, String objectColumn, Object objectValue, Boolean hasTablePrimaryKey);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code setObjectAsync("SQLTable", "nick", "Steve", "money", 3000);}</p>
	 */
	public void setObjectAsync(String table, String keyColumn, Object keyValue, String objectColumn, Object objectValue, Boolean hasTablePrimaryKey);

	/**
	 * <p>Apache commons lib is required for Pair class.</p>
	 * <p>Left value is column, Right value is value object of column.
	 */
	public void setObjectsSync(String table, List<Pair<String, Object>> pairs, Boolean hasTablePrimaryKey);

	/**
	 * <p>Apache commons lib is required for Pair class.</p>
	 * <p>Left value is column, Right value is value object of column.
	 */
	public void setObjectsAsync(String table, List<Pair<String, Object>> pairs, Boolean hasTablePrimaryKey);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code getRow("Players", "nick", "Steve");}</p>
	 */
	public ResultSet getRow(String table, String keyColumn, String keyValue);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code getRow("Players", "premium", true, 5);}</p>
	 */
	public ResultSet getRows(String table, String keyColumn, String keyValue, int limit);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code deleteRow("Players", "nick", "Steve");}</p>
	 */
	public void deleteRowSync(String table, String keyColumn, String keyValue);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code deleteRow("Players", "nick", "Steve");}</p>
	 */
	public void deleteRowAsync(String table, String keyColumn, String keyValue);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code deleteRow("Players", "premium", true, 5);}</p>
	 */
	public void deleteRowsSync(String table, String keyColumn, String keyValue, int limit);

	/**
	 * <p>Keys are used to find correct row.</p>
	 * <p>{@code deleteRow("Players", "premium", true, 5);}</p>
	 */
	public void deleteRowsAsync(String table, String keyColumn, String keyValue, int limit);

}

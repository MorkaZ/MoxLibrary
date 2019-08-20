package com.morkaz.moxlibrary.api;

import com.morkaz.moxlibrary.database.sql.SQLDatabaseType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class QueryUtils {


	public static String constructQueryTableCreate(String tableName, List<Pair<String, String>> columns, String primaryKey, SQLDatabaseType databaseType){
		String query = "CREATE TABLE IF NOT EXISTS "+tableName+" (";
		for (Pair<String, String> pair : columns){
			query = query + "`"+pair.getLeft()+"` "+pair.getRight()+", ";
		}
		if (primaryKey != null){
			query = query + " PRIMARY KEY (`"+primaryKey+"`)";
			query = query + ");";
		} else {
			query = query.substring(0, query.length()-2) + ");";
		}
		return query;
	}

	public static String constructQueryRowGet(String table, String indexColumn, Object indexValue, SQLDatabaseType databaseType){
		String query;
		if (indexValue instanceof Number){
			query = "SELECT * " +
					"FROM `"+table+"` " +
					"WHERE "+indexColumn+" = "+indexValue+" " +
					"LIMIT 1";
		} else {
			query = "SELECT * " +
					"FROM `"+table+"` " +
					"WHERE "+indexColumn+" = '"+indexValue.toString()+"' " +
					"LIMIT 1";
		}
		return query;
	}

	public static String constructQueryRowsGet(String table, String indexColumn, Object indexValue, Object limit, SQLDatabaseType databaseType){
		String query;
		if (indexValue instanceof Number){
			query = "SELECT * " +
					"FROM `"+table+"` " +
					"WHERE `"+indexColumn+"` = "+indexValue+" ";
		} else {
			query = "SELECT * " +
					"FROM `"+table+"` " +
					"WHERE `"+indexColumn+"` = '"+indexValue.toString()+"' ";
		}
		if (!(limit.equals(0) || limit.equals("0"))){
			query = query + "LIMIT "+limit.toString()+"";
		}
		return query;
	}

	public static String constructQueryRowRemove(String table, Pair pair, SQLDatabaseType databaseType){
		String query;
		String column = (String)pair.getLeft();
		Object value = pair.getRight();
		if (value instanceof Number) {
			query = "DELETE FROM `"+table+"` "+
					"WHERE `"+column+"` = "+value+" ";
		} else {
			query = "DELETE FROM `"+table+"` "+
					"WHERE `"+column+"` = '"+value+"' ";
		}
		if (databaseType != SQLDatabaseType.SQLITE){
			query = query + "LIMIT 1";
		}
		return query;
	}

	public static String constructQueryRowRemove(String table, String column, Object value, SQLDatabaseType databaseType){
		String query;
		if (value instanceof Number) {
			query = "DELETE FROM `"+table+"` " +
					"WHERE `"+column+"` = "+value+" ";
		} else {
			query = "DELETE FROM `"+table+"` " +
					"WHERE `"+column+"` = '"+value+"' ";
		}
		if (databaseType != SQLDatabaseType.SQLITE){
			query = query + "LIMIT 1";
		}
		return query;
	}

	public static String constructQueryRowsRemove(String table, String column, Object value, Object limit, SQLDatabaseType databaseType){
		String query;
		if (value instanceof Number) {
			query = "DELETE FROM `"+table+"` " +
					"WHERE `"+column+"` = "+value+" ";
		} else {
			query = "DELETE FROM `"+table+"` " +
					"WHERE `"+column+"` = '"+value+"' ";
		}
		if (!(limit.equals(0) || limit.equals("0"))){
			query = query + "LIMIT "+limit.toString()+"";
		}
		return query;
	}

	public static String constructQueryRowsRemove(String table, Pair pair, Object limit, SQLDatabaseType databaseType){
		String query;
		String column = (String)pair.getLeft();
		Object value = pair.getRight();
		if (value instanceof Number) {
			query = "DELETE FROM `"+table+"` " +
					"WHERE `"+column+"` = "+value+" ";
		} else {
			query = "DELETE FROM `"+table+"` " +
					"WHERE `"+column+"` = '"+value+"' ";
		}
		if (!(limit.equals(0) || limit.equals("0"))){
			query = query + "LIMIT "+limit.toString()+"";
		}
		return query;
	}

	/**
		@param indexColumn - column to "WHERE" query's part
		@param indexValue - value to "WHERE" query's part
		@param valueColumn - column that will be NULLed in row
	 */
	public static String constructQueryValueRemove(String table, String indexColumn, Object indexValue, String valueColumn, SQLDatabaseType databaseType){
		String query;
		if (indexValue instanceof Number){
			query = "UPDATE `"+table+"` " +
					"SET `"+valueColumn+"` = NULL " +
					"WHERE `"+table+"`.`"+indexColumn+"` = "+indexValue;
		} else {
			query = "UPDATE `"+table+ "` " +
					"SET `"+valueColumn+"` = NULL " +
					"WHERE `"+table+"`.`"+indexColumn+"` = '"+indexValue.toString()+"'";
		}
		return query;
	}

	public static String constructQueryValueRemove(String table, Pair indexPair, String column, SQLDatabaseType databaseType){
		String query;
		if (indexPair.getRight() instanceof Number){
			query = "UPDATE `"+table+ "` " +
					"SET `"+column+"` = NULL " +
					"WHERE `"+table+"`.`"+indexPair.getLeft()+"` = "+indexPair.getRight();
		} else {
			query = "UPDATE `"+table+"` " +
					"SET `"+column+"` = NULL " +
					"WHERE `"+table+"`.`"+indexPair.getLeft()+"` = '"+indexPair.getRight().toString()+"'";
		}
		return query;
	}

	public static String constructQueryValueRemove(String table, Pair indexPair, Pair valuePair, SQLDatabaseType databaseType){
		String query;
		if (indexPair.getRight() instanceof Number){
			query = "UPDATE `"+table+ "` " +
					"SET `"+valuePair.getLeft()+"` = NULL " +
					"WHERE `"+table+"`.`"+indexPair.getLeft()+"` = "+indexPair.getRight();
		} else {
			query = "UPDATE `"+table+ "` " +
					"SET `"+valuePair.getLeft()+"` = NULL " +
					"WHERE `"+table+"`.`"+indexPair.getLeft()+"` = '"+indexPair.getRight().toString()+"'";
		}
		return query;
	}

//	String query1 = "INSERT OR IGNORE INTO `"+table+"` VALUES ("+keyValue+", "+setValueTrans+") ";
//	String query2 = "UPDATE `"+table+"` SET `"+setColumn+"` = "+setValueTrans+" WHERE `"+keyValue+"` = "+keyValueTrans+" ";
	public static List<String> constructQuerySingleValueSet(String table, String keyColumn, Object keyValue, String setColumn, Object setValue,
											   Boolean hasTableUniqueKey, SQLDatabaseType databaseType){
		List<String> queryList = new ArrayList<>();
		String setValueTrans = setValue+"";
		if (setValue == null || (""+setValue).equalsIgnoreCase("null")){
			setValueTrans = "NULL";
		} else if (!(setValue instanceof Number)) {
			setValueTrans = "'" + setValueTrans + "'";
		}
		String keyValueTrans = keyValue+"";
		if (keyValue == null || (""+keyValue).equalsIgnoreCase("null")){
			keyValueTrans = "NULL";
		} else if (!(keyValue instanceof Number)) {
			keyValueTrans = "'" + keyValueTrans + "'";
		}
		if (databaseType == SQLDatabaseType.MYSQL){
			String query;
			query = "INSERT INTO `"+table+"` "
					+ "(`"+keyColumn+"`, `"+setColumn+"`) "
					+ "VALUES ("+keyValueTrans+", "+setValueTrans+") ";
			if (hasTableUniqueKey) {
				query = query
						+ "ON DUPLICATE KEY UPDATE "
						+ "`"+setColumn+"` = "+setValueTrans+" ";
			}
			queryList.add(query);
		} else {
			//It will be uncommented when newest version of SQLIte driver will be released in java's JDK.
			//At this moment SQLite does not support UPSERT query like MySQL does.
//			query = "INSERT INTO `"+table+"` "
//					+ "(`"+keyColumn+"`, `"+setColumn+"`) "
//					+ "VALUES ("+keyValueTrans+", "+setValueTrans+") ";
//			if (hasTableUniqueKey) {
//				query = query
//						+ "ON CONFLICT (`"+keyColumn+"`) "
//						+ "DO UPDATE SET "
//						+ "`"+setColumn+"` = "+setValueTrans+"";
//			}
			String begining = "INSERT";
			if (hasTableUniqueKey) {
				begining = "INSERT OR IGNORE";
			}
			String query1 = begining+" INTO `"+table+"` "
					+ "(`"+keyColumn+"`, "+setColumn+") "
					+ "VALUES ("+keyValueTrans+", "+setValueTrans+")";
			if (hasTableUniqueKey) {
				String query2 = "UPDATE `"+table+"` "
						+ "SET `"+setColumn+"` = "+setValueTrans+" "
						+ "WHERE `"+keyColumn+"` = "+keyValueTrans+" ";
				queryList.add(query2);
			}
			queryList.add(query1);
		}
		return queryList;
	}

	public static List<String> constructQuerySingleValueSet(String table, Pair keyPair, Pair setPair, Boolean hasTableUniqueKey, SQLDatabaseType databaseType){
		String keyColumn = (String)keyPair.getLeft(),
				setColumn = (String)setPair.getLeft();
		Object keyValue = keyPair.getRight(),
				setValue = setPair.getRight();
		List<String> queryList = constructQuerySingleValueSet(table, keyColumn, keyValue, setColumn, setValue, hasTableUniqueKey, databaseType);
		return queryList;
	}

	public static List<String> constructQuerySingleValueSum(String table, Pair keyPair, Pair addPair, Boolean hasTableUniqueKey, SQLDatabaseType databaseType){
		List<String> queryList = new ArrayList<>();
		String keyColumn = (String)keyPair.getLeft(),
				setColumn = (String)addPair.getLeft();
		Object keyValue = keyPair.getRight(),
				setValue = addPair.getRight();
		String keyValueTrans = keyValue+"";
		if (keyValue == null || (""+keyValue).equalsIgnoreCase("null")){
			keyValueTrans = "NULL";
		} else if (!(keyValue instanceof Number)) {
			keyValueTrans = "'" + keyValueTrans + "'";
		}
		if (databaseType == SQLDatabaseType.MYSQL){
			String query;
			query = "INSERT INTO `"+table+"` "
					+ "(`"+keyColumn+"`, `"+setColumn+"`) "
						+ "VALUES ("+keyValueTrans+", "+setValue+") ";
			if (hasTableUniqueKey) {
				query = query
						+ "ON DUPLICATE KEY UPDATE "
						+ "`"+setColumn+"` = coalesce("+setColumn+" + "+setValue+", "+setValue+") ";
			}
			queryList.add(query);
		} else {
//			query = "INSERT INTO `"+table+"` "
//					+ "(`"+keyColumn+"`, `"+setColumn+"`) "
//					+ "VALUES ("+keyValueTrans+", "+setValue+") ";
//			if (hasTableUniqueKey) {
//				query = query
//						+ "ON CONFLICT (`"+keyColumn+"`) "
//						+ "DO UPDATE SET "
//						+ "`"+setColumn+"` = "+setColumn+" + "+setValue+"";
//			}
			if (hasTableUniqueKey) {
				String query1, query2;
				query1 = "INSERT OR IGNORE INTO `"+table+"` "
						+ "(`"+keyColumn+"`, "+setColumn+") "
						+ "VALUES ("+keyValueTrans+", 0)";
				query2 = "UPDATE `"+ table+"` "
						+ "SET `"+setColumn+"` = coalesce("+setColumn+" + "+setValue+", "+setValue+") "
						+ "WHERE `"+keyColumn+"` = "+keyValueTrans+" ";
				queryList.add(query1);
				queryList.add(query2);
			} else {
				String query1;
				query1 = "INSERT INTO `"+table+"` "
						+ "(`"+keyColumn+"`, "+setColumn+") "
						+ "VALUES ("+keyValueTrans+", "+setValue+")";
				queryList.add(query1);
			}
		}
		return queryList;
	}

	public static List<String> constructQuerySingleValueSubtract(String table, Pair keyPair, Pair addPair, Boolean hasTableUniqueKey, SQLDatabaseType databaseType){
		List<String> queryList = new ArrayList<>();
		String keyColumn = (String)keyPair.getLeft(),
				setColumn = (String)addPair.getLeft();
		Object keyValue = keyPair.getRight(),
				setValue = addPair.getRight();
		String keyValueTrans = keyValue+"";
		if (keyValue == null || (""+keyValue).equalsIgnoreCase("null")){
			keyValueTrans = "NULL";
		} else if (!(keyValue instanceof Number)) {
			keyValueTrans = "'" + keyValueTrans + "'";
		}
		if (databaseType == SQLDatabaseType.MYSQL){
			String query;
			query = "INSERT INTO `"+table+"` "
					+ "(`"+keyColumn+"`, `"+setColumn+"`) "
					+ "VALUES ("+keyValueTrans+", -"+setValue+") ";
			if (hasTableUniqueKey) {
				query = query
						+ "ON DUPLICATE KEY UPDATE "
						+ "`"+setColumn+"` = coalesce("+setColumn+" - "+setValue+", "+setValue+") ";
			}
			queryList.add(query);
		} else {
			if (hasTableUniqueKey) {
				String query1, query2;
				query1 = "INSERT OR IGNORE INTO `"+table+"` "
						+ "(`"+keyColumn+"`, "+setColumn+") "
						+ "VALUES ("+keyValueTrans+", 0)";
				query2 = "UPDATE `"+ table+"` "
						+ "SET `"+setColumn+"` = coalesce("+setColumn+" - "+setValue+", "+setValue+") "
						+ "WHERE `"+keyColumn+"` = "+keyValueTrans+" ";
				queryList.add(query1);
				queryList.add(query2);
			} else {
				String query1;
				query1 = "INSERT INTO `"+table+"` "
						+ "(`"+keyColumn+"`, "+setColumn+") "
						+ "VALUES ("+keyValueTrans+", "+setValue+")";
				queryList.add(query1);
			}
		}
		return queryList;
	}


	/**
	 * <p>If databse is not MySQL then REMEMBER to put primary table name on first place</p>
	 */
	public static List<String> constructQueryMultipleValuesSet(String table, List<Pair<String, Object>> pairs, Boolean hasTableUniqueKey, SQLDatabaseType databaseType){
		List<String> queryList = new ArrayList<>();
		String flatColumns = "(";
		String flatValues = "(";
		String flatColumnsAndValues = "";
		Boolean first = true;
		for(Pair pair : pairs){
			String column = (String)pair.getLeft();
			Object value = pair.getRight();
			String valueTrans = value+"";
			if (value == null || (""+value).equalsIgnoreCase("null")){
				valueTrans = "NULL";
			} else if (!(value instanceof Number)) {
				valueTrans = "'" + valueTrans + "'";
			}
			if (first){
				flatColumns = flatColumns+"`"+column+"`";
				flatValues = flatValues + valueTrans;
				flatColumnsAndValues = "`"+column+"`=" + valueTrans;
				first = false;
			} else {
				flatColumns = flatColumns+", `"+column+"`";
				flatValues = flatValues + ", " + valueTrans;
				flatColumnsAndValues = flatColumnsAndValues + ", `"+column+"`=" + valueTrans;
			}
		}
		flatColumns = flatColumns + ")";
		flatValues = flatValues + ")";
		if (databaseType == SQLDatabaseType.MYSQL){
			String query;
			query = "INSERT INTO `"+table+"` "
					+ flatColumns+" "
					+ "VALUES "+flatValues+" ";
			if (hasTableUniqueKey){
				query = query
						+ "ON DUPLICATE KEY UPDATE "
						+ flatColumnsAndValues;
			}
			queryList.add(query);
		} else {
//			query = "INSERT INTO `"+table+"` "
//					+ flatColumns+" "
//					+ "VALUES "+flatValues+" ";
//			if (hasTableUniqueKey){
//				query = query
//						+ "ON CONFLICT (`"+pairs.get(0).getLeft()+"`) "
//						+ "DO UPDATE SET "
//						+ flatColumnsAndValues;
//			}
			String begining = "INSERT";
			if (hasTableUniqueKey) {
				begining = "INSERT OR IGNORE";
			}
			String query1 = begining+" INTO `"+table+"` "
					+ flatColumns
					+ "VALUES "+flatValues;
			if (hasTableUniqueKey) {
				Object keyValue = pairs.get(0).getRight();
				String keyValueTrans = keyValue+"";
				if (keyValue == null || (""+keyValue).equalsIgnoreCase("null")){
					keyValueTrans = "NULL";
				} else if (!(keyValue instanceof Number)) {
					keyValueTrans = "'" + keyValueTrans + "'";
				}
				String query2 = "UPDATE `"+table+"` "
						+ "SET "+flatColumnsAndValues+" "
						+ "WHERE `"+pairs.get(0).getLeft()+"` = "+keyValueTrans+" ";
				queryList.add(query2);
			}
			queryList.add(query1);

		}

		return queryList;
	}

}

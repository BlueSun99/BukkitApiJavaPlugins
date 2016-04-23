package com.bluesun99.simplerpg;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private String url, user, password;
	private Connection conn;
	private String dbname = "simplerpg";
	private String tablename = "playerclass";
	
	public DBManager()
	{
		try {
			Class.forName("org.h2.Driver");
		} catch (Exception e) {
			SimpleRPGMain.lm.format("srt_dbmanager_failed", e.getStackTrace().toString());
			return;
		}
		url = "jdbc:h2:" + "." + File.separator + SimpleRPGMain.plugin.getDataFolder() + File.separator + "SimpleRPG;MODE=MySQL;IGNORECASE=TRUE";
		user = "sa";
		password = "";
		SimpleRPGMain.logger.info(SimpleRPGMain.lm.format("srt_dbmanager_loaded", url));
	}
	
	public Connection getConnection()
	{
		if (conn != null)
			return conn;
		
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void createDefaultStuff()
	{
		if (conn == null)
			return;
		
		try {
			Statement st = conn.createStatement();
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs1 = md.getSchemas();
			ResultSet rs2 = md.getTables(null, "simplerpg", "%", null);
			
			boolean isSchemaExists = false;
			boolean isTableExists = false;
			
			while (rs1.next())
			{
				String s = rs1.getNString(1);
				//SimpleRPGMain.logger.info(s);
				if (s.equals(dbname))
					isSchemaExists = true;
			}
			
			if (!isSchemaExists)
				st.executeUpdate("CREATE SCHEMA " + dbname + ";");
			
			st.executeUpdate("USE " + dbname + ";");
			
			while (rs2.next())
			{
				String s = rs2.getNString(3);
				//SimpleRPGMain.logger.info(s);
				if (s.equals(tablename))
					isTableExists = true;
			}
			
			if (!isTableExists)
				st.executeUpdate(
					"CREATE TABLE " + tablename + " (" +
					"UUID varchar(36) not null," +
					"class int not null" + 
					");");

			rs1.close();
			rs2.close();
			st.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try {
			conn.close();
		} catch (Exception e) {}
	}
}

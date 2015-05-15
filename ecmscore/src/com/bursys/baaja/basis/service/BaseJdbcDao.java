package com.bursys.baaja.basis.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bursys.baaja.basis.constants.MiscConstants;
import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.baaja.basis.utils.BaajaConfig;

/**
********************

 @author JosephBrian

 * 
 * This class provides JDBC data access capabilities and any JDBC DAO's should
 * extend from this class to get some basic behavior such as getting a
 * connection from pool and so forth.
 */
	
	
public class BaseJdbcDao {
			
	private final static Log log = LogFactory.getLog(BaseJdbcDao.class);

	/**
	 * This will get a connection from the default data source defined in the
	 * application.properties file
	 * 
	 * @return SQL Connection from data connection pool from app server
	 * @throws BaajaException -
	 *             exception
	 */
	protected Connection getDbConnection() throws BaajaException {
		String dsName = BaajaConfig.getInstance().getProperty(
				MiscConstants.DATA_SOURCE_NAME);
		return getDbConnection(dsName);
	}
	
	public static Connection getDbConnectionStatic() throws BaajaException {
		String dsName = BaajaConfig.getInstance().getProperty(
				MiscConstants.DATA_SOURCE_NAME);
		return getDbConnection(0, dsName);		
	}

	/**
	 * This will get a connection using the data source provided
	 * 
	 * @param dsName
	 *            data source name to use to get the connection
	 * @return SQL COnnection from the connection pool
	 * @throws BaajaException
	 */
	protected Connection getDbConnection(String dsName) throws BaajaException {
		return getDbConnection(0, dsName);
	}

	/**
	 * This method will get a sql connection from the database properties
	 * defined in the baaja properties file if you have multiple data base
	 * properties defined, then here you will have to pass in the number of that
	 * database (database.num=1) so you will have to pass in 1 and then this
	 * method will read all props from property file
	 * 
	 * @param dbNum
	 *            database configuration number to pick up
	 * @return valid sql connection
	 * @throws BaajaException
	 */
	protected Connection getDbConnection(int dbNum) throws BaajaException {
		return getDbConnection(dbNum, null);
	}

	/**
	 * This method will return the connection either from the data source name
	 * or based on the configuration number from the props file
	 * (database.num=1). One of the parameter has to be provided.
	 * 
	 * @param dbNum
	 *            number of the database for which connection needs to be
	 *            generated
	 * @param dsName
	 *            data source name
	 * @return returns a connection object
	 * @throws BaajaException
	 */
	protected static Connection getDbConnection(int dbNum, String dsName)
			throws BaajaException {
		Connection conn = null;
		// if datasource is not null, then use the connection pool to obtain a
		// new connection
		if (dsName != null) {
			try {
				Context ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup(dsName);
				conn = ds.getConnection();
			} catch (NamingException e) {
				log
						.error(
								"Naming exception occured when looking up for a datasource provided",
								e);
				throw new BaajaException(
						"Naming exception occured when looking up for a datasource provided"
								+ e.getMessage());
			} catch (SQLException e) {
				log
						.error(
								"Naming exception occured when looking up for a datasource provided",
								e);
				throw new BaajaException(
						"Naming exception occured when looking up for a datasource provided"
								+ e.getMessage());
			}
			return conn;
		} else {
			return null;
		}
	}
	
	/**
	 * This method will execute the query sent as a parameter
	 * @param query	The query to be executed.
	 */
	public void executeQuery(String query) throws BaajaException{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getDbConnection();
			ps = con.prepareStatement(query);
			ps.execute();
		} catch (SQLException e) {
			log.error("Error executing SQL query", e);
			throw new BaajaException("Query execution problem" + e.getMessage());
		} finally{
			sqlCleanUp(con, ps, null);
		}
	}
	/**
	 * Close Connection statment and Result Set
	 * 
	 * @param con
	 * @param stmt
	 * @param rs
	 */
	public static void sqlCleanUp(Connection con, Statement stmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			log.debug("An error is occured in processing of cleaning objects RS");
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			log.debug("An error is occured in processing of cleaning objects STMT");
		}
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			log.debug("An error is occured in processing of cleaning objects CONN");
		}
	}

	/**
	 * Close Connection 
	 * 
	 * @param con
	 */
	public static void closeConnection(Connection con) {
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			log.debug("An error is occured in processing of cleaning objects CONN");
		}
	}
	
	public void closeConnections( Connection conn)
	{
		try
		{
			if(conn != null)
			{
				conn.close();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}

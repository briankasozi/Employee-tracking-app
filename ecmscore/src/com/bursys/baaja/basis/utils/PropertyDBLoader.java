package com.bursys.baaja.basis.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bursys.baaja.basis.exception.BaajaException;

/**
 * 
 */
public class PropertyDBLoader {
    private static Log log = LogFactory.getLog(PropertyDBLoader.class);

    public synchronized Properties getProperties(String jndiName, String sql, Properties props) throws BaajaException{
        log.debug("Loading the properties from the jndiName :: " + jndiName + "::" + sql);
        DataSource dataSource;
        try   {
            Context ic = new InitialContext();
            dataSource = (DataSource) ic.lookup(jndiName);
        }catch(Exception e)  {
           throw new BaajaException(e);
        }
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try   {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())  {
                props.put(rs.getString("KEY_NAME"), rs.getString("KEY_VALUE"));
            }
        }catch(SQLException e)  {
           throw new BaajaException(e);
        }finally {
           sqlCleanUp(conn, st, rs);
        }
        return props;
    }

    private void sqlCleanUp(Connection con, Statement stmt, ResultSet rs)  {
      try   {
         if(rs != null) rs.close();
         if(stmt != null) stmt.close();
         if(con != null) con.close();
      }catch(SQLException ex) {
         ex.printStackTrace();
      }
   }


}

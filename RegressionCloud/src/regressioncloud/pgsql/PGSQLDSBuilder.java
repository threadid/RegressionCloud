package com.regressioncloud.pgsql;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;

public class PGSQLDSBuilder {

	public PGSQLDSBuilder() {
	}
	
	public DataSource getDS() {
			PoolProperties PP = new PoolProperties();
			Context initContext = null;
			DataSource DS = null;

		try {
/*****************************************************************************************
*  The following settings are all defined in context.xml
*  WebContent/META-INF/context.xml
* <?xml version="1.0" encoding="UTF-8"?>
* <Context path="/RegressionCloud" docBase="RegressionCloud" crossContext="true" reloadable="true" debug="1">
*	<Resource name="jdbc/postgres" auth="Container"
*		type="javax.sql.DataSource"
*		factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
*		driverClassName="org.postgresql.Driver"
*		url="jdbc:postgresql://regressioncloud.com:5432/RegressionCloud"
*		username="username"
*		password="password"
*		/>
* </Context>
*****************************************************************************************/
//			PP.setName("jdbc/postgres");
//			PP.setUrl("jdbc:postgresql://instancename.xxxxxxxxxxxx.us-east-1.rds.amazonaws.com:5432/instancename");
//			PP.setDriverClassName("org.postgresql.Driver");
//			PP.setUsername("username");
//			PP.setPassword("username");
			PP.setFairQueue(false);
			PP.setJmxEnabled(true);
			PP.setTestOnBorrow(true);
			PP.setTestOnReturn(false);
			PP.setTestWhileIdle(false);
			PP.setValidationQuery("SELECT 1");
			PP.setValidationInterval(1800000);
			PP.setTimeBetweenEvictionRunsMillis(30000);
			PP.setMaxActive(100);
			PP.setInitialSize(10);
			PP.setInitSQL("SELECT 1");
			PP.setMaxWait(6000);
			PP.setRemoveAbandonedTimeout(60);
			PP.setMinEvictableIdleTimeMillis(30000);
			PP.setMinIdle(10);
			PP.setMaxIdle(100);
			PP.setLogAbandoned(true);
			PP.setRemoveAbandoned(true);
			PP.setDefaultAutoCommit(true);
			PP.setDefaultReadOnly(false);
			PP.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			PP.setUseEquals(false);
			PP.setJdbcInterceptors(
	            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
	            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		} catch (Exception e) {
			System.out.println("Error in DBCP resource definition");
			e.printStackTrace();
	    	return null;
		}
		try {
			initContext = new InitialContext();
			DS = (DataSource)initContext.lookup("java:/comp/env/jdbc/postgres");
			DS.setPoolProperties(PP);
		} catch (NamingException e) {
			System.out.println("Error in DBCP resource definition");
			e.printStackTrace();
	    	return null;
		}
		return DS;
	}
    
}

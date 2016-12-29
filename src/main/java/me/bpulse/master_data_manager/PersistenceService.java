package me.bpulse.master_data_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PersistenceService {

	@Value("${dbdriver}")
	private String dbdriver;

	@Value("${dbconnection}")
	private String dbconnection;

	@Value("${dbuser}")
	private String dbuser;

	@Value("${dbpassword}")
	private String dbpassword;
	
	@Value("${bp.query}")
	private String query;
	
	public List<String[]> getAllResults() {
		List<String[]> results = new ArrayList<String[]>();
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
			String[] rowNames = new String[columnsNumber];
			for (int i=0;i<columnsNumber;i++) {
				rowNames[i] = rsmd.getColumnName(i+1);
			}
			results.add(rowNames);			
			
			while (rs.next()) {
				String[] row = new String[columnsNumber];
				for (int i=0;i<columnsNumber;i++) {
					row[i] = rs.getString(i+1);
				}
				results.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	private Connection getConnection() {
		try {
			Class.forName(dbdriver);
			return DriverManager.getConnection(dbconnection,dbuser,dbpassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

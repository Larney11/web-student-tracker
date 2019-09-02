package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


public class StudentDBUtil {
	
	private DataSource dataSource;
	
	public StudentDBUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public List<Student> getStudents() throws Exception {
		
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			// Get a connection
			myConn = dataSource.getConnection();
			
			// Create SQL statement
			String sql = "SELECT * FROM student ORDER BY last_name";
			myStmt = myConn.createStatement();
			
			// Execute query
			myRs = myStmt.executeQuery(sql);
			
			// Process result set
			while(myRs.next()) {
				
				// Retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//Create new Student object
				Student tempStudent = new Student(id, firstName, lastName, email);
				
				// Add to list of students
				students.add(tempStudent);
			}	
			
		}
		finally {
			// Close JDBC objects
			close(myConn, myStmt, myRs);
		}
		
		return students;
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
			
		try {
			if(myRs != null) {
				myRs.close();
			}
			
			if(myStmt != null) {
				myStmt.close();
			}
			
			if(myConn != null) {
				myConn.close();
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

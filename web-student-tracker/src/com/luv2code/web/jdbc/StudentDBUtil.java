package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	

	public void addStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// Get DB connection
			myConn = dataSource.getConnection();
			
			// Create a SQL for INSERT
			String sql = "insert into student "
					+ "(first_name, last_name, email) "
					+ "values (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			//Set parameter values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			// Execute SQL insert
			myStmt.execute();
			
		}
		finally {
			// Clean up JDBC object
			close(myConn, myStmt, null);
			
		}
	}

	
	public Student getStudent(String theStudentId) throws Exception {
		
		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			// Convert Id to int
			studentId = Integer.parseInt(theStudentId);
			
			// Get DB connection
			myConn = dataSource.getConnection();
			
			// Create a SQL to get Student
			String sql = "select * from student Where id=?";
			
			// Create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//Set parameter
			myStmt.setInt(1, studentId);
			
			// Execute SQL insert
			myRs = myStmt.executeQuery();
			
			/// Retrieve data from result set
			if(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// Use the studentId during construction
				theStudent= new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student ID: " + studentId);
			}
		}
		finally {
			// Clean up JDBC object
			close(myConn, myStmt, myRs);	
		}
		
		return theStudent;
	}
	
	
	public void updateStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();
			
			// create SQL update statement
			String sql = "update student "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute SQL statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}
}

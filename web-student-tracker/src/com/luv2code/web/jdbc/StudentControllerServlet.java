package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDBUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		
		super.init();
		
		// Create our student db util. .  and pass the connection pool/dataSource
		
			studentDbUtil = new StudentDBUtil(dataSource);
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			// Read the "command" parameter
			String theCommand = request.getParameter("command");
			
			if(theCommand == null) {
				theCommand = "LIST";
			}
			
			// Route to the appropriate method
			switch(theCommand) {
				
				case "LIST":
					listStudents(request, response);
					break;
					
				case "ADD":
					addStudent(request, response);
					break;
					
				default:
					listStudents(request, response);
					break;
			}
		}
		catch(Exception ex) {
			throw new ServletException(ex);
		}
	}
	
	
	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// Create a new student object
		Student theStudent = new Student(firstName, lastName, email);
		
		// Add the student to the database
		studentDbUtil.addStudent(theStudent);
		
		// Send back to the main page (the student list)
		listStudents(request, response);
	}


	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Get students from db utill
		List<Student> students = studentDbUtil.getStudents();
			
		// Add student to request
		request.setAttribute("STUDENT_LIST", students);
			
		// Send to JSP page(View)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}
	
}

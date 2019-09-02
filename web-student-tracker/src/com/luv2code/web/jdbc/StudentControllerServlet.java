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
			// list the students . . . in MVC fashion
			listStudents(request, response);
		}
		catch(Exception ex) {
			throw new ServletException(ex);
		}
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

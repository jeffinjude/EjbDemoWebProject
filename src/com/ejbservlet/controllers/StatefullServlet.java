package com.ejbservlet.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ejbdemo.sampleBeans.ListElementsRemote;

/**
 * Servlet implementation class StatefullServlet
 */
@WebServlet(description = "Servlet that implements statefull bean", urlPatterns = { "/StatefullServlet" })
public class StatefullServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ListElementsRemote listElements = (ListElementsRemote) session.getAttribute("listElementsSession");
		if (listElements == null) {
			InitialContext ic;
			try {
				ic = new InitialContext();
				listElements = (ListElementsRemote)ic.lookup("java:global/EjbDemoEnterpriseProject/EjbDemoEjbProject/ListElements");
			    session.setAttribute("listElementsSession", listElements);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		
		try
		{
			if(request.getParameter("addNum") != null)
			{
				int e = Integer.parseInt(request.getParameter("element"));
				listElements.addElement(e);
			}
			if(request.getParameter("removeNum") != null)
			{
				int e = Integer.parseInt(request.getParameter("element"));
				listElements.removeElement(e);
			}
			
			request.setAttribute("listReqParam", listElements.getElements());
			RequestDispatcher view = request.getRequestDispatcher("ListElements.jsp");
			view.forward(request, response);
		}
		catch(Exception e)
		{
			PrintWriter writer = response.getWriter();
			writer.println("Please enter a valid integer element!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

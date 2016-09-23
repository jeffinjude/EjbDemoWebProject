package com.ejbservlet.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ejbdemo.services.SampleBizRules;

/**
 * Servlet implementation class EjaJpaServlet
 */
@WebServlet(urlPatterns = { "/EjbJpaServlet"})
public class EjbJpaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//@EJB
	SampleBizRules sampleBizRules;
	@Resource(mappedName="java:/jmsDemo/sampleQueue")
	private Queue sampleQueue;
	@Resource(mappedName="java:/jmsDemo/ConnectionFactory")
	private ConnectionFactory sampleConnectionFactory;
    
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    try {
			InitialContext ctx = new InitialContext();
			sampleBizRules = (SampleBizRules) ctx.lookup("java:app/EjbDemoEjbProject/SampleBizRules");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	  }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.println("Servet the get request!");
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Apply business rules
		String phoneNumber = sampleBizRules.applyPhoneNumberBizRules(request.getParameter("phoneNumber"));
		
		//Prepare the data as json string and send to queue
		sendJmsMessageToSampleQueue("{\"userName\" : \"" + request.getParameter("userName") + "\",\"phoneNumber\" : \"" + phoneNumber + "\"}");
		
		PrintWriter writer = response.getWriter();
		writer.println("Data successfully inserted!");
	}
	
	public void sendJmsMessageToSampleQueue(String message)
	{
		System.out.println("Sending message..");
		try
		{
			Connection con = sampleConnectionFactory.createConnection();
			Session s = con.createSession();
			MessageProducer mp = s.createProducer(sampleQueue);
			TextMessage tm = s.createTextMessage();
			tm.setText(message);
			mp.send(tm);
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List" %>
<%
	List<Integer> listReqParam = (List<Integer>)request.getAttribute("listReqParam");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Statefull Session</title>
</head>
<body>
	<h1>Enter Integer Elements</h1>
	<form name="listElementsForm" method="post" action="StatefullServlet">
		<input type="text" name="element" id="element"><br>
		<input type="submit" value="Add" name="addNum"><br>
		<input type="submit" value="Remove" name="removeNum"><br>
		<%
			if(listReqParam != null)
			{
				for(int value : listReqParam)
				{
					out.println("<br>" + value);
				}
				out.println("<br> List Size : " + listReqParam.size());
			}
		%>
	</form>
	
</body>
</html>
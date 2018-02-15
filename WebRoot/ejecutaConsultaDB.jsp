<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="com.sicap.clientes.dao.DAOMaster"%>
<%@ page import="java.sql.ResultSetMetaData"%>
<%@ page import="java.sql.SQLException"%>
<%
Connection cn = null;
ResultSet rs = null;
ResultSetMetaData rsmd = null;
try{
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

if ( request.getParameter("query")!=null && !request.getParameter("query").trim().equals("") ){
	cn = new DAOMaster().getConnection();
	Statement st = cn.createStatement();
	rs = st.executeQuery(request.getParameter("query"));
	//rs = new DAOMaster().ejecutaSelect(request.getParameter("query"));
}

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Consultas</title>
    <script>
		function ejecutaConsulta(){
			window.document.forma.command.value='ejecutaConsulta';
			window.document.forma.submit();
		}
	</script>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="./css/BEtext.css" rel="stylesheet" type="text/css">

  </head>
  
<body leftmargin="0" topmargin="0">
<center>
    <form name="forma" action="admin" method="post">
    	<input type="hidden" name="command" value="">
    	<textarea rows="4" cols="60" name="query"></textarea><br><br>
    	<input type="button" value="Enviar" onclick="ejecutaConsulta()">
    </form>
</center>
<%
if ( rs!=null ){
	response.setContentType("text/plain");
	response.setHeader("Content-Disposition","attachment; filename=\"resultado.csv\"");
	response.setHeader("cache-control", "no-cache");
	out.clear();
	rsmd = rs.getMetaData();
	
	int numCols = rsmd.getColumnCount ();

	// Display column headings
	for (int i=1; i<=numCols; i++) {
		if (i > 1) out.print(",");
		out.print(rsmd.getColumnLabel(i));
	}
	out.println("");
	// Display data, fetching until end of the result set

	boolean more = rs.next ();
	while (more) {
		// Loop through each column, getting the
		// column data and displaying
		for (int i=1; i<=numCols; i++) {
			if (i > 1) out.print(",");
			out.print(rs.getString(i));
		}
		out.println("");

		// Fetch the next result set row
		more = rs.next ();
	}
	rs.close();
	cn.close();
}else{%>
  </body>
</html>
<%}
}catch(SQLException sqle) {
	out.print(sqle.getMessage());
}
catch(Exception e) {
	out.print(e.getMessage());
}

%>
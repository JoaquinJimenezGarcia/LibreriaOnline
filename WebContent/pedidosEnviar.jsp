<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<title>Marcar Pedido Como Enviado</title>
</head>
<body>
	<div id="main" class="container">
		<h1>Marcar Pedido Como Enviado</h1>
		<form method="get" action="enviado">
			<div class="form-group">
		    		<label for="marcar">Pedido a Marcar Como Enviado...</label>
			    <select class="form-control" id="marcar" name="marcar">
			    		<%@ page import = "java.sql.*" %>
			    		
			    		<%
			    		
						Class.forName("com.mysql.jdbc.Driver");
						
		    				String userName = "USUARIO";
						String password = "CLAVE";
						String url = "jdbc:mysql://IP_DB:PUERTO/NOMBRE_DB";
						
						Connection conn = DriverManager.getConnection(url, userName, password);
						Statement stmt = conn.createStatement();
						
						String sqlStr = "SELECT Pedido.numeroPedido, Pedido.idUsuario, Pedido.idLibro, Libro.idLibro, Libro.TituloLibro, Pedido.Cantidad, Usuario.idUsuario, Usuario.NombreUsuario "
							+ "FROM Pedido, Usuario, Libro "
							+ "WHERE Usuario.idUsuario = Pedido.idUsuario "
							+ "AND Pedido.idLibro = Libro.idLibro "
							+ "AND Pedido.enviado = 0";
						
						ResultSet rs = stmt.executeQuery(sqlStr);
						
						String numeroAnterior = "0";
						
						while(rs.next()) {
							String numeroActual = rs.getString("Pedido.numeroPedido");
							
							if(!numeroActual.equals(numeroAnterior)) {
							
					%>
			        <option>Pedido de <%=rs.getString("Usuario.NombreUsuario")%> --- nº <%=rs.getString("Pedido.numeroPedido")%></option>
			       
			        <%
							} 
							
			        			numeroAnterior = numeroActual;
						}
						
						rs.close();
						stmt.close();
						conn.close();
					%>
			    </select>
		  	</div>
		  	<button type="submit" name="enviado" class="btn btn-primary">Marcar Como Enviado</button>
		</form>
	</div>
</body>
</html>
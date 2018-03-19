package es.joaquinjimenezgarcia;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sun.istack.internal.logging.Logger;

/**
 * Servlet implementation class MostrarPedidosServlet
 */
@WebServlet("/MostrarPedidosServlet")
public class MostrarPedidosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MostrarPedidosServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException {
		try {
			InitialContext ctx = new InitialContext();
			pool = (DataSource) ctx.lookup("java:comp/env/jdbc/LibreriaOnline");
			
			if (pool == null) {
				throw new ServletException("DataSource desconocida'mysql_tiendalibros'");
			}
		} catch (NamingException ex) {
			Logger.getLogger(LoginServlet.class.getName(), null).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = pool.getConnection();
			stmt = conn.createStatement();
			
			out.println("<html>");
			out.println("<head>");
			out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">");
			out.println("<title>Pedidos</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div id=\"main\" class=\"container\">");
			out.println("<h2>¿Qué desea hacer?</h2>");
		
			String usuario;
			HttpSession session = request.getSession(false);
		
			if (session == null) {
				out.println("<h3>No has iniciado sesión</h3>");
			} else {
				synchronized (session) {
					usuario = (String) session.getAttribute("usuario");
				}
				
				out.println("<table>");
				out.println("<tr>");
				out.println("<td>Usuario:</td>");
				out.println("<td>" + usuario + "</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("<p><a href='logout'>Salir</a></p>");
				
				try {
					stmt = (Statement) conn.createStatement();
					String sqlStr = "SELECT Pedido.idPedido, Pedido.numeroPedido, Pedido.idUsuario, Pedido.idLibro, Libro.idLibro, Libro.TituloLibro, Pedido.Cantidad, Usuario.idUsuario, Usuario.NombreUsuario "
							+ "FROM Pedido, Usuario, Libro "
							+ "WHERE Usuario.idUsuario = Pedido.idUsuario "
							+ "AND Pedido.idLibro = Libro.idLibro "
							+ "AND Pedido.enviado = 0 "
							+ "ORDER BY Pedido.idPedido";
					
					out.println("<html>");
					out.println("<head><title>Pedidos</title></head>");
					out.println("<body>");
					out.println("<div id=\"main\" class=\"container\">");
					
					ResultSet rs = stmt.executeQuery(sqlStr);
					int count = 0;
					
					out.println("<a href=\"pedidos.html\">Volver</a>");
					out.println("<table class=\"table\">");
					out.println("<thead class=\"thead-dark\">");
					out.println("<tr>");
					out.println("<th>Cliente</th>");
					out.println("<th>Libro</th>");
					out.println("<th>Cantidad</th>");
					out.println("<th>Nº Pedido</th>");
					out.println("</tr>");
					out.println("</thead>");
					
					String numeroAnterior = "0";
					
					while (rs.next()) {
						String numeroActual = rs.getString("idUsuario");
						
						out.println("<tr>");
						
						if(!numeroActual.equals(numeroAnterior)) {
							out.println("<td>" + rs.getString("Usuario.NombreUsuario") + "</td>");
							count++;
						} else {
							out.println("<td></td>");
						}
						
						out.println("<td>" + rs.getString("Libro.TituloLibro") + "</td>");
						out.println("<td>" + rs.getString("Cantidad") + "</td>");
						
						if(!numeroActual.equals(numeroAnterior)) {
							out.println("<td>" + rs.getString("Pedido.numeroPedido") + "</td>" + "</tr>");
						} else {
							out.println("<td></td>" + "</tr>");
						}
						
						numeroAnterior = numeroActual;
					}
					
					out.println("</table>");
					out.println("<p>" + count + " pedidos encontrados.</p>");
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.print(ex);
				}
			}
			
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
		}catch (SQLException ex) {
			out.println("<p>Servicio no disponible</p>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			
			Logger.getLogger(LoginServlet.class.getName(), null).log(Level.SEVERE, null, ex); 
		} finally {
			out.close();
			
			try {
				if (stmt != null) {
					stmt.close();
				}
				
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

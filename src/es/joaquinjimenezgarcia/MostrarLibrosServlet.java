package es.joaquinjimenezgarcia;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.Statement;

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

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;
import com.sun.istack.internal.logging.Logger;

/**
 * Servlet implementation class MostrarLibrosServlet
 */
@WebServlet("/MostrarLibrosServlet")
public class MostrarLibrosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MostrarLibrosServlet() {
        super();
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
			out.println("<title>Libros</title>");
			out.println("</head>");
			out.println("<body>");
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
					String sqlStr = "SELECT TituloLibro, PrecioLibro, CantidadLibro FROM Libro";
					
					out.println("<html>");
					out.println("<head><title>Libros</title></head>");
					out.println("<body>");
					
					ResultSet rs = stmt.executeQuery(sqlStr);
					int count = 0;
					
					out.println("<a href=\"libros.html\">Volver</a>");
					out.println("<table border=\"1\">");
					out.println("<tr>");
					out.println("<th>Título</th>");
					out.println("<th>Precio</th>");
					out.println("<th>Cantidad</th>");
					out.println("</tr>");
					
					while (rs.next()) {
						out.println("<tr>" + "<td>" + rs.getString("TituloLibro") + "</td>");
						out.println("<td>" + rs.getString("PrecioLibro") + "€" + "</td>");
						out.println("<td>" + rs.getString("CantidadLibro") + "</td>" + "</tr>");
						
						count++;
					}
					
					out.println("</table>");
					out.println("<p>" + count + " libros encontrados.</p>");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			out.println("</body>");
			out.println("</html>");
		}catch (SQLException ex) {
			out.println("<p>Servicio no disponible</p>");
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

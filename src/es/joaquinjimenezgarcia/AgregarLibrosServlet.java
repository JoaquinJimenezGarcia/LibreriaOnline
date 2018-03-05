package es.joaquinjimenezgarcia;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * Servlet implementation class AgregarLibrosServlet
 */
@WebServlet("/AgregarLibrosServlet")
public class AgregarLibrosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AgregarLibrosServlet() {
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
	 * 
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Login</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>Panel Administración</h2>");
			
			conn = pool.getConnection();
			stmt = conn.createStatement();
			
			String titulo = request.getParameter("titulo");
			String precio = request.getParameter("precio");
			String cantidad = request.getParameter("cantidad");
			String idAutor = request.getParameter("idAutor");
			String idEditorial = request.getParameter("idEditorial");
			
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("INSERT INTO Libro VALUES(null, '")
				.append(titulo).append("','")
				.append(precio).append("','")
				.append(cantidad).append("','")
				.append(idAutor).append("','")
				.append(idEditorial).append("')");
		
			if (stmt.executeUpdate(sqlStr.toString())==-1) {
				out.println("<h3>Error insertando datos en la Base de Datos</h3>");
				out.println("<p><a href='libros.html'>Volver</a></p>");
			} else {
				out.println("<p>Añadido con éxito</p>");
				out.println("<a href=\"libros.html\">Volver</a>");
			}

			out.println("</body>");
			out.println("</html>");
		} catch (SQLException ex) {
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
			} catch (SQLException ex) {
				Logger.getLogger(LoginServlet.class.getName(), null).log(Level.SEVERE, null, ex);
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

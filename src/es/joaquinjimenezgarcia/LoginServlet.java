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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import com.sun.istack.internal.logging.Logger;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
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
			
			String usuario = request.getParameter("usuario");
			String password = request.getParameter("password");
			
			if (usuario.length() == 0) {
				out.println("<h3>Debes introducir tu usuario</h3>");
			} else if (password.length() == 0) {
				out.println("<h3>Debes introducir tu contraseña</h3>");
			} else {
				StringBuilder sqlStr = new StringBuilder();
				sqlStr.append("SELECT * FROM Usuario WHERE ");
				sqlStr.append("STRCMP(Usuario.NombreUsuario, '").append(usuario).append("') = 0");
				sqlStr.append(" AND STRCMP(Usuario.ClaveUsuario, PASSWORD('").append(password).append("')) = 0");
				
				ResultSet rset = stmt.executeQuery(sqlStr.toString());
				
				if (!rset.next()) {
					out.println("<h3>Nombre de usuario o contraseña incorrectos</h3>");
					out.println("<p><a href='index.html'>Volver a Login</a></p>");
				} else {
					HttpSession session = request.getSession(false);
				
					if (session != null) {
						session.invalidate();
					}
					
					session = request.getSession(true);
					
					synchronized (session) {
						session.setAttribute("usuario", usuario);
					}
					
					out.println("<p>Hola, " + usuario + "!</p>");
					out.println("<p><a href='libros.html'>Libros</a></p>");
					out.println("<p><a href='autores.html'>Autores</a></p>");
					out.println("<p><a href='editoriales.html'>Editoriales</a></p>");
					out.println("<p><a href='pedidos.html'>Pedidos</a></p>");
				}
			}
			
			out.println("</body>");
			out.println("</html>");
		} catch (SQLException ex) {
			out.println("<p>Servicio no disponible</p>");
			out.println(ex);
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
}
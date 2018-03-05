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
			// Crea un contecto para poder luego buscar el recurso DataSource
			InitialContext ctx = new InitialContext();
			// Busca el recurso DataSource en el contexto
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
			// Recuperar el nombre de usuario
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
					// Paso 4: Ejecutar las sentencias
					String sqlStr = "SELECT TituloLibro, PrecioLibro, CantidadLibro FROM Libro";
					// Generar una pÃ¡gina HTML como resultado de la consulta
					out.println("<html>");
					out.println("<head><title>Libros</title></head>");
					out.println("<body>");
					ResultSet rs = stmt.executeQuery(sqlStr);
					// Paso 5: Recoger los resultados y procesarlos
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
					out.println("<h3>" + ex + "</h3>");
				}
			}
			out.println("</body>");
			out.println("</html>");
		}catch (SQLException ex) {
			out.println("<p>Servicio no disponible</p>");
			out.println(ex);
			out.println("</body>");
			out.println("</html>");
			Logger.getLogger(LoginServlet.class.getName(), null).log(Level.SEVERE, null, ex); 
		} finally {
			// Cerramos objetos
			out.close();
			
			try {
				// Cerramos el resto de recursos
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

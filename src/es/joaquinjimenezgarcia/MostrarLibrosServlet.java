package es.joaquinjimenezgarcia;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
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
				out.println("<h3>No has iniciado sesiÃ³n</h3>");
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
					out.println("<head><title>Resultado</title></head>");
					out.println("<body>");
					out.println("<h3>Su consulta</h3>");
					out.println("<p>Tu consulta es: " + sqlStr + "</p>");
					ResultSet rs = stmt.executeQuery(sqlStr);
					// Paso 5: Recoger los resultados y procesarlos
					int count = 0;
					while (rs.next()) {
						out.println("<p>" + rs.getString("TituloLibro") + ",");
						out.println(rs.getString("PrecioLibro") + ",");
						out.println(rs.getString("CantidadLibro") + "</p>");
						count++;
					}
					out.println("<p>" + count + " registros encontrados.</p>");
					out.println("<a href=\"ConsultaLibros.html\">Volver</a>");
				} catch (Exception ex) {
					ex.printStackTrace();
					out.println("<h3>" + ex + "</h3>");
				}
			}
			out.println("</body>");
			out.println("</html>");
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

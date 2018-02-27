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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		// Creamos un objeto para poder escribir la respuesta
		PrintWriter out = response.getWriter();
		// Creamos objetos para la conexión
		Connection conn = null;
		Statement stmt = null;
		
		try {
			stmt = (Statement) conn.createStatement();
			// Paso 4: Ejecutar las sentencias
			String sqlStr = "SELECT * FROM Libros";
			// Generar una página HTML como resultado de la consulta
			out.println("<html>");
			out.println("<head><title>Resultado</title></head>");
			out.println("<body>");
			out.println("<h3>Su consulta</h3>");
			out.println("<p>Tu consulta es: " + sqlStr + "</p>");
			ResultSet rs = stmt.executeQuery(sqlStr);
			// Paso 5: Recoger los resultados y procesarlos
			int count = 0;
			while (rs.next()) {
				out.println("<p>" + rs.getString("autorLibro") + ",");
				out.println(rs.getString("tituloLibro") + ",");
				out.println(rs.getString("precioLibro") + ",");
				out.println(rs.getString("cantidadLibro") + "</p>");
				count++;
			}
			out.println("<p>" + count + " registros encontrados.</p>");
			out.println("<a href=\"ConsultaLibros.html\">Volver</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			// Cerramos el flujo de escritura
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

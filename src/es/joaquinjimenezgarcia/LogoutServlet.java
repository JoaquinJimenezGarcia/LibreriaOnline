package es.joaquinjimenezgarcia;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
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
		try {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Logout</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>Logout</h2>");
			HttpSession session = request.getSession(false);
			if (session == null) {
				out.println("<h3>No has iniciado sesión</h3>");
			} else {
				session.invalidate();
				out.println("<p>Adiós</p>");
				out.println("<p><a href='index.html'>Login</a></p>");
			}
			out.println("</body>");
			out.println("</html>");
		} finally {
			// Cerramos objetos
			out.close();
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

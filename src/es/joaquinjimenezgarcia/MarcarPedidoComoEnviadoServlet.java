package es.joaquinjimenezgarcia;

import java.io.IOException;
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

import com.sun.istack.internal.logging.Logger;

/**
 * Servlet implementation class MarcarPedidoComoEnviadoServlet
 */
@WebServlet("/MarcarPedidoComoEnviadoServlet")
public class MarcarPedidoComoEnviadoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource pool;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MarcarPedidoComoEnviadoServlet() {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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

package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class IntroServlet
 */
@WebServlet(name = "introServlet", urlPatterns = {"/" + IntroServlet.INTRO})
public class IntroServlet extends HttpServlet {
    public static final String INTRO = "intro";
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntroServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute(LoginServlet.USERNAME) == null) {
            // user is not logged
            request.getRequestDispatcher("/WEB-INF/jsp/view/intro_non_logged.jsp").forward(request,
                    response);
        } else {
            // user is logged
            response.sendRedirect(TasksServlet.SHOW);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}

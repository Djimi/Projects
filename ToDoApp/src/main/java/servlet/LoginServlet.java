package servlet;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import database.DBConnection;
import model.User;
import org.bson.Document;
import security.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet implementation class LoginServlet
 */

@WebServlet(name = "loginServlet", urlPatterns = {"/" + LoginServlet.LOGIN,
        "/" + LoginServlet.REGISTER, "/" + LoginServlet.LOGOUT})
public class LoginServlet extends HttpServlet {
    public static final String TASKS = "tasks";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String SUCCESSFUL_REGISTER = "successful_register";
    public static final String SUCCESSFUL_LOGIN = "successful_login";
    public static final String REGISTER = "register";
    public static final String LOGOUT = "logout";
    public static final String LOGIN = "login";
    public static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contextPath = request.getServletPath();

        if (request.getSession().getAttribute(USERNAME) != null) {
            response.sendRedirect(TasksServlet.SHOW);
            return;
        }

        if (contextPath.equals("/" + LOGIN)) {
            request.getRequestDispatcher("/WEB-INF/jsp/view/login.jsp").forward(request, response);
            return;
        } else if (contextPath.equals("/" + REGISTER)) {
            request.getRequestDispatcher("/WEB-INF/jsp/view/register.jsp").forward(request,
                    response);
            return;
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestedURI = request.getServletPath();

        if (requestedURI.equals("/" + LOGOUT)) {
            request.getSession().invalidate();
            response.sendRedirect(IntroServlet.INTRO);
            return;
        }

        if (request.getSession().getAttribute(USERNAME) != null) {
            response.sendRedirect(TasksServlet.SHOW);
            return;
        }

        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        if (isEmpty(username) || isEmpty(password)) {
            if (requestedURI.equals("/" + LOGIN)) {
                showUnsuccessfulLogin(request, response);
                return;
            } else if (requestedURI.equals("/" + REGISTER)) {
                showUnsuccessfulRegister(request, response);
            }
        } else {

            MongoCollection<Document> usersCollection = DBConnection.getUsersCollection();
            FindIterable<Document> founded = usersCollection
                    .find(new Document(DBConnection.USERNAME, username));
            Document userInfo = founded.first();

            if (requestedURI.equals("/" + LOGIN)) {

                User user = DBConnection.parseUser(userInfo);

                if (userInfo != null) {
                    boolean isPasswordCorrect = BCrypt.checkpw(password, user.getPassword());

                    if (isPasswordCorrect) {
                        request.getSession().setAttribute(USERNAME, username);
                        request.changeSessionId(); // protect us from session
                        // fixation attack
                        response.sendRedirect(TasksServlet.SHOW);
                    } else {
                        showUnsuccessfulLogin(request, response);
                    }
                } else {
                    showUnsuccessfulLogin(request, response);
                }

            } else if (requestedURI.equals("/" + REGISTER)) {
                if (isEmpty(username) || isEmpty(password) || userInfo != null) {
                    // userInfo will be not null when we have user with the same
                    // username!
                    showUnsuccessfulRegister(request, response);
                } else {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    String confirmedPassword = request.getParameter("confirmedPassword");
                    if (!password.equals(confirmedPassword)) {
                        showUnsuccessfulRegister(request, response);
                        return;
                    }

                    Document dataToAdd = new Document().append(USERNAME, username)
                            .append(PASSWORD, hashedPassword).append(TASKS, new ArrayList<>());
                    usersCollection.insertOne(dataToAdd);
                    request.getSession().setAttribute(USERNAME, username);
                    response.sendRedirect(TasksServlet.SHOW);
                }
            }
        }
    }

    private void showUnsuccessfulRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(SUCCESSFUL_REGISTER, false);
        request.getRequestDispatcher("/WEB-INF/jsp/view/register.jsp").forward(request, response);
    }

    private void showUnsuccessfulLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(SUCCESSFUL_LOGIN, false);
        request.getRequestDispatcher("/WEB-INF/jsp/view/login.jsp").forward(request, response);
    }

    public static boolean isEmpty(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }

        return false;
    }

}

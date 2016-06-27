package servlet;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import database.DBConnection;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Servlet implementation class ToDoApp
 */
@WebServlet(name = "toDoAppServlet", urlPatterns = {"/" + TasksServlet.SHOW,
        "/" + TasksServlet.CREATE_TASK, "/" + TasksServlet.DELETE_TASK}, loadOnStartup = 1)
public class TasksServlet extends HttpServlet {
    public static final String CREATE_TASK = "createTask";
    public static final String SHOW = "show";
    private static final long serialVersionUID = 1L;
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DELETE_TASK = "deleteTask";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TasksServlet() {
        super();

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().equals("/" + CREATE_TASK)) {
            request.getRequestDispatcher("/WEB-INF/jsp/view/createTask.jsp").forward(request,
                    response);
            return;
        }

        if (request.getServletPath().equals("/" + DELETE_TASK)) {
            response.sendRedirect(SHOW);
            return;
        }

        if (request.getServletPath().equals("/" + SHOW)) {
            MongoCollection<Document> usersCollection = DBConnection.getUsersCollection();
            String username = (String) request.getSession().getAttribute(LoginServlet.USERNAME);

            Document dateDocument = new Document();

            String yearParameter = request.getParameter(DBConnection.YEAR);
            String monthParameter = request.getParameter(DBConnection.MONTH);
            String dayParameter = request.getParameter(DBConnection.DAY);

            if (!LoginServlet.isEmpty(yearParameter)) {
                dateDocument.append(LoginServlet.TASKS + "." + DBConnection.YEAR,
                        Integer.parseInt(yearParameter));
            }

            if (!LoginServlet.isEmpty(monthParameter)) {
                dateDocument.append(LoginServlet.TASKS + "." + DBConnection.MONTH,
                        Integer.parseInt(monthParameter));
            }

            if (!LoginServlet.isEmpty(dayParameter)) {
                dateDocument.append(LoginServlet.TASKS + "." + DBConnection.DAY,
                        Integer.parseInt(dayParameter));
            }

            Document[] tasks = new Document[]{
                    new Document("$match", new Document(LoginServlet.USERNAME, username)),
                    new Document("$unwind", "$tasks"), new Document("$match", dateDocument),
                    new Document("$group", new Document("_id", "$_id").append("tasks",
                            new Document("$push", "$tasks")))};

            AggregateIterable<Document> userInfoWithFilteredTasks = usersCollection
                    .aggregate(Arrays.asList(tasks));

            Document userInfo = userInfoWithFilteredTasks.first();

            if (userInfo != null) {
                request.setAttribute(LoginServlet.TASKS, userInfo.get("tasks"));
            }
            request.getRequestDispatcher("/WEB-INF/jsp/view/showTasks.jsp").forward(request,
                    response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        int year = Integer.parseInt(request.getParameter(DBConnection.YEAR));
        int month = Integer.parseInt(request.getParameter(DBConnection.MONTH));
        int day = Integer.parseInt(request.getParameter(DBConnection.DAY));
        String title = request.getParameter(TasksServlet.TITLE);
        String description = request.getParameter(TasksServlet.DESCRIPTION);
        MongoCollection<Document> usersCollection = DBConnection.getUsersCollection();
        Document userInfo = new Document(DBConnection.USERNAME,
                request.getSession().getAttribute(DBConnection.USERNAME));

        Document taskDocument = new Document(TasksServlet.TITLE, title)
                .append(TasksServlet.DESCRIPTION, description).append(DBConnection.YEAR, year)
                .append(DBConnection.MONTH, month).append(DBConnection.DAY, day);

        if (servletPath.equals("/" + CREATE_TASK)) {

            Document newTaskQuery = new Document("$push",
                    new Document(DBConnection.TASKS, taskDocument));
            usersCollection.findOneAndUpdate(userInfo, newTaskQuery);
            response.sendRedirect(TasksServlet.SHOW);
        } else if (servletPath.equals("/" + DELETE_TASK)) {
            usersCollection.updateOne(userInfo,
                    new Document("$pull", new Document(DBConnection.TASKS, taskDocument)));
            response.sendRedirect(TasksServlet.SHOW);

        }

    }

    @Override
    public void init() throws ServletException {
        super.init();
        DBConnection.establishConnection();
    }

    @Override
    public void destroy() {
        DBConnection.closeConnection();
    }

}

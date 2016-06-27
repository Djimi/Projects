<%@page import="database.DBConnection" %>
<%@page import="org.bson.Document" %>
<%@page import="servlet.LoginServlet" %>
<%@page import="servlet.TasksServlet" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <title>To Do App</title>
    </head>
    <body>

        <div align="right" style="width: fit-content;height: fit-content;">
            <form action="logout" method="POST">
                <input type="submit" value="Log out"/>
            </form>

            <form action="createTask" method="GET">
                <input type="submit" value="Create task">
            </form>

            <h2>Filter</h2>
            <form action="show" method="GET">
                Day: <input type="number" name="day"><br> Month: <input
                    type="number" name="month"><br>Year: <input
                    type="number" name="year"><br>
                <input type="submit" value="Show tasks">
            </form>
            <br>
            <%
                @SuppressWarnings("unchecked")
                List<Document> tasks = (List<Document>) request.getAttribute(LoginServlet.TASKS);

                if (tasks != null) {

                    for (Document task : tasks) {
                        int day = task.getInteger(DBConnection.DAY);
                        int month = task.getInteger(DBConnection.MONTH);
                        int year = task.getInteger(DBConnection.YEAR);
                        String title = task.getString(TasksServlet.TITLE);
                        String description = task.getString(TasksServlet.DESCRIPTION);
            %>
            Title:
            <%=title%>
            <br> Date:
            <%=day%>-<%=month%>-<%=year%>
            <br> Description:
            <%=description%><br>
            <form action="deleteTask" method="POST">
                <input type="hidden" name="year" value=<%=year %>> <input
                    type="hidden" name="month" value=<%=month %>> <input
                    type="hidden" name="day" value=<%=day %>> <input
                    type="hidden" name="title" value=<%=title %>> <input
                    type="hidden" name="description" value=<%=description %>> <input
                    type="submit" value="Delete">
            </form>
            <br>
            <br> -----------------------------------------------------------
            <br>
            <br>
            <%
                    }
                }
            %>
        </div>
    </body>
</html>
<%@page import="servlet.LoginServlet" %>
<html>
    <head>
        <title>Login</title>
    </head>
    <body>
        <%
            Boolean successfulLogin = (Boolean) request.getAttribute(LoginServlet.SUCCESSFUL_LOGIN);
            if (successfulLogin != null && successfulLogin.equals(false)) {
        %>
        <h2>WRONG PASSWORD OR USERNAME!</h2>
        <%
            }
        %>
        <div align="right" style="width: fit-content;height: fit-content;">
            <form action="login" method="POST">
                Name: <input type="text" name="username"> <br>
                Password: <input type="password" name="password"> <br>
                <input type="submit" value="Log in">
            </form>
            <form action="intro" method="GET">
                <input type="submit" value="Intro">
            </form>

        </div>
    </body>
</html>
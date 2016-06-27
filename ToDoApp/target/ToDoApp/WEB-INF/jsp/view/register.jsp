<%@page import="servlet.LoginServlet" %>
<html>
    <head>
        <title>Register</title>
    </head>
    <body>
        <h2>Register</h2>

        <%
            Boolean successfulRegister = (Boolean) request
                    .getAttribute(LoginServlet.SUCCESSFUL_REGISTER);
            if (successfulRegister != null && successfulRegister.equals(false)) {
        %>
        <h2>BAD PASSWORD OR USERNAME!</h2>
        <%
            }
        %>

        <div align="right" style="width: fit-content;height: fit-content;">
            <form action="register" method="POST">
                Enter your name: <input type="text" name="username"> <br>
                Enter your password: <input type="password" name="password"> <br>
                Enter your password again: <input type="password" name="confirmedPassword">
                <br> <input type="submit" value="Register">
            </form>
            <form action="intro" method="GET">
                <input type="submit" value="Intro">
            </form>
        </div>

    </body>
</html>
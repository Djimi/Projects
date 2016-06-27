<%@page import="servlet.LoginServlet"%>
<html>
<head>
<title>ToDoApp</title>
</head>

<body>
	<h2>This is "TO DO" app</h2>

	<form action="login" method="GET">
		<input type="submit" value="Log in">
	</form>

	<form action="register" method="GET">
		<input type="submit" value="Register">
	</form>
</body>
</html>
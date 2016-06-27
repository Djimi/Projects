<html>
    <head>
        <title>To Do App</title>
    </head>
    <body>

        <form action="show" method="GET">
            <input type="submit" value="Show all tasks">
        </form>

        <div align="right" style="width: fit-content;height: fit-content;">
            <form action="createTask" method="POST">
                Title: <input type="text" name="title"><br>
                Description: <input type="text" name="description"><br>
                Day: <input type="number" name="day"><br>
                Month: <input type="number" name="month"><br>
                Year: <input type="number" name="year"><br>
                <input type="submit" value="Create Task"><br>
            </form>
        </div>
    </body>
</html>
package database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.ToDoTask;
import model.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Damyan Manev.
 */
public class DBConnection {

    private static MongoDatabase database;
    private static MongoClient mongoClient;
    private static MongoCollection<Document> usersCollection;

    public static final String USERNAME = "username";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String TASKS = "tasks";

    public static void establishConnection() {
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("ToDoAppDB");
        usersCollection = database.getCollection("users");
    }

    public static void closeConnection() {
        mongoClient.close();
    }

    public static MongoCollection<Document> getUsersCollection() {
        return usersCollection;
    }

    public static User parseUser(Document document) {
        if (document == null) {
            return null;
        }

        User user = new User();

        String name = document.getString("name");
        user.setName(name);

        String password = document.getString("password");
        user.setPassword(password);

        @SuppressWarnings("unchecked")
        List<Document> documentsList = (List<Document>) document.get("tasks");

        List<ToDoTask> todoTasks = new ArrayList<>();

        for (Document doc : documentsList) {
            ToDoTask task = parseToDoTask(doc);
            todoTasks.add(task);
        }

        user.setTasks(todoTasks);

        return user;
    }

    public static ToDoTask parseToDoTask(Document document) {
        ToDoTask task = new ToDoTask();

        Integer year = document.getInteger("year");
        task.setYear(year);

        Integer month = document.getInteger("month");
        task.setMonth(month);

        Integer day = document.getInteger("day");
        task.setDay(day);

        String title = document.getString("title");
        task.setTitle(title);

        String description = document.getString("description");
        task.setDescription(description);

        return task;
    }

}

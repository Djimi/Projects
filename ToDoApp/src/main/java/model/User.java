package model;

import java.util.List;

/**
 * Created by Damyan Manev.
 */
public class User {
	private String name;
	private String password;
	private List<ToDoTask> tasks;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ToDoTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ToDoTask> tasks) {
		this.tasks = tasks;
	}

}

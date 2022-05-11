package com.todo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "todo_items")
public class TodoItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String task;
    private Boolean isDone;
    private LocalDate date;

    public TodoItem() {
    }

    public TodoItem(Long userId, String task, Boolean isDone, LocalDate date) {
        this.userId = userId;
        this.task = task;
        this.isDone = isDone;
        this.date = date;
    }

    public TodoItem(Long id, Long userId, String task, Boolean isDone, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.task = task;
        this.isDone = isDone;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        task = task.trim();
        this.task = task;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public String getDate() {
        if (date != null)
            return date.toString();
        else
            return null;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", task='" + task + '\'' +
                ", isDone=" + isDone +
                ", date=" + date +
                '}';
    }
}

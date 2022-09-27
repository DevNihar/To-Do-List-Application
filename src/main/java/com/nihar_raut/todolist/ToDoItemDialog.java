package com.nihar_raut.todolist;

import datamodel.ToDoData;
import datamodel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class ToDoItemDialog {

    @FXML
    private TextField shortDescriptionField;
    @FXML
    private TextArea detailsField;
    @FXML
    private DatePicker deadLineField;

    public ToDoItem processData(){
        String shortDescription = shortDescriptionField.getText();
        String details = detailsField.getText();
        LocalDate deadLineValue = deadLineField.getValue();
        ToDoItem newItem = new ToDoItem(shortDescription, details, deadLineValue);
        ToDoData.getInstance().addToDoItems(newItem);
        return newItem;
    }
}

package com.nihar_raut.todolist;

import datamodel.ToDoData;
import datamodel.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    private List<ToDoItem> toDoItems;

    @FXML
    private ListView<ToDoItem> toDoListView;
    @FXML
    private TextArea itemsDetailsTextArea;
    @FXML
    private Label deadLine;

    public void initialize(){
        ToDoItem item1 = new ToDoItem("TCP Practical", "Attend TCP/IP Practical",
                LocalDate.of(2022, Month.SEPTEMBER, 26));
        ToDoItem item2 = new ToDoItem("Amazon Package", "Receive the amazon package",
                LocalDate.of(2022, Month.SEPTEMBER, 29));
        ToDoItem item3 = new ToDoItem("Project", "Finish the Escape Project until",
                LocalDate.of(2022, Month.OCTOBER, 10));
        ToDoItem item4 = new ToDoItem("Java Course", "Finish learning the java course",
                LocalDate.of(2022, Month.OCTOBER, 15));
        ToDoItem item5 = new ToDoItem("Pickup Laundry", "Pick up your laundry from the laundromat",
                LocalDate.of(2022, Month.AUGUST, 9));

        toDoItems = new ArrayList<>();
        toDoItems.add(item1);
        toDoItems.add(item2);
        toDoItems.add(item3);
        toDoItems.add(item4);
        toDoItems.add(item5);

        ToDoData.getInstance().setToDoItems(toDoItems);
        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observableValue, ToDoItem toDoItem, ToDoItem t1) {
                ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
                itemsDetailsTextArea.setText(item.getDetails());
                DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                deadLine.setText(df.format(item.getDeadLine()));
            }
        });
        toDoListView.getItems().setAll(toDoItems);
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();
    }
}
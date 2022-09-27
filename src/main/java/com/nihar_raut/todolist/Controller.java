package com.nihar_raut.todolist;

import datamodel.ToDoData;
import datamodel.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Controller {
    @FXML
    private ListView<ToDoItem> toDoListView;
    @FXML
    private TextArea itemsDetailsTextArea;
    @FXML
    private Label deadLine;
    @FXML
    private BorderPane mainBorderPane;

    public void initialize() {
//        ToDoItem item1 = new ToDoItem("TCP Practical", "Attend TCP/IP Practical",
//                LocalDate.of(2022, Month.SEPTEMBER, 26));
//        ToDoItem item2 = new ToDoItem("Amazon Package", "Receive the amazon package",
//                LocalDate.of(2022, Month.SEPTEMBER, 29));
//        ToDoItem item3 = new ToDoItem("Project", "Finish the Escape Project until",
//                LocalDate.of(2022, Month.OCTOBER, 10));
//        ToDoItem item4 = new ToDoItem("Java Course", "Finish learning the java course",
//                LocalDate.of(2022, Month.OCTOBER, 15));
//        ToDoItem item5 = new ToDoItem("Pickup Laundry", "Pick up your laundry from the laundromat",
//                LocalDate.of(2022, Month.AUGUST, 9));
//
//        toDoItems = new ArrayList<>();
//        toDoItems.add(item1);
//        toDoItems.add(item2);
//        toDoItems.add(item3);
//        toDoItems.add(item4);
//        toDoItems.add(item5);
//
//        ToDoData.getInstance().setToDoItems(toDoItems);
        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observableValue, ToDoItem toDoItem, ToDoItem t1) {
                ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
               try{
                    itemsDetailsTextArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    deadLine.setText(df.format(item.getDeadLine()));
                }catch (NullPointerException e){
                   System.out.println("The Item is null");
                   System.out.println(e.getMessage());
                }

            }
        });
        toDoListView.setItems(ToDoData.getInstance().getToDoItems());
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();

    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New ToDoItem");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("toDoItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
        }


        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ToDoItemDialog controller = fxmlLoader.getController();
            ToDoItem newItem = controller.processData();
//            toDoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
            toDoListView.getSelectionModel().select(newItem);
            System.out.println("OK Pressed");
        } else {
            System.out.println("Cancel Pressed");
        }
    }
}
package com.nihar_raut.todolist;

import datamodel.ToDoData;
import datamodel.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    @FXML
    private ListView<ToDoItem> toDoListView;
    @FXML
    private TextArea itemsDetailsTextArea;
    @FXML
    private Label deadLine;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private ToggleButton filterToggleButton;

    private FilteredList<ToDoItem> filteredList;

    private Predicate<ToDoItem> wantAllItems;
    private Predicate<ToDoItem> wantTodaysItem;

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
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        listContextMenu.getItems().addAll(deleteMenuItem);
        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observableValue, ToDoItem toDoItem, ToDoItem t1) {
                ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
               if(item != null) {
                   itemsDetailsTextArea.setText(item.getDetails());
                   DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                   deadLine.setText(df.format(item.getDeadLine()));
               }
            }
        });

        wantAllItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem item) {
                return true;
            }
        };

        wantTodaysItem = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem item) {
                return (item.getDeadLine().isEqual(LocalDate.now()));
            }
        };

        filteredList = new FilteredList<>(ToDoData.getInstance().getToDoItems(), wantAllItems);

        SortedList<ToDoItem> sortedList = new SortedList<>(filteredList,
                new Comparator<ToDoItem>() {
                    @Override
                    public int compare(ToDoItem o1, ToDoItem o2) {
                        return o1.getDeadLine().compareTo(o2.getDeadLine());
                    }
                });

        toDoListView.setItems(sortedList);
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();
        toDoListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> toDoItemListView) {
                ListCell<ToDoItem> cell = new ListCell<>(){
                    @Override
                    protected void updateItem(ToDoItem item, boolean b) {
                        super.updateItem(item, b);
                        if(b){
                            setText(null);
                        }else {
                            setText(item.getShortDescription());
                            if(LocalDate.now().equals(item.getDeadLine())){
                                setTextFill(Color.RED);
                            }else if(item.getDeadLine().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.ROSYBROWN);
                            }else if(item.getDeadLine().isBefore(LocalDate.now())){
                                setTextFill(Color.CORAL);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty)->{
                            if(isNowEmpty){
                                cell.setContextMenu(null);
                            }else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return cell;
            }
        });
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
        }
    }
    public void deleteItem(ToDoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete To Do Item");
        alert.setHeaderText("Delete item " + item.getShortDescription());
        alert.setContentText("Are you sure ? Press Ok to confirm and Cancel to back out");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK)){
            ToDoData.getInstance().deleteListItem(item);
        }
    }
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        ToDoItem selectedItem = toDoListView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            if(keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteItem(selectedItem);
            }
        }
    }

    @FXML
    public void handleFilterButton(){
        ToDoItem selectedItem = toDoListView.getSelectionModel().getSelectedItem();
        if(filterToggleButton.isSelected()){
            filteredList.setPredicate(wantTodaysItem);
            if(filteredList.isEmpty()){
                itemsDetailsTextArea.clear();
                deadLine.setText("");
            } else if (filteredList.contains(selectedItem)) {
                toDoListView.getSelectionModel().select(selectedItem);
            }else {
                toDoListView.getSelectionModel().selectFirst();
            }
        }else {
            filteredList.setPredicate(wantAllItems);
            toDoListView.getSelectionModel().select(selectedItem);
        }
    }
}
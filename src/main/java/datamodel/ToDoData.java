package datamodel;

import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class ToDoData {
    private static ToDoData toDoData =  new ToDoData();
    private static String fileName = "ToDoItems.txt";
    private List<ToDoItem> toDoItems;
    private DateTimeFormatter formatter;

    private ToDoData(){
        this.formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static ToDoData getInstance(){
        return toDoData;
    }

    public List<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void setToDoItems(List<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }

    public void loadToDoItems() throws IOException {
        toDoItems = FXCollections.observableArrayList();
        Path filePath = Paths.get(fileName);
        String Input;
        try(BufferedReader bf = Files.newBufferedReader(filePath)){
            while((Input = bf.readLine())!= null){
                String[] stringPieces = Input.split("\t");
                String shortDescription = stringPieces[0];
                String details = stringPieces[1];
                String date = stringPieces[2];
                LocalDate dueDate = LocalDate.parse(date, formatter);
                ToDoItem item = new ToDoItem(shortDescription, details, dueDate);
                toDoItems.add(item);
            }
        }
    }

    public void saveToDOItems() throws IOException{
        Path filePath = Paths.get(fileName);


        try(BufferedWriter bw = Files.newBufferedWriter(filePath)){
            Iterator<ToDoItem> iter = toDoItems.iterator();
            while(iter.hasNext()){
                ToDoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadLine()));
                bw.newLine();
            }
        }
    }





















}

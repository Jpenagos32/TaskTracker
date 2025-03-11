import Models.TaskModel;
import Models.TaskModelDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFile {
    private final String FILE_NAME = "data.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Creates a JSON file at internal storage
     *
     * @param task New Task
     */
    public void createJsonFile(TaskModel task) {
        // verifica que exista el archivo
        File jsonFile = new File(FILE_NAME);

        if (jsonFile.exists()) {
            // leer los datos que contiene
            List<TaskModel> tasks = getTasksFromFile(FILE_NAME);
            // Crear nueva tarea y agregarla a la lista
            tasks.add(task);

            TaskModelDTO taskModelDTO = new TaskModelDTO(tasks);
            writeTasksToFile(taskModelDTO, FILE_NAME);
            System.out.println("***Task added***");

        } else {
            // Si no existe el archivo se crea uno nuevo agregando la tarea
            List<TaskModel> tasks = new ArrayList<>();

            tasks.add(task);
            TaskModelDTO taskModelDTO = new TaskModelDTO(tasks);

            writeTasksToFile(taskModelDTO, FILE_NAME);
            System.out.println("***Task added***");
        }
    }

    public List<TaskModel> getTasksFromFile(String fileName) {
        File jsonFile = new File(fileName);

        if (jsonFile.exists()) {
            // si el archivo existe, intentar leerlo y obtener los datos
            try (FileReader reader = new FileReader(fileName)) {
                TaskModelDTO jsonContent = gson.fromJson(reader, TaskModelDTO.class);
                return new ArrayList<>(jsonContent.tasks());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return new ArrayList<>();
            }
        } else {
            System.out.println("File does not exists");
            return new ArrayList<>();
        }
    }

    public void writeTasksToFile(TaskModelDTO taskModelDTO, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(taskModelDTO, writer);
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }




}

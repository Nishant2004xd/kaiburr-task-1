package com.myproject.kaiburrtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController // Tells Spring this is a REST API controller
@RequestMapping("/tasks") // All endpoints will start with /tasks
public class TaskController {

    @Autowired // Asks Spring to "inject" the repository for us
    private TaskRepository taskRepository;

    // Endpoint 1: PUT a task (Create/Update)
    @PutMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {

        // Validation logic
        if (!CommandValidator.isCommandSafe(task.getCommand())) {
            // Return a 400 Bad Request error if command is unsafe
            return new ResponseEntity<>("Command is unsafe or invalid.", HttpStatus.BAD_REQUEST);
        }

        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    // Endpoint 2 & 3: GET tasks (all or by ID)
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) String id) {
        if (id != null) {
            // Get by ID
            Optional<Task> task = taskRepository.findById(id);
            if (task.isPresent()) {
                return new ResponseEntity<>(List.of(task.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
            }
        } else {
            // Get all
            List<Task> tasks = taskRepository.findAll();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }
    }

    // Endpoint 4: DELETE a task by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable String id) {
        try {
            taskRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (Exception e) {
            // This happens if the ID doesn't exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint 5: GET (find) tasks by name
    @GetMapping("/findByName")
    public ResponseEntity<List<Task>> findTasksByName(@RequestParam String name) {
        List<Task> tasks = taskRepository.findByNameContaining(name);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        } else {
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }
    }

    // Endpoint 6: PUT a TaskExecution (by task ID)
    @PutMapping("/{id}/execute")
    public ResponseEntity<Task> executeTask(@PathVariable String id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (!taskOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOptional.get();
        TaskExecution execution = new TaskExecution();
        execution.setStartTime(new Date());

        try {
            // Create a process builder for the command
            ProcessBuilder processBuilder = new ProcessBuilder();
            // This runs the command using sh -c, which handles simple shell commands
            // For Windows, you might change "sh", "-c" to "cmd.exe", "/c"
            processBuilder.command("cmd.exe", "/c", task.getCommand());

            Process process = processBuilder.start();

            // Capture the output
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor(); // Wait for the command to finish
            execution.setEndTime(new Date());

            if (exitCode == 0) {
                execution.setOutput(output.toString());
            } else {
                // Capture error stream if something went wrong
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder("Error:\n");
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                execution.setOutput(errorOutput.toString());
            }

        } catch (Exception e) {
            execution.setEndTime(new Date());
            execution.setOutput("Execution failed: " + e.getMessage());
        }

        // Add the new execution to the task's list
        if (task.getTaskExecutions() == null) {
            task.setTaskExecutions(new ArrayList<>());
        }
        task.getTaskExecutions().add(execution);

        // Save the updated task back to the database
        Task updatedTask = taskRepository.save(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }
}
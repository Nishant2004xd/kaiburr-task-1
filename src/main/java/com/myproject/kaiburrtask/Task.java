package com.myproject.kaiburrtask;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@lombok.Data // From Lombok
@Document(collection = "tasks") // Tells Spring this is a MongoDB document
public class Task {

    @Id // Marks this as the unique ID
    private String id;

    private String name;
    private String owner;
    private String command;

    // This will hold a list of all the times this task was run
    private List<TaskExecution> taskExecutions;
}
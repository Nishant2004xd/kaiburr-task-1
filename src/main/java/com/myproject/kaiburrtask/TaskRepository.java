package com.myproject.kaiburrtask;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// This interface extends MongoRepository
// This is what gives us all the database methods for free:
// .save(), .findById(), .findAll(), .deleteById(), etc.
public interface TaskRepository extends MongoRepository<Task, String> {

    // This is for the "find tasks by name" endpoint
    // Spring Data MongoDB will automatically create the query
    // just from the method name!
    List<Task> findByNameContaining(String name);
}
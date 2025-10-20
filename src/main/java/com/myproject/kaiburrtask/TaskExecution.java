package com.myproject.kaiburrtask;

import java.util.Date;

// This @Data annotation comes from Lombok
// It automatically creates getters, setters, and constructors
@lombok.Data
public class TaskExecution {

    // These properties are from the PDF
    private Date startTime;
    private Date endTime;
    private String output;

}
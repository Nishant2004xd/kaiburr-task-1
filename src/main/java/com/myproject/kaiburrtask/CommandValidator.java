package com.myproject.kaiburrtask;

import java.util.List;

public class CommandValidator {

    // A simple list of "unsafe" command fragments
    // You can add more to this list if you want
    private static final List<String> DENY_LIST = List.of(
            "rm -rf", "sudo", "mv", "dd", ":(){:|:&};:", "wget", "curl", ">", "<", "|", "&"
    );

    public static boolean isCommandSafe(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false; // Empty commands are not valid
        }

        String lowerCaseCommand = command.toLowerCase();
        for (String unsafe : DENY_LIST) {
            if (lowerCaseCommand.contains(unsafe)) {
                return false; // Found an unsafe fragment
            }
        }
        return true; // Seems safe
    }
}
package Enums;

public enum TaskStatus {
    DONE("Done"),
    TODO("Todo"),
    IN_PROGRESS("In Progress");

    // Código necesario para poder darle un nombre a cada ENUM
    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}

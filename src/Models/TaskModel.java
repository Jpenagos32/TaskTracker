package Models;


public record TaskModel (
    String id,
    String description,
    String status,
    String createdAt,
    String updatedAt
){}

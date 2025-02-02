package model;

public record UpdateStudentById(
        String student_name,
        Integer age,
        String email,
        Character grade,
        Double score
) {
}

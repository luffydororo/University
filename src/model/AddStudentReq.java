package model;

public record AddStudentReq(
        String student_name,
        Integer age,
        String email,
        Character grade,
        Double score
) {
}

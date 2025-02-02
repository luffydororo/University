package model;

public record StudentResponseDto (
        String student_name,
        Integer age,
        String email,
        Character grade,
        Double score
) {
}

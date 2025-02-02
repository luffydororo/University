package repository;

import model.AddStudentReq;
import model.Student;
import model.UpdateStudentById;

import java.sql.SQLException;
import java.util.List;

public interface StudentRepository {
    List<Student> getAllStudents();
    Student getStudentById(int id);
   List<Student> getStudentByGrade(char grade);
    Student addStudent(AddStudentReq addStudentReq);
    Student updateStudent(int id, UpdateStudentById updateStudentById);
    Integer deleteStudentById(int id);
    List<Student> getTopStudents();

}

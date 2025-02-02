package repository;

import jdk.jshell.spi.SPIResolutionException;
import model.AddStudentReq;
import model.Student;
import model.UpdateStudentById;
import utils.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public List<Student> getAllStudents() {
        List<Student> studentsList = new ArrayList<>();
        try {
            Connection connection = DBConfig.getDbConfig();
            Statement statement = connection.createStatement();
            ResultSet resultSet =statement.executeQuery("""
SELECT * FROM students 
""");
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setEmail(resultSet.getString("email"));
                String gradeStr = resultSet.getString("grade");
                student.setGrade(gradeStr.charAt(0));
                student.setScore(resultSet.getDouble("score"));
                studentsList.add(student);
            }
            return studentsList;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return List.of();
    }
    @Override
    public List<Student> getTopStudents() {
        List<Student> topStudents = new ArrayList<>();
        try{
            Connection connection = DBConfig.getDbConfig();
            PreparedStatement preparedStatement = connection.prepareStatement("""
SELECT id, name,age,email,grade, score FROM students WHERE score >= 90 ORDER BY score DESC
""");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setEmail(resultSet.getString("email"));
                String gradeStr = resultSet.getString("grade");
                student.setGrade(gradeStr.charAt(0));
                student.setScore(resultSet.getDouble("score"));
                topStudents.add(student);
            }
//return topStudents;
        } catch (Exception e) {
            System.out.println("Student Not found"+e.getMessage());
        }
        return topStudents.isEmpty() ? List.of() : topStudents;
    }
    @Override
    public Student getStudentById(int id) {
        try{
            Connection connection = DBConfig.getDbConfig();
            PreparedStatement preparedStatement =connection.prepareStatement("""
SELECT * FROM students WHERE id = ?
""");
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Student student = new Student();

        while (resultSet.next()) {
            student.setId(resultSet.getInt("id"));
            student.setName(resultSet.getString("name"));
            student.setAge(resultSet.getInt("age"));
            student.setEmail(resultSet.getString("email"));
            String gradeStr = resultSet.getString("grade");
            student.setGrade(gradeStr.charAt(0));
            student.setScore(resultSet.getDouble("score"));
        }
        return student;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Student> getStudentByGrade(char grade) {
        List<Student> AllStudentsByGrade = new ArrayList<>();
        try{
            Connection connection = DBConfig.getDbConfig();
            assert connection != null;
            PreparedStatement preparedStatement =connection.prepareStatement("""
SELECT * FROM students WHERE grade = ?
""");
            preparedStatement.setString(1, String.valueOf(grade));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setEmail(resultSet.getString("email"));
                String gradeStr = resultSet.getString("grade");
                student.setGrade(gradeStr.charAt(0));
                student.setScore(resultSet.getDouble("score"));
                AllStudentsByGrade.add(student);
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return AllStudentsByGrade;
    }


    @Override
    public Student addStudent(AddStudentReq addStudentReq) {
        try{
            Connection connection = DBConfig.getDbConfig();
            PreparedStatement preparedStatement = connection.prepareStatement("""
INSERT INTO students(id,name,age,email,grade,score) VALUES (?,?,?,?,?,?)
 """);
            int student_id = new Random().nextInt(100);
            if (validateStudent(addStudentReq)) {
                preparedStatement.setInt(1, student_id);
                preparedStatement.setString(2,addStudentReq.student_name());
                preparedStatement.setInt(3,addStudentReq.age());
                preparedStatement.setString(4,addStudentReq.email());
                String gradeStr = addStudentReq.grade().toString().toUpperCase();
                preparedStatement.setString(5,gradeStr);
                preparedStatement.setDouble(6,addStudentReq.score());
                int rowAffected = preparedStatement.executeUpdate();
                String message = rowAffected > 0 ?"Add new student successfully " :"Add student fail";
                System.out.println(message);
                if (rowAffected > 0) {
                    return  new Student(student_id,addStudentReq.student_name(),addStudentReq.age(),addStudentReq.email(),addStudentReq.grade(),addStudentReq.score());
                }
            }else {
                System.out.println("Invalid student data");
            }
        }catch (Exception e) {
            System.out.println("Database error"+e.getMessage());
        }
        return null;
    }

    @Override
    public Student updateStudent(int id, UpdateStudentById updateStudentById)  {
            try{Connection connection = DBConfig.getDbConfig();
                PreparedStatement pre = getPreparedStatement(connection,id, updateStudentById);
                connection.setAutoCommit(false);
                if (validateStudent(updateStudentById)) {
                    int rowAffected = pre.executeUpdate();
                    connection.commit();
                    if (rowAffected > 0) {
                        System.out.println("Update student successfully ");
                        return getStudentById(id);
                    }else {
                        connection.rollback();
                        System.out.println("Update student fail");
                    }
                }else {
                    System.out.println("Invalid student data");
                }

            }catch (Exception e) {
                System.out.println("Error Update students" + e.getMessage());
            }
            return getStudentById(id);
    }
    public void addStudentsInBatch(List<AddStudentReq> students) {
        try (Connection connection = DBConfig.getDbConfig();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students(id, name, age, email, grade, score) VALUES (?, ?, ?, ?, ?, ?)")) {
            connection.setAutoCommit(false); // Start transaction
            for (AddStudentReq student : students) {
                if (validateStudent(student)) {
                    int student_id = new Random().nextInt(100);
                    preparedStatement.setInt(1, student_id);
                    preparedStatement.setString(2, student.student_name());
                    preparedStatement.setInt(3, student.age());
                    preparedStatement.setString(4, student.email());
                    preparedStatement.setString(5, String.valueOf(student.grade()));
                    preparedStatement.setDouble(6, student.score());
                    preparedStatement.addBatch();
                } else {
                    System.out.println("Invalid student data for: " + student.student_name());
                }
            }
            preparedStatement.executeBatch();
            connection.commit(); // Commit transaction
            System.out.println("Students added in batch successfully");
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

            private static PreparedStatement getPreparedStatement(Connection connection, int id, UpdateStudentById updateStudentById) throws SQLException {
        PreparedStatement pre = connection.prepareStatement("""
    UPDATE students 
    SET name = ?, age = ?, email = ?, grade = ?, score = ?
    WHERE id = ?""");
    if (updateStudentById.student_name() != null) {
        pre.setString(1, updateStudentById.student_name());
    }
    if (updateStudentById.age() != null) {
        pre.setInt(2, updateStudentById.age());
    }
    if (updateStudentById.email() != null) {
        pre.setString(3, updateStudentById.email());
    }
    if (updateStudentById.grade() != null) {
        pre.setString(4, updateStudentById.grade().toString());
    }
    if (updateStudentById.score() != null) {
        pre.setDouble(5, updateStudentById.score());
    }
    pre.setInt(6, id);
    return pre;
}

    @Override
    public Integer deleteStudentById(int id) {
        try{
            Connection connection = DBConfig.getDbConfig();
            PreparedStatement pre = connection.prepareStatement("""
DELETE FROM students
WHERE id = ?
""");
            pre.setInt(1, id);
            int rowAffected = pre.executeUpdate();
            String message = rowAffected > 0 ?"User with ID : "+id+" has been deleted 🙏🙏🙏🙏 ":"User ID not found";
            System.out.println(message);
            return rowAffected;
        }catch (Exception e) {
            System.out.println("Error: delete by id"+e.getMessage());
        }

        return 0;
    }

    private boolean validateStudent(AddStudentReq student) {
        return validateAge(student.age()) &&
                validateEmail(student.email()) &&
                validateGrade(student.grade()) &&
                validateScore(student.score());
    }

    private boolean validateStudent(UpdateStudentById student) {
        return validateAge(student.age()) &&
                validateEmail(student.email()) &&
                validateGrade(student.grade()) &&
                validateScore(student.score());
    }

    private boolean validateAge(int age) {
        return age > 0 & age < 50;
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, email);
    }
    private boolean validateGrade(char grade) {
        // Check if the grade is one of the valid options
        return grade == 'A' || grade == 'B' || grade == 'C' || grade == 'D' || grade == 'F';
    }
    private boolean validateScore(double score) {
        // Check if the score is between 0 and 100 inclusive
        return score >= 0 && score <= 100;
    }


    }


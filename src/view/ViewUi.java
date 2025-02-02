package view;

import model.AddStudentReq;
import model.Student;
import model.StudentResponseDto;
import model.UpdateStudentById;
import repository.StudentRepositoryImpl;
import utils.TableUiConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewUi {
    public static void ui(){
        StudentRepositoryImpl studentRepository = new StudentRepositoryImpl();
        while (true){
            System.out.print("""
                   ----------------------------
                   1:Show all students
                   2:Add new student
                   3:Update student by ID
                   4:Find student by Grade
                   5: Get Top Students
                   6:Delete student by ID
                   7:Insert More students
                   --------------------------
                    """);
            System.out.print(" [+] Insert Option: ");
            int numberInsert = new Scanner(System.in).nextInt();
            switch (numberInsert){
                case 1 ->{
                    System.out.println("List all student : ");
                    System.out.println(TableUiConfig.getTable(studentRepository.getAllStudents()).render());
                }
                case 2 ->{
                    System.out.println("Enter student details:");
                    System.out.print("Insert student name:");
                    String studentName = new Scanner(System.in).nextLine();
                    System.out.print("Insert student age:");
                    int studentAge = new Scanner(System.in).nextInt();
                    System.out.print("Insert student email:");
                    String studentEmail = new Scanner(System.in).nextLine();
                    System.out.print("Insert student grade:");
                    char studentGrade = new Scanner(System.in).next().toLowerCase().charAt(0);
                    System.out.print("Insert student score:");
                    double studentScore = new Scanner(System.in).nextDouble();
                    Student student1 = studentRepository.addStudent(new AddStudentReq(studentName,studentAge,studentEmail,studentGrade,studentScore));
                    System.out.println(student1);
                }
                case 3 ->{
                    System.out.print("Insert student ID to Update:");
                    int studentID = new Scanner(System.in).nextInt();
                    System.out.print("Insert student name:");
                    String studentName = new Scanner(System.in).nextLine();
                    System.out.print("Insert student age:");
                    int studentAge = new Scanner(System.in).nextInt();
                    System.out.print("Insert student email:");
                    String studentEmail = new Scanner(System.in).nextLine();
                    System.out.print("Insert student grade:");
                    char studentGrade = new Scanner(System.in).next().toLowerCase().charAt(0);
                    System.out.print("Insert student score:");
                    double studentScore = new Scanner(System.in).nextDouble();
                    Student studentUp =studentRepository.updateStudent(studentID,new UpdateStudentById(studentName,studentAge,studentEmail,studentGrade,studentScore));
                    StudentResponseDto responseDto = new StudentResponseDto(
                        studentUp.getName(),
                            studentUp.getAge(),
                            studentUp.getEmail(),
                            studentUp.getGrade(),
                            studentUp.getScore());
                    System.out.println(responseDto);

                }
                case 4 ->{
                    System.out.println("Get student by Grade:");
                    System.out.print("Insert student grade:");
                    char studentGrade = new Scanner(System.in).nextLine().toUpperCase().charAt(0);
                    List<Student> student2 =  studentRepository.getStudentByGrade(studentGrade);
                    if (student2.isEmpty()) {
                        System.out.println("No students found with grade " + studentGrade);
                    } else {
                        System.out.println(TableUiConfig.getTable(student2).render());
                    }
                }
                case 5 ->{
                    System.out.println("List top-performance student");
                    System.out.println(TableUiConfig.getTable(studentRepository.getTopStudents()).render());

                }
                case 6 ->{
                    System.out.print("Delete student by ID:");
                    int studentID = new Scanner(System.in).nextInt();
                    studentRepository.deleteStudentById(studentID);
                }
                case 7 ->{
                    System.out.print("Insert student Name:");
                    List<AddStudentReq> student = new ArrayList<>();
                    while (true){
                        System.out.print("Insert student name:");
                        String studentName = new Scanner(System.in).nextLine();
                        if ("done".equalsIgnoreCase(studentName)) break;
                        System.out.print("Insert student age:");
                        int studentAge = new Scanner(System.in).nextInt();
                        System.out.print("Insert student email:");
                        String studentEmail = new Scanner(System.in).nextLine();
                        System.out.print("Insert student grade:");
                        char studentGrade = new Scanner(System.in).next().toUpperCase().charAt(0);
                        System.out.print("Insert student score:");
                        double studentScore = new Scanner(System.in).nextDouble();
                        new Scanner(System.in).nextLine();
                        student.add(new AddStudentReq(studentName,studentAge,studentEmail,studentGrade,studentScore));
                    }
                    studentRepository.addStudentsInBatch(student);
                }
            }
        }
    }
}

package utils;

import model.Student;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import repository.StudentRepositoryImpl;

import java.util.List;

public class TableUiConfig {
    public static Table getTable(List<Student> studentList) {
        StudentRepositoryImpl userRepository = new StudentRepositoryImpl();
        userRepository.getAllStudents().forEach(System.out::println);
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        String [] columnNames = {"ID","Student","Age","Email","Grade","Score" };
        //Add column
        for (int i=0;i<6;i++){
            table.addCell(columnNames[i],new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i,10,30);
        }
        for(Student student : studentList ){
            table.addCell(student.getId().toString(),new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(student.getName(),new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(student.getAge().toString(),new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(student.getEmail(),new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(student.getGrade().toString(),new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(student.getScore().toString(),new CellStyle(CellStyle.HorizontalAlign.center));
        }
        return table;

    }
}

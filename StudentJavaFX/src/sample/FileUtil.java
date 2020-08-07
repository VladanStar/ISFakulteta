package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author profesor
 */
public class FileUtil {

    private static BufferedReader reader;
    private static BufferedWriter writer;

    /**
     * Metoda koja upisuje studenta u fajl u formatu ime prezime smer tip ime
     * prezime smet tip....
     *
     * @param student
     */
    public static void saveData(Student student) {
        try {
            writer = new BufferedWriter(new FileWriter("studenti.txt", true));
            writer.write(student.toString());
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Metoda za čitanje studenata iz fajla
     *
     * @return
     */
    public static ObservableList<Student> readData() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        try {
            reader = new BufferedReader(new FileReader("studenti.txt"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                students.add(new Student(data[0], data[1], Student.Smer.valueOf(data[2]), data[3].equals("tradicionalno") ? true : false));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Fajl nije jos uvek kreiran");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return students;
    }
}
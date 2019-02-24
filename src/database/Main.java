package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String DRIVER =  "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/studentdb";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.println("Welcome to studentDB");
        viewOptions();

        while (true) {
            System.out.print("Command: ");
            String command = reader.next();

            switch (command) {
                case "i":
                    insertStudent();
                    break;
                case "v":
                    viewStudents();
                    break;
                case "o":
                    viewOptions();
                    break;
                case "q":
                    System.out.println("Quit");
                    reader.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Command");
            }
        }
    }

    private static void insertStudent() {
        System.out.println("Insert Student");
        Scanner reader = new Scanner(System.in);
        try {
            Connection conn = getConnection();
            System.out.print("First Name: ");
            String first = reader.next();
            System.out.print("Last Name: ");
            String last = reader.next();
            System.out.print("Academic Year (1-4): ");
            int year = reader.nextInt();
            System.out.print("GPA: ");
            double gpa = reader.nextDouble();
            PreparedStatement post = conn.prepareStatement("INSERT INTO students (first, last, year, gpa) VALUES ('"+first+"', '"+last+"', '"+year+"', '"+gpa+"')");
            post.executeUpdate();
            System.out.println("Student Inserted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void viewStudents() {
        ArrayList<Student> studentList = new ArrayList<Student>();
        try {
            Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM students");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("first"),
                        resultSet.getString("last"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("gpa"));
                studentList.add(student);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("================= Students =====================");
        System.out.println("ID   First          Last          Year    GPA");
        System.out.println("------------------------------------------------");

        for (Student student : studentList) {
            System.out.print(String.format("%-3s %-15s %-15s %-4s %4s\n", student.getId(), student.getFirst(),student.getLast(),student.getYear(),student.getGpa()));
            System.out.println();
        }
        System.out.println("=================================================");
    }

    private static void viewOptions() {
        System.out.println("========================");
        System.out.println("         Options        ");
        System.out.println("[i] - Insert Student     ");
        System.out.println("[v] - View Student Table");
        System.out.println("[o] - View Options       ");
        System.out.println("[q] - Quit       ");
        System.out.println("========================");
    }

    private static void createTable() {
        try {
            Connection conn = getConnection();
            PreparedStatement create = conn.prepareStatement("CREATE TABLE IF NOT EXISTS students(id INT NOT NULL AUTO_INCREMENT, first VARCHAR(60), last VARCHAR(60), year INT, gpa DECIMAL(3,2), PRIMARY KEY(id))");
            create.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("Created Table");
        }
    }

    private static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(URL, USERNAME, "");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Student Grade Management System
 * A comprehensive console application to manage student marks and calculate performance statistics.
 */
public class StudentGradeManager {

    // Model class for Student
    public static class Student {
        private String name;
        private double mark;

        public Student(String name, double mark) {
            this.name = name;
            this.mark = mark;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getMark() {
            return mark;
        }

        public void setMark(double mark) {
            this.mark = mark;
        }

        public String getLetterGrade() {
            if (mark >= 90) return "A+";
            if (mark >= 80) return "A";
            if (mark >= 70) return "B";
            if (mark >= 60) return "C";
            if (mark >= 50) return "D";
            return "F";
        }

        public String getStatus() {
            return mark >= 50 ? "PASS" : "FAIL";
        }
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("==================================================");
        System.out.println("      WELCOME TO STUDENT GRADE MANAGER          ");
        System.out.println("==================================================");

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice (1-7): ", 1, 7);
            System.out.println();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addSampleData();
                case 3 -> displayAllStudents();
                case 4 -> displaySummaryReport();
                case 5 -> searchStudent();
                case 6 -> deleteStudent();
                case 7 -> {
                    running = false;
                    System.out.println("Thank you for using Student Grade Manager. Goodbye!");
                }
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("--------------------------------------------------");
        System.out.println("                      MENU                        ");
        System.out.println("--------------------------------------------------");
        System.out.println("1. Add New Student");
        System.out.println("2. Add Sample Data (Demo Records)");
        System.out.println("3. Display All Students");
        System.out.println("4. Display Summary Report (Avg, Max, Min Stats)");
        System.out.println("5. Search Student by Name");
        System.out.println("6. Delete Student Record");
        System.out.println("7. Exit");
        System.out.println("--------------------------------------------------");
    }

    private static void addStudent() {
        System.out.println("--- Add New Student ---");
        String name = readNonEmptyString("Enter Student Name: ");
        double mark = readDouble("Enter Marks (0.0 to 100.0): ", 0.0, 100.0);

        students.add(new Student(name, mark));
        System.out.printf("Successfully added student: %s with mark: %.2f%n", name, mark);
    }

    private static void addSampleData() {
        System.out.println("--- Loading Sample Student Data ---");
        students.add(new Student("Alice Smith", 94.5));
        students.add(new Student("Bob Johnson", 78.0));
        students.add(new Student("Charlie Brown", 85.25));
        students.add(new Student("Diana Prince", 42.0));
        students.add(new Student("Ethan Hunt", 66.5));
        students.add(new Student("Fiona Gallagher", 91.0));
        System.out.println("6 sample student records added successfully!");
    }

    private static void displayAllStudents() {
        System.out.println("--- All Student Records ---");
        if (students.isEmpty()) {
            System.out.println("No student records found. Add students first.");
            return;
        }

        printTableLine();
        System.out.printf("| %-4s | %-22s | %-8s | %-6s | %-6s |%n", "No.", "Name", "Mark", "Grade", "Status");
        printTableLine();

        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("| %-4d | %-22s | %-8.2f | %-6s | %-6s |%n",
                    (i + 1), truncate(s.getName(), 22), s.getMark(), s.getLetterGrade(), s.getStatus());
        }
        printTableLine();
        System.out.printf("Total Records: %d%n", students.size());
    }

    private static void displaySummaryReport() {
        System.out.println("==================================================");
        System.out.println("             STUDENT SUMMARY REPORT               ");
        System.out.println("==================================================");

        if (students.isEmpty()) {
            System.out.println("No student records found to generate statistics.");
            return;
        }

        double sum = 0;
        double highestMark = Double.NEGATIVE_INFINITY;
        double lowestMark = Double.POSITIVE_INFINITY;

        for (Student s : students) {
            double m = s.getMark();
            sum += m;
            if (m > highestMark) highestMark = m;
            if (m < lowestMark) lowestMark = m;
        }

        double average = sum / students.size();

        // Collect top and lowest scorers (handles ties)
        List<String> topScorers = new ArrayList<>();
        List<String> lowestScorers = new ArrayList<>();

        int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;
        int passCount = 0;

        for (Student s : students) {
            if (Math.abs(s.getMark() - highestMark) < 0.001) {
                topScorers.add(s.getName() + " (" + s.getMark() + ")");
            }
            if (Math.abs(s.getMark() - lowestMark) < 0.001) {
                lowestScorers.add(s.getName() + " (" + s.getMark() + ")");
            }

            switch (s.getLetterGrade()) {
                case "A+", "A" -> countA++;
                case "B" -> countB++;
                case "C" -> countC++;
                case "D" -> countD++;
                default -> countF++;
            }

            if (s.getMark() >= 50) passCount++;
        }

        int total = students.size();
        double passPercentage = ((double) passCount / total) * 100;

        System.out.printf("Total Students Analyzed : %d%n", total);
        System.out.printf("Average Marks           : %.2f / 100.0%n", average);
        System.out.printf("Highest Mark            : %.2f%n", highestMark);
        System.out.printf("Top Performer(s)        : %s%n", String.join(", ", topScorers));
        System.out.printf("Lowest Mark             : %.2f%n", lowestMark);
        System.out.printf("Lowest Performer(s)     : %s%n", String.join(", ", lowestScorers));
        System.out.printf("Pass Rate               : %d / %d (%.1f%%)%n", passCount, total, passPercentage);

        System.out.println("--------------------------------------------------");
        System.out.println("Grade Distribution:");
        System.out.printf("  A/A+ (>=80) : %d (%.1f%%)%n", countA, (countA * 100.0 / total));
        System.out.printf("  B    (70-79): %d (%.1f%%)%n", countB, (countB * 100.0 / total));
        System.out.printf("  C    (60-69): %d (%.1f%%)%n", countC, (countC * 100.0 / total));
        System.out.printf("  D    (50-59): %d (%.1f%%)%n", countD, (countD * 100.0 / total));
        System.out.printf("  F    (<50)  : %d (%.1f%%)%n", countF, (countF * 100.0 / total));
        System.out.println("==================================================");
    }

    private static void searchStudent() {
        System.out.println("--- Search Student ---");
        if (students.isEmpty()) {
            System.out.println("No records stored.");
            return;
        }

        String query = readNonEmptyString("Enter student name to search: ").toLowerCase();
        List<Student> results = new ArrayList<>();

        for (Student s : students) {
            if (s.getName().toLowerCase().contains(query)) {
                results.add(s);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No matching student records found.");
        } else {
            System.out.printf("Found %d matching record(s):%n", results.size());
            printTableLine();
            System.out.printf("| %-22s | %-8s | %-6s | %-6s |%n", "Name", "Mark", "Grade", "Status");
            printTableLine();
            for (Student s : results) {
                System.out.printf("| %-22s | %-8.2f | %-6s | %-6s |%n",
                        truncate(s.getName(), 22), s.getMark(), s.getLetterGrade(), s.getStatus());
            }
            printTableLine();
        }
    }

    private static void deleteStudent() {
        System.out.println("--- Delete Student Record ---");
        displayAllStudents();

        if (students.isEmpty()) {
            return;
        }

        int index = readInt("Enter Student No. to delete (0 to cancel): ", 0, students.size());
        if (index == 0) {
            System.out.println("Delete operation cancelled.");
            return;
        }

        Student removed = students.remove(index - 1);
        System.out.printf("Successfully removed record for '%s'.%n", removed.getName());
    }

    // Input Helper Methods with Robust Exception Handling
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Invalid entry. Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Invalid entry. Value must be between %.1f and %.1f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid decimal number.");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Name cannot be empty. Please try again.");
        }
    }

    private static void printTableLine() {
        System.out.println("+------+------------------------+----------+--------+--------+");
    }

    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}

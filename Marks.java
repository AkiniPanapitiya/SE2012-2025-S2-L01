import java.util.*;

public class Marks {
    static Scanner sc = new Scanner(System.in);
    static int n;
    static int[][] marks;

    public static void main(String[] args) {
        System.out.print("Enter number of students: ");
        while (true) {
            try {
                n = Integer.parseInt(sc.nextLine());
                if (n <= 0) throw new Exception();
                break;
            } catch (Exception e) {
                System.out.print("Invalid input. Enter a positive integer for number of students: ");
            }
        }

        marks = new int[n][3];
        for (int[] student : marks)
            Arrays.fill(student, -1); // mark as not entered

        printHelp(); // show examples at start

        while (true) {
            System.out.print("\nEnter command (type 'help' for options): ");
            String command = sc.nextLine().trim();
            String[] parts = command.split(" ");

            try {
                switch (parts[0].toLowerCase()) {
                    case "add":
                        if (parts.length != 2) {
                            System.out.println("Usage: add [studentID]  → e.g., add 1");
                            break;
                        }
                        addStudentMarks(Integer.parseInt(parts[1]));
                        break;
                    case "update":
                        if (parts.length != 3) {
                            System.out.println("Usage: update [studentID] [subjectID]  → e.g., update 1 2");
                            break;
                        }
                        updateStudentMark(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        break;
                    case "average_s":
                        if (parts.length != 2) {
                            System.out.println("Usage: average_s [studentID]  → e.g., average_s 1");
                            break;
                        }
                        studentAverage(Integer.parseInt(parts[1]));
                        break;
                    case "average":
                        if (parts.length != 2) {
                            System.out.println("Usage: average [subjectID]  → e.g., average 2");
                            break;
                        }
                        subjectAverage(Integer.parseInt(parts[1]));
                        break;
                    case "total":
                        if (parts.length != 2) {
                            System.out.println("Usage: total [studentID]  → e.g., total 3");
                            break;
                        }
                        studentTotal(Integer.parseInt(parts[1]));
                        break;
                    case "grades":
                        displayGrades();
                        break;
                    case "help":
                        printHelp();
                        break;
                    case "exit":
                        System.out.println("Exiting program.");
                        return;
                    default:
                        System.out.println("Invalid command. Type 'help' to see available commands.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input or data. Please try again.");
            }
        }
    }

    static void printHelp() {
        System.out.println("\n📘 Command Menu (Examples):");
        System.out.println(" add [studentID]         → e.g., add 1");
        System.out.println(" update [studentID] [subjectID] → e.g., update 1 2");
        System.out.println(" average_s [studentID]   → e.g., average_s 1");
        System.out.println(" average [subjectID]     → e.g., average 2");
        System.out.println(" total [studentID]       → e.g., total 3");
        System.out.println(" grades                  → View grade summary for all students");
        System.out.println(" help                    → Show this help menu");
        System.out.println(" exit                    → Quit the program\n");
    }

    static void addStudentMarks(int id) {
        if (!isValidID(id)) return;

        System.out.println("Enter marks for Student " + id + ":");
        marks[id - 1][0] = getValidatedMark("Mathematics");
        marks[id - 1][1] = getValidatedMark("Chemistry");
        marks[id - 1][2] = getValidatedMark("Physics");

        System.out.println("Marks added successfully.");
    }

    static void updateStudentMark(int id, int subjectID) {
        if (!isValidID(id) || subjectID < 1 || subjectID > 3) {
            System.out.println("Invalid student ID or subject ID.");
            return;
        }

        int newMark = getValidatedMark(getSubjectName(subjectID));
        marks[id - 1][subjectID - 1] = newMark;
        System.out.println("Mark updated successfully.");
    }

    static void studentAverage(int id) {
        if (!isValidID(id)) return;

        int[] m = marks[id - 1];
        if (isIncomplete(m)) {
            System.out.println("Marks not available for all subjects.");
            return;
        }

        double avg = (m[0] + m[1] + m[2]) / 3.0;
        System.out.printf("Average marks of student %d: %.2f\n", id, avg);
    }

    static void subjectAverage(int subjectID) {
        if (subjectID < 1 || subjectID > 3) {
            System.out.println("Invalid subject ID.");
            return;
        }

        int total = 0, count = 0;
        for (int[] m : marks) {
            if (m[subjectID - 1] != -1) {
                total += m[subjectID - 1];
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No marks entered for this subject.");
        } else {
            double avg = total / (double) count;
            System.out.printf("Average marks for %s: %.2f\n", getSubjectName(subjectID), avg);
        }
    }

    static void studentTotal(int id) {
        if (!isValidID(id)) return;

        int[] m = marks[id - 1];
        if (isIncomplete(m)) {
            System.out.println("Marks not available for all subjects.");
            return;
        }

        int total = m[0] + m[1] + m[2];
        System.out.printf("Total marks of student %d: %d\n", id, total);
    }

    static void displayGrades() {
        System.out.println("\nGrade Summary:");
        System.out.printf("%-10s %-12s %-12s %-12s\n", "StudentID", "Mathematics", "Chemistry", "Physics");

        for (int i = 0; i < n; i++) {
            int[] m = marks[i];
            System.out.printf("%-10d", i + 1);
            for (int j = 0; j < 3; j++) {
                if (m[j] == -1) {
                    System.out.printf("%-12s", "N/A");
                } else {
                    System.out.printf("%-12s", getGrade(m[j]));
                }
            }
            System.out.println();
        }
    }

    static int getValidatedMark(String subject) {
        int mark;
        while (true) {
            System.out.print("Enter " + subject + " mark (0-100): ");
            try {
                mark = Integer.parseInt(sc.nextLine());
                if (mark < 0 || mark > 100) throw new Exception();
                return mark;
            } catch (Exception e) {
                System.out.println("Invalid mark. Try again.");
            }
        }
    }

    static String getGrade(int score) {
        if (score >= 90) return "Grade A";
        else if (score >= 80) return "Grade B";
        else if (score >= 70) return "Grade C";
        else if (score >= 60) return "Grade D";
        else return "Fail";
    }

    static String getSubjectName(int id) {
        return switch (id) {
            case 1 -> "Mathematics";
            case 2 -> "Chemistry";
            case 3 -> "Physics";
            default -> "Unknown";
        };
    }

    static boolean isValidID(int id) {
        if (id < 1 || id > n) {
            System.out.println("Invalid student ID.");
            return false;
        }
        return true;
    }

    static boolean isIncomplete(int[] m) {
        return m[0] == -1 || m[1] == -1 || m[2] == -1;
    }
}

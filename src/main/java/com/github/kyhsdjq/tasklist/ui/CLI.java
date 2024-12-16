package com.github.kyhsdjq.tasklist.ui;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class CLI {
    /**
     *
     * @param question the question that will be printed
     * @param answers acceptable answers provided (no restriction if it's null)
     * @return the answer user printed
     */
    public static String askForString(String question, List<String> answers) {
        return askForString(question, answers, new Scanner(System.in));
    }

    public static String askForString(String question, List<String> answers, Scanner scanner) {
        String askString = question + " " + (answers != null ? answers : "");
        String warnString = "Please provide one of the acceptable answers: " + (answers != null ? answers : "");

        String response;
        while (true) {
            System.out.println(askString);
            response = scanner.nextLine().trim();

            if (answers == null || answers.contains(response)) {
                break;
            }
            else {
                System.out.println(warnString);
            }
        }

        return response;
    }

    /**
     *
     * @param question the question that will be printed
     * @return the answer user provided
     */
    public static int askForInt(String question) {
        return askForInt(question, new Scanner(System.in));
    }

    public static int askForInt(String question, Scanner scanner) {
        int response;

        while (true) {
            try {
                System.out.println(question);
                response = Integer.parseInt(scanner.nextLine().trim());
                break;
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        return response;
    }

    public static LocalDateTime askForLocalDateTime() {
        return askForLocalDateTime(new Scanner(System.in));
    }

    public static LocalDateTime askForLocalDateTime(Scanner scanner) {
        while (true) {
            try {
                int year = askForInt("Please enter the year:", scanner);
                int month = askForInt("Please enter the month:", scanner);
                int day = askForInt("Please enter the day:", scanner);
                int hour = askForInt("Please enter the hour:", scanner);
                int minute = askForInt("Please enter the minute:", scanner);

                return LocalDateTime.of(year, month, day, hour, minute);
            } catch (DateTimeException e) {
                System.out.println("Invalid date or time entered. Please try again.");
            }
        }
    }

    public static LocalDate askForLocalDate() {
        return askForLocalDate(new Scanner(System.in));
    }

    public static LocalDate askForLocalDate(Scanner scanner) {
        while (true) {
            try {
                int year = askForInt("Please enter the year:", scanner);
                int month = askForInt("Please enter the month:", scanner);
                int day = askForInt("Please enter the day:", scanner);

                return LocalDate.of(year, month, day);
            } catch (DateTimeException e) {
                System.out.println("Invalid date entered. Please try again.");
            }
        }
    }

    public static Duration askForDuration() {
        return askForDuration(new Scanner(System.in));
    }

    public static Duration askForDuration(Scanner scanner) {
        while (true) {
            try {
                int days = askForInt("Please enter the number of days:", scanner);
                int hours = askForInt("Please enter the number of hours:", scanner);
                int minutes = askForInt("Please enter the number of minutes:", scanner);

                return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes);
            } catch (DateTimeException e) {
                System.out.println("Invalid duration entered. Please try again.");
            }
        }
    }
}
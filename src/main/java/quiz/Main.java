package quiz;//package javamqa.demojmqa;
//
//import java.util.List;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//
//        System.out.println("Welcome to JavaQuizMQA!");
//
//        Scanner scanner = new Scanner(System.in);
//
//        // Retrieve and display available themes.
//        List<Theme> themes = Database.getAllThemes();
//        System.out.println("Available Themes:");
//        displayThemes(themes);
//
//        // Prompt the user to select a theme.
//        int selectedThemeId = getValidThemeId(scanner, themes);
//            if (selectedThemeId == -1) {
//                System.out.println("Invalid theme selection. Exiting...");
//                scanner.close();
//                return;
//            }
//
//            // Retrieve and display available subthemes for the selected theme.
////        List<SubTheme> subThemes = Database.getSubThemesForTheme(selectedThemeId);
////            System.out.println("Available Subthemes for Theme " + selectedThemeId + ":");
////            displaySubThemes(subThemes);
////
////            // Prompt the user to select a subtheme.
////        int selectedSubThemeId = getValidSubThemeId(scanner, subThemes);
////            if (selectedSubThemeId == -1) {
////                System.out.println("Invalid subtheme selection. Exiting...");
////                scanner.close();
////                return;
////            }
//
//        // Modify the code for displaying subthemes and getting user input
//        List<SubTheme> subThemes = Database.getSubThemesForTheme(selectedThemeId);
//        System.out.println("Available Subthemes for Theme " + selectedThemeId + ":");
//        displayIndexedSubThemes(subThemes); // Display subthemes with indexed numbers
//
//// Prompt the user to select a subtheme by index
//        int selectedSubThemeIndex = getValidSubThemeIndex(scanner, subThemes.size());
//        if (selectedSubThemeIndex == -1) {
//            System.out.println("Invalid subtheme selection. Exiting...");
//            scanner.close();
//            return;
//        }
//
//        //SubTheme selectedSubTheme = subThemes.get(selectedSubThemeIndex);
//        SubTheme selectedSubTheme = subThemes.get(selectedSubThemeIndex);
//
//
//            // Retrieve and display available questions for the selected subtheme.
//        List<Question> questions = Database.getQuestionsForSubTheme(selectedSubTheme.themeId());
//            // System.out.println("Available Questions for Subtheme " + selectedSubThemeId + ":");
//            //displayQuestions(questions);
//
//            int totalQuestions = questions.size();
//            int correctAnswers = 0;
//
//            // Ask each question one by one and get user's answer.
//        for (int i = 0; i < totalQuestions; i++) {
//            Question question = questions.get(i);
//            System.out.println("Question " + (i + 1) + ": " + question.getQuestionText());
//            String[] answers = question.getAnswers();
//            displayAnswers(answers);
//
//            String userAnswer = getUserAnswer(scanner);
//
//            if (userAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
//                correctAnswers++;
//                System.out.println("Correct!");
//            } else {
//                System.out.println("Wrong answer! The correct answer is " + question.getCorrectAnswer());
//            }
//
//            System.out.println("Explanation: " + question.getExplanation() + "\n");
//        }
//
//        // Display the final score.
//        System.out.println("Quiz Completed!");
//        System.out.println("Total Questions: " + totalQuestions);
//        System.out.println("Correct Answers: " + correctAnswers);
//        System.out.println("Incorrect Answers: " + (totalQuestions - correctAnswers));
//        double percentage = ((double) correctAnswers / totalQuestions) * 100;
//        System.out.printf("Percentage Score: %.2f%%", percentage);
//
//        scanner.close();
//    }
//
//    private static void displayThemes(List<Theme> themes) {
//        for (Theme theme : themes) {
//            System.out.println(theme.id() + ". " + theme.themeName());
//            System.out.println("   Explanation: " + theme.explanation() + "\n");
//        }
//    }
//
//    private static void displaySubThemes(List<SubTheme> subThemes) {
//        for (SubTheme subTheme : subThemes) {
//            System.out.println(subTheme.id() + ". " + subTheme.subThemeName());
//        }
//    }
//
//    // Method to display subthemes with indexed numbers
//    private static void displayIndexedSubThemes(List<SubTheme> subThemes) {
//        for (int i = 0; i < subThemes.size(); i++) {
//            SubTheme subTheme = subThemes.get(i);
//            System.out.println((i + 1) + ". " + subTheme.subThemeName());
//        }
//    }
//
//    // Method to get a valid subtheme index from user input
//    private static int getValidSubThemeIndex(Scanner scanner, int maxIndex) {
//        while (true) {
//            System.out.print("Enter subtheme number (1-" + maxIndex + "): ");
//            int selectedSubThemeIndex = scanner.nextInt();
//
//            if (selectedSubThemeIndex >= 1 && selectedSubThemeIndex <= maxIndex) {
//                return selectedSubThemeIndex - 1; // Adjust index for list access
//            }
//
//            System.out.println("Invalid subtheme number. Please choose a valid number.");
//        }
//    }
//
//
////    private static void displayQuestions(List<Question> questions) {
////        for (Question question : questions) {
////            System.out.println(question.getId() + ". " + question.getQuestionText());
////        }
////    }
//
//    private static void displayAnswers(String[] answers) {
//        char option = 'a';
//        for (String answer : answers) {
//            System.out.println(answer);
//            option++;
//        }
//    }
//
//    static String getUserAnswer(Scanner scanner) {
//        String userAnswer;
//        do {
//            System.out.print("Enter your answer (a, b, c, or d): ");
//            userAnswer = scanner.next().trim().toLowerCase();
//        } while (!userAnswer.matches("[a-d]"));
//        return userAnswer;
//    }
//
//    private static int getValidThemeId(Scanner scanner, List<Theme> themes) {
//        while (true) {
//            System.out.print("Enter theme ID: ");
//            int selectedThemeId = scanner.nextInt();
//
//            for (Theme theme : themes) {
//                if (theme.id() == selectedThemeId) {
//                    return selectedThemeId;
//                }
//            }
//
//            System.out.println("Invalid theme ID. Please choose a valid theme.");
//            displayThemes(themes);
//        }
//    }
//
////    private static int getValidSubThemeId(Scanner scanner, List<SubTheme> subThemes) {
////        while (true) {
////            System.out.print("Enter subtheme ID: ");
////            int selectedSubThemeId = scanner.nextInt();
////
////            for (SubTheme subTheme : subThemes) {
////                if (subTheme.getId() == selectedSubThemeId) {
////                    return selectedSubThemeId;
////                }
////            }
////
////            System.out.println("Invalid subtheme ID. Please choose a valid subtheme.");
////            displaySubThemes(subThemes);
////        }
////    }
//
//}
//
//

package quiz;//package javamqa.demojmqa;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//import java.util.List;
//
//public class JavaQuizFXCss extends Application {
//    private List<Theme> themes;
//    private List<SubTheme> subThemes;
//    private List<Question> questions;
//    private int correctAnswers = 0;
//
//    private Stage primaryStage;
//    private BorderPane mainLayout;
//    private VBox questionLayout;
//
//    private Button finishButton;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        this.primaryStage = primaryStage;
//        this.primaryStage.setTitle("JavaQuizFX");
//
//        initMainLayout();
//
//        Scene scene = new Scene(mainLayout, 800, 600);
//
//        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        showThemeSelection();
//    }
//
//    private void initMainLayout() {
//        mainLayout = new BorderPane();
//        mainLayout.setPadding(new Insets(10));
//
//    }
//
//    private void showThemeSelection() {
//        themes = Database.getAllThemes();
//
//        VBox themeBox = new VBox();
//        themeBox.setAlignment(Pos.CENTER);
//        themeBox.setSpacing(20);
//
//        Label titleLabel = new Label("Select a Theme:");
//        themeBox.getChildren().add(titleLabel);
//
//        ToggleGroup themeToggleGroup = new ToggleGroup();
//
//        for (Theme theme : themes) {
//            RadioButton themeButton = new RadioButton(theme.themeName());
//            themeButton.setToggleGroup(themeToggleGroup);
//            themeBox.getChildren().add(themeButton);
//        }
//
//        Button startButton = new Button("Start Quiz");
//        startButton.setOnAction(event -> {
//            int selectedThemeId = getSelectedThemeId(themeToggleGroup);
//            if (selectedThemeId != -1) {
//                showSubThemeSelection(selectedThemeId);
//            }
//        });
//
//        themeBox.getChildren().add(startButton);
//        mainLayout.setCenter(themeBox);
//    }
//
//    private int getSelectedThemeId(ToggleGroup toggleGroup) {
//        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
//        if (selectedRadioButton != null) {
//            String selectedThemeName = selectedRadioButton.getText();
//            for (Theme theme : themes) {
//                if (theme.themeName().equals(selectedThemeName)) {
//                    return theme.id();
//                }
//            }
//        }
//        return -1;
//    }
//
//    private void showSubThemeSelection(int selectedThemeId) {
//        subThemes = Database.getSubThemesForTheme(selectedThemeId);
//
//        VBox subThemeBox = new VBox();
//        subThemeBox.setAlignment(Pos.CENTER);
//        subThemeBox.setSpacing(20);
//
//        Label titleLabel = new Label("Select a Sub theme:");
//        subThemeBox.getChildren().add(titleLabel);
//
//        ToggleGroup subThemeToggleGroup = new ToggleGroup();
//
//        for (SubTheme subTheme : subThemes) {
//            RadioButton subThemeButton = new RadioButton(subTheme.subThemeName());
//            subThemeButton.setToggleGroup(subThemeToggleGroup);
//            subThemeBox.getChildren().add(subThemeButton);
//        }
//
//        Button startButton = new Button("Start Quiz");
//        startButton.setOnAction(event -> {
//            int selectedSubThemeId = getSelectedSubThemeId(subThemeToggleGroup);
//            if (selectedSubThemeId != -1) {
//                questions = Database.getQuestionsForSubTheme(selectedSubThemeId);
//                correctAnswers = 0; // Reset correct answers count
//                showQuiz();
//            }
//        });
//
//        subThemeBox.getChildren().add(startButton);
//        mainLayout.setCenter(subThemeBox);
//    }
//
//    private int getSelectedSubThemeId(ToggleGroup toggleGroup) {
//        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
//        if (selectedRadioButton != null) {
//            String selectedSubThemeName = selectedRadioButton.getText();
//            for (SubTheme subTheme : subThemes) {
//                if (subTheme.subThemeName().equals(selectedSubThemeName)) {
//                    return subTheme.id();
//                }
//            }
//        }
//        return -1;
//    }
//
//    private void showQuiz() {
//        questionLayout = new VBox();
//        questionLayout.setAlignment(Pos.TOP_CENTER);
//        questionLayout.setSpacing(25);
//        questionLayout.setPadding(new Insets(15));
//
//        ScrollPane scrollPane = new ScrollPane(questionLayout);
//        scrollPane.setFitToWidth(true);
//
//        for (int i = 0; i < questions.size(); i++) {
//            Question question = questions.get(i);
//            VBox questionBox = createQuestionBox(i + 1, question);
//            questionLayout.getChildren().add(questionBox);
//        }
//
//        finishButton = new Button("Finish");
//        finishButton.setOnAction(event -> showResult());
//        finishButton.setStyle("-fx-background-color: #36A9AE; -fx-text-fill: white;");
//
//        VBox quizLayout = new VBox(scrollPane, finishButton);
//        mainLayout.setCenter(quizLayout);
//
//    }
//
//    private VBox createQuestionBox(int questionNumber, Question question) {
//        VBox questionBox = new VBox();
//        questionBox.setAlignment(Pos.TOP_LEFT);
//        questionBox.setSpacing(5);
//
//        Label questionLabel = new Label(questionNumber + ". " + question.getQuestionText());
//        questionBox.getChildren().add(questionLabel);
//
//        ToggleGroup answerToggleGroup = new ToggleGroup();
//
//        String[] answers = question.getAnswers();
//        for (int i = 0; i < answers.length; i++) {
//            RadioButton answerButton = new RadioButton(answers[i]);
//            answerButton.setToggleGroup(answerToggleGroup);
//            answerButton.setUserData(i);
//            questionBox.getChildren().add(answerButton);
//        }
//
//        Button submitButton = new Button("Submit");
//        submitButton.setOnAction(event -> {
//            int selectedAnswerIndex = (int) answerToggleGroup.getSelectedToggle().getUserData();
//            checkAnswer(question, selectedAnswerIndex, questionBox, submitButton);
//        });
//        // submitButton.setStyle("-fx-background-color: #1899D6; -fx-text-fill: white;");
//
//        questionBox.getChildren().add(submitButton);
//        return questionBox;
//    }
//
//
//    private void checkAnswer(Question question, int selectedAnswerIndex, VBox questionBox, Button submitButton) {
//        String[] answers = question.getAnswers();  // No need for correctAnswers here
//        String selectedAnswer = answers[selectedAnswerIndex];
//        String correctAnswer = question.getCorrectAnswerText();
//
//        Label resultLabel = new Label();
//        if (selectedAnswer.contains(correctAnswer)) {
//            resultLabel.setText("Correct!");
//            resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
//            correctAnswers++;
//        } else {
//            resultLabel.setText("Wrong answer! The correct answer is " + question.getCorrectAnswer());
//            resultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
//        }
//
//        //resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
//
//        Label explanationLabel = new Label("Explanation: " + question.getExplanation());
//        explanationLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
//
//        questionBox.getChildren().addAll(resultLabel, explanationLabel);
//
//        // Disable the submit button to prevent further submissions
//        submitButton.setDisable(true);
//
//        if (correctAnswers + (questions.size() - questionLayout.getChildren().size()) == questions.size()) {
//            showResult();
//        }
//    }
//
//    private void showResult() {
//        VBox resultBox = getvBox();
//
//        // Create a new stage for the results window
//        Stage resultsStage = new Stage();
//        resultsStage.setTitle("Quiz Results");
//
//        BorderPane resultsLayout = new BorderPane();
//        resultsLayout.setCenter(resultBox);
//
//        Button newQuizButton = new Button("Start New Quiz");
//        Button exitButton = new Button("Exit");
//
//        HBox buttonBox = new HBox(10, newQuizButton, exitButton);
//        buttonBox.setAlignment(Pos.CENTER);
//        buttonBox.setPadding(new Insets(20));
//
//        resultsLayout.setBottom(buttonBox);
//
//        Scene resultsScene = new Scene(resultsLayout, 400, 300);
//        resultsStage.setScene(resultsScene);
//
//        // Hide the finish button after displaying results
//        finishButton.setVisible(false);
//
//        // Show the results window
//        resultsStage.show();
//
//        // Set action for the "Start New Quiz" button
//        newQuizButton.setOnAction(event -> {
//            resultsStage.close(); // Close the results window
//            resetQuiz(); // Reset the quiz
//        });
//
//        // Set action for the "Exit" button
//        exitButton.setOnAction(event -> {
//            resultsStage.close(); // Close the results window
//            primaryStage.close(); // Close the main application window
//        });
//
//    }
//
//    private VBox getvBox() {
//        Label resultLabel = new Label("Quiz Finished!");
//        resultLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
//
//        Label correctLabel = new Label("Correct Answers: " + correctAnswers);
//        Label wrongLabel = new Label("Wrong Answers: " + (questions.size() - correctAnswers));
//        double percentage = ((double) correctAnswers / questions.size()) * 100;
//        Label percentageLabel = new Label("Percentage Score: " + String.format("%.2f", percentage) + "%");
//
//        VBox resultBox = new VBox(20, resultLabel, correctLabel, wrongLabel, percentageLabel);
//        resultBox.setAlignment(Pos.CENTER);
//        return resultBox;
//    }
//
//    private void resetQuiz() {
//        mainLayout.getChildren().clear(); // Clear the current content
//
//        showThemeSelection(); // Show the theme selection screen again
//    }
//
//
//}
//

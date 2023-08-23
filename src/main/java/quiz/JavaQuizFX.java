package quiz;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class JavaQuizFX extends Application {
    private List<Theme> themes;
    private List<SubTheme> subThemes;
    private List<Question> questions;
    private int correctAnswers = 0;

    private Stage primaryStage;
    private BorderPane mainLayout;
    private VBox questionLayout;
    private Button finishButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JavaQuizFX");

        initMainLayout();

        Scene scene = new Scene(mainLayout, 1024, 760);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        showThemeSelection();
    }

    private void initMainLayout() {
        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));
    }


    private void showThemeSelection() {
        themes = Database.getAllThemes();

        VBox themeBox = new VBox();
        themeBox.setAlignment(Pos.CENTER);
        themeBox.getStyleClass().add("vbox-theme-selection");

        Label welcomeLabel = new Label("Welcome to Java Quiz!");
        welcomeLabel.getStyleClass().add("label-welcome");
        themeBox.getChildren().add(welcomeLabel);

        Label titleLabel = new Label("Select a Theme:");
        titleLabel.getStyleClass().add("label-title");
        themeBox.getChildren().add(titleLabel);

        ToggleGroup themeToggleGroup = new ToggleGroup();

        for (Theme theme : themes) {
            RadioButton themeButton = new RadioButton(theme.themeName());
            themeButton.setToggleGroup(themeToggleGroup);
            themeBox.getChildren().add(themeButton);
        }

        Button startButton = new Button("Start Quiz");
        startButton.getStyleClass().add("button-start-quiz");
        startButton.setOnAction(event -> {
            int selectedThemeId = getSelectedThemeId(themeToggleGroup);
            if (selectedThemeId != -1) {
                showSubThemeSelection(selectedThemeId);
            }
        });

        themeBox.getChildren().add(startButton);
        mainLayout.setCenter(themeBox);
    }

    private void showSubThemeSelection(int selectedThemeId) {
        subThemes = Database.getSubThemesForTheme(selectedThemeId);

        VBox subThemeBox = new VBox();
        subThemeBox.setAlignment(Pos.CENTER);
        subThemeBox.getStyleClass().add("vbox-sub-theme-selection");

        Label titleLabel = new Label("Select a Sub theme:");
        titleLabel.getStyleClass().add("label-title");
        subThemeBox.getChildren().addAll(titleLabel);

        ToggleGroup subThemeToggleGroup = new ToggleGroup();

        for (SubTheme subTheme : subThemes) {
            RadioButton subThemeButton = new RadioButton(subTheme.subThemeName());
            subThemeButton.setToggleGroup(subThemeToggleGroup);
            subThemeBox.getChildren().add(subThemeButton);
        }

        Button startButton = new Button("Start Quiz");
        startButton.getStyleClass().add("button-start-quiz");
        startButton.setOnAction(event -> {
            int selectedSubThemeId = getSelectedSubThemeId(subThemeToggleGroup);
            if (selectedSubThemeId != -1) {
                questions = Database.getQuestionsForSubTheme(selectedSubThemeId);
                correctAnswers = 0; // Reset correct answers count
                showQuiz();
            }
        });

        subThemeBox.getChildren().addAll(startButton);
        mainLayout.setCenter(subThemeBox);
    }

    private int getSelectedThemeId(ToggleGroup toggleGroup) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedThemeName = selectedRadioButton.getText();
            for (Theme theme : themes) {
                if (theme.themeName().equals(selectedThemeName)) {
                    return theme.id();
                }
            }
        }
        return -1;
    }

    private int getSelectedSubThemeId(ToggleGroup toggleGroup) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedSubThemeName = selectedRadioButton.getText();
            for (SubTheme subTheme : subThemes) {
                if (subTheme.subThemeName().equals(selectedSubThemeName)) {
                    return subTheme.id();
                }
            }
        }
        return -1;
    }


    private void showQuiz() {
        questionLayout = new VBox();
        questionLayout.setAlignment(Pos.TOP_CENTER);
        questionLayout.setSpacing(10);
        questionLayout.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(questionLayout);
        scrollPane.setFitToWidth(true);

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            VBox questionBox = createQuestionBox(i + 1, question);
            questionLayout.getChildren().add(questionBox);
        }

        finishButton = new Button("Finish");
        finishButton.getStyleClass().add("button-finish");
        finishButton.setOnAction(event -> showResult());

        VBox quizLayout = new VBox(scrollPane, finishButton);
        quizLayout.getStyleClass().add("vbox-quiz");
        mainLayout.setCenter(quizLayout);
    }

    private VBox createQuestionBox(int questionNumber, Question question) {
        VBox questionBox = new VBox();
        questionBox.setAlignment(Pos.TOP_LEFT);
        questionBox.setSpacing(5);
        questionBox.getStyleClass().add("vbox-question");

        Label questionLabel = new Label(questionNumber + ". " + question.getQuestionText());
        questionLabel.getStyleClass().add("label-question");
        questionBox.getChildren().add(questionLabel);

        ToggleGroup answerToggleGroup = new ToggleGroup();

        String[] answers = question.getAnswers();
        for (int i = 0; i < answers.length; i++) {
            RadioButton answerButton = new RadioButton(answers[i]);
            answerButton.setToggleGroup(answerToggleGroup);
            answerButton.setUserData(i);
            answerButton.getStyleClass().add("radio-button-answer");
            questionBox.getChildren().add(answerButton);
        }

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("button-submit");
        submitButton.setOnAction(event -> {
            int selectedAnswerIndex = (int) answerToggleGroup.getSelectedToggle().getUserData();
            checkAnswer(question, selectedAnswerIndex, questionBox, submitButton);
        });

        questionBox.getChildren().add(submitButton);
        return questionBox;
    }

    private void checkAnswer(Question question, int selectedAnswerIndex, VBox questionBox, Button submitButton) {
        String[] answers = question.getAnswers();
        String selectedAnswer = answers[selectedAnswerIndex];
        String correctAnswer = question.getCorrectAnswerText();

        Label resultLabel = new Label();

        if (selectedAnswer.contains(correctAnswer)) {
            resultLabel.setText("Correct!");
            resultLabel.getStyleClass().add("label-correct");
            correctAnswers++;
        } else {
            resultLabel.setText("Wrong answer! The correct answer is " + question.getCorrectAnswer());
            resultLabel.getStyleClass().add("label-wrong");
        }

        Label explanationLabel = new Label("Explanation: " + question.getExplanation());
        explanationLabel.getStyleClass().add("label-explanation");

        questionBox.getChildren().addAll(resultLabel, explanationLabel);

        submitButton.setDisable(true);

        if (correctAnswers + (questions.size() - questionLayout.getChildren().size()) == questions.size()) {
            showResult();
        }
    }


    private void showResult() {
        VBox resultBox = getvBox();

        Stage resultsStage = new Stage();
        resultsStage.setTitle("Quiz Results");

        BorderPane resultsLayout = new BorderPane();
        resultsLayout.setCenter(resultBox);

        Button newQuizButton = new Button("Start New Quiz");
        newQuizButton.getStyleClass().add("button-start-new-quiz");
        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("button-exit");


        HBox buttonBox = new HBox(10, newQuizButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getStyleClass().add("button-box");

        resultsLayout.setBottom(buttonBox);

        Scene resultsScene = new Scene(resultsLayout, 400, 300);

        resultsScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        resultsStage.setScene(resultsScene);

        finishButton.setVisible(false);

        resultsStage.show();

        newQuizButton.setOnAction(event -> {
            resultsStage.close();
            resetQuiz();
        });

        exitButton.setOnAction(event -> {
            resultsStage.close();
            primaryStage.close();
        });

    }

    private VBox getvBox() {
        Label resultLabel = new Label("Quiz Finished!");
        resultLabel.getStyleClass().add("label-result-title");

        Label correctLabel = new Label("Correct Answers: " + correctAnswers);
        Label wrongLabel = new Label("Wrong Answers: " + (questions.size() - correctAnswers));
        double percentage = ((double) correctAnswers / questions.size()) * 100;
        Label percentageLabel = new Label("Percentage Score: " + String.format("%.2f", percentage) + "%");
        correctLabel.getStyleClass().add("label-score");
        wrongLabel.getStyleClass().add("label-score");
        percentageLabel.getStyleClass().add("label-score");

        VBox resultBox = new VBox(resultLabel, correctLabel, wrongLabel, percentageLabel);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.getStyleClass().add("vbox-result");
        return resultBox;
    }


    private void resetQuiz() {
        mainLayout.getChildren().clear();
        showThemeSelection();
    }


}



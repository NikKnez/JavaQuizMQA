package quiz;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*

USE JavaQuizMQA;

CREATE TABLE themes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    theme_name VARCHAR(255) NOT NULL
    theme_explanations VARCHAR(255) NOT NULL
);

CREATE TABLE subthemes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    theme_id INT,
    subtheme_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (theme_id) REFERENCES themes (id)
);

CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    theme_id INT,
    subtheme_id INT,
    question_number INT,
    question_text TEXT NOT NULL,
    correct_answer CHAR(1) NOT NULL,
    explanation TEXT NOT NULL,
    FOREIGN KEY (theme_id) REFERENCES themes (id),
    FOREIGN KEY (subtheme_id) REFERENCES subthemes (id)
);

CREATE TABLE answers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT,
    answer_text VARCHAR(255) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions (id)
);
 */

/*
INSERT INTO themes (theme_name, theme_explanations) VALUES ('Java Data Types, Variables, and Arrays',
'The section contains Java multiple choice questions and answers on integer, character, floating and boolean data types,
variables, type casting and conversions and properties of arrays.');
INSERT INTO subthemes (theme_id, subtheme_name) VALUES (1, 'Java Integer and Floating Data Types');
INSERT INTO questions (theme_id, subtheme_id, question_number, question_text, correct_answer, explanation)
VALUES (1, 1, 1, 'What is the range of short data type in Java?', 'b', 'Short occupies 16 bits in memory. Its range is from -32768 to 32767.');

INSERT INTO answers (question_id, answer_text) VALUES (1, 'a) -128 to 127');
INSERT INTO answers (question_id, answer_text) VALUES (1, 'b) -32768 to 32767');
INSERT INTO answers (question_id, answer_text) VALUES (1, 'c) -2147483648 to 2147483647');
INSERT INTO answers (question_id, answer_text) VALUES (1, 'd) None of the mentioned');

 */

public class Database {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/JavaQuizMQA";
    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASS");

    private static final Properties props = new Properties();

    static {
        try (InputStream inputStream = Database.class.getResourceAsStream("/config.properties")) {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.pass");
        return DriverManager.getConnection(DB_URL, user, pass);
    }

    public static List<Theme> getAllThemes() {
        List<Theme> themes = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT id, theme_name, theme_explanations FROM themes")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String themeName = resultSet.getString("theme_name");
                String explanation = resultSet.getString("theme_explanations"); // Get theme explanation for each theme
                Theme theme = new Theme(id, themeName, explanation);
                themes.add(theme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }

    public static List<SubTheme> getSubThemesForTheme(int themeId) {
        List<SubTheme> subThemes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, subtheme_name FROM subthemes WHERE theme_id = ?")) {
            stmt.setInt(1, themeId);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String subThemeName = resultSet.getString("subtheme_name");
                SubTheme subTheme = new SubTheme(id, themeId, subThemeName);
                subThemes.add(subTheme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subThemes;
    }

    public static List<Question> getQuestionsForSubTheme(int subThemeId) {
        List<Question> questions = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, theme_id, question_number, question_text, correct_answer, explanation " +
                             "FROM questions WHERE subtheme_id = ?")) {
            stmt.setInt(1, subThemeId);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int themeId = resultSet.getInt("theme_id");
                int questionNumber = resultSet.getInt("question_number");
                String questionText = resultSet.getString("question_text");
                String correctAnswer = resultSet.getString("correct_answer");
                String explanation = resultSet.getString("explanation");

                String[] answers = getAnswersForQuestion(id);

                Question question = new Question(id, themeId, subThemeId, questionNumber, questionText, answers, correctAnswer, explanation);
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public static String[] getAnswersForQuestion(int questionId) {
        List<String> answersList = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT answer_text FROM answers WHERE question_id = ?")) {
            stmt.setInt(1, questionId);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String answerText = resultSet.getString("answer_text");
                answersList.add(answerText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answersList.toArray(new String[0]);
    }

    public static String getThemeExplanation(int themeId) {
        String explanation = "";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT explanation FROM theme_explanations WHERE theme_id = ?")) {
            stmt.setInt(1, themeId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                explanation = resultSet.getString("explanation");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return explanation;
    }
}

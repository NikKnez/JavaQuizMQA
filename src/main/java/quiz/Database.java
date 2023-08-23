package quiz;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

package quiz;

public class Question {
    private final int id;
//    private final int themeId;
//    private final int subThemeId;
//    private final int questionNumber;
    private final String questionText;
    private final String[] answers;
    private final String correctAnswer;
    private final String explanation;

    public Question(int id, int themeId, int subThemeId, int questionNumber, String questionText, String[] answers, String correctAnswer, String explanation) {
        this.id = id;
//        this.themeId = themeId;
//        this.subThemeId = subThemeId;
//        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public int getId() {
        return id;
    }

//    public int getThemeId() {
//        return themeId;
//    }
//
//    public int getSubThemeId() {
//        return subThemeId;
//    }
//
//    public int getQuestionNumber() {
//        return questionNumber;
//    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getCorrectAnswerText() {
        return correctAnswer;
    }

//    public String getThemeName() {
//        // You can call the Database.getThemeById() method to fetch the theme name from the database based on themeId.
//        Theme theme = Database.getThemeById(themeId);
//        return theme != null ? theme.getThemeName() : "Unknown Theme";
//    }
//
//    public String getSubThemeName() {
//        // You can call the Database.getSubThemeById() method to fetch the subtheme name from the database based on subThemeId.
//        SubTheme subTheme = Database.getSubThemeById(subThemeId);
//        return subTheme != null ? subTheme.getSubThemeName() : "Unknown Subtheme";
//    }
}



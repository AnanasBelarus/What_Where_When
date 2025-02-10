package models;

import java.io.Serializable;

public class AbstractQuestion implements Serializable {
    private int id;
    private String question;
    private String answer;
    private int value;
    private int penalty;  // По умолчанию значение штрафа равно 0
    private String author;
    private String altAnswers;
    private String comment;
    private String source;

    // Приватный конструктор для использования строителем
    private AbstractQuestion(Builder builder) {
        this.id = builder.id;
        this.question = builder.question;
        this.answer = builder.answer;
        this.value = builder.value;
        this.penalty = builder.penalty;
        this.author = builder.author;
        this.altAnswers = builder.altAnswers;
        this.comment = builder.comment;
        this.source = builder.source;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getSource() {
        return source;
    }

    public String getComment() {
        return comment;
    }

    public String getAltAnswers() {
        return altAnswers;
    }

    public String getAuthor() {
        return author;
    }

    public int getPenalty() {
        return penalty;
    }

    public int getValue() {
        return value;
    }

    public int checkAnswer(String answer) {


        if (this.answer.equalsIgnoreCase(answer)) {
            return this.value;
        } else {
            return penalty;
        }
    }
    // Внутренний класс Builder
    public static class Builder {
        private int id;
        private String question;
        private String answer;
        private int value;
        private int penalty = 0;  // По умолчанию значение штрафа равно 0
        private String author;
        private String altAnswers;
        private String comment;
        private String source;


        // Метод для установки идентификатора вопроса
        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        // Метод для установки вопроса
        public Builder setQuestion(String question) {
            this.question = question;
            return this;
        }

        // Метод для установки ответа
        public Builder setAnswer(String answer) {
            if (answer.startsWith("Ответ:")){
                answer = answer.replace("Ответ:","").trim();
            }
            this.answer = answer;
            return this;
        }

        // Метод для установки стоимости вопроса
        public Builder setValue(int value) {
            this.value = value;
            return this;
        }

        // Метод для установки штрафа (необязательный)
        public Builder setPenalty(int penalty) {
            this.penalty = penalty;
            return this;
        }

        public String getAltAnswers() {
            return altAnswers;
        }

        public void setAltAnswers(String altAnswers) {
            this.altAnswers = altAnswers;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        // Метод для создания экземпляра AbstractQuestion
        public AbstractQuestion build() {
            // Проверка обязательных полей перед созданием объекта
            if (question == null || answer == null) {
                throw new IllegalStateException("Question, answer and value must be set properly.");
            }
            if (value<=0){
                this.setValue(1);
            }
            return new AbstractQuestion(this);
        }
    }
}

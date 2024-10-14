package models;

public class AbstractQuestion {
    private int id;
    private final String question;
    private final String answer;
    private final int value;
    private int penalty = 0;

    // Приватный конструктор для использования строителем
    private AbstractQuestion(Builder builder) {
        this.id = builder.id;
        this.question = builder.question;
        this.answer = builder.answer;
        this.value = builder.value;
        this.penalty = builder.penalty;
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

    public int checkAnswer(String answer) {
        if (this.answer.equals(answer)) {
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

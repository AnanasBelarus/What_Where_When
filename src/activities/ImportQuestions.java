package activities;

import models.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ImportQuestions {
    private static Scanner scanner = new Scanner(System.in);
    private static String DEFAULT_PATH = "src/championships/";

    private static String parseSection(BufferedReader reader, String line, String keyword, Consumer<String> setter) throws IOException {
        if (line != null && line.toLowerCase().replaceAll(" ", "").startsWith(keyword)) {
            StringBuilder currentValue = new StringBuilder(line);
            line = reader.readLine();
            while (line != null && !isKeyword(line)) {
                currentValue.append(line);
                line = reader.readLine();
            }  // считываем значение полностью
            setter.accept(currentValue.toString());  // устанавливаем значение с помощью переданного сеттера
        }
        return line;
    }

    private static boolean isKeyword(String line) {
        String trimmedLine = line.toLowerCase().replaceAll(" ", "").trim();
        return trimmedLine.startsWith("чемпионат:") || trimmedLine.startsWith("дата:") ||
                trimmedLine.startsWith("редактор:") || trimmedLine.startsWith("инфо:") ||
                trimmedLine.startsWith("тур:") || trimmedLine.startsWith("вопрос") ||
                trimmedLine.startsWith("ответ:") || trimmedLine.startsWith("зачёт:") ||
                trimmedLine.startsWith("автор:") || trimmedLine.startsWith("комментарий:") ||
                trimmedLine.startsWith("источник:");
    }

    private static File pickDirectory(String dir) throws Exception {
        System.out.println("Введите путь к директории (по умолчанию " + dir + "):");
        String dirPath = scanner.nextLine();
        if (dirPath == null || dirPath.equals("")) {
            dirPath = dir;
        }
        File directory = new File(dirPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new Exception("Директория не найдена или не является директорией.");
        }
        return directory;
    }

    private static File pickFile(File dir) throws Exception {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            throw new Exception("Директория пуста.");
        }

        // Шаг 3: Вывод списка файлов
        System.out.println("Список файлов в директории:");
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println((i + 1) + ": " + files[i].getName());
            }
        }

        // Шаг 4: Выбор файла из списка
        System.out.println("Введите номер файла, который хотите открыть:");
        int fileIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // обработка переноса строки после nextInt()

        if (fileIndex < 0 || fileIndex >= files.length || !files[fileIndex].isFile()) {
            throw new Exception("Неверный выбор файла.");
        }
        return files[fileIndex];
    }


    public static Championship getChampionshipFromFile() throws ClassNotFoundException, IOException, Exception {
        File selectedFile = pickFile(pickDirectory(DEFAULT_PATH));
        System.out.println("Вы выбрали файл: " + selectedFile.getName());

        ObjectInputStream oos = new ObjectInputStream(new FileInputStream(selectedFile.getAbsolutePath()));
        Championship championship = (Championship) oos.readObject();
        oos.close();
        return championship;
    }

    public static Championship ImportQuestionsFromTXT() throws Exception {
        File selectedFile = pickFile(pickDirectory("src/import_dir/"));
        System.out.println("Вы выбрали файл: " + selectedFile.getName());
        // Шаг 5: Выбор кодировки
        System.out.println("Введите кодировку файла (например, UTF-8 или Windows-1251, по умолчанию KOI8-U):");
        String encoding = scanner.nextLine();
        if (encoding == null || encoding.equals("") || !Charset.isSupported(encoding)) {
            encoding = "KOI8-U";
        }
        Charset charset = Charset.forName(encoding);
        // Шаг 6: Чтение и вывод содержимого файла
        BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath(), charset));
        List<AbstractQuestion> questions = null;
        AbstractQuestion.Builder currentQuestion = null;
        AtomicReference<String> championshipName = new AtomicReference<>();
        AtomicReference<String> date = new AtomicReference<>();
        AtomicReference<String> editor = new AtomicReference<>();
        AtomicReference<String> about = new AtomicReference<>();
        ArrayList<Round> roundList = new ArrayList<>();
        AtomicReference<String> roundTitle = new AtomicReference<>();

        String line = reader.readLine();

//
        while (line != null) {
            line = parseSection(reader, line, "чемпионат:", championshipName::set);
            line = parseSection(reader, line, "дата:", date::set);
            line = parseSection(reader, line, "редактор:", editor::set);
            line = parseSection(reader, line, "инфо:", about::set);
            // Обрабатываем вопрос и создаём новый, если найдено ключевое слово
            if (line != null && line.toLowerCase().replaceAll(" ", "").startsWith("тур:")) {
                if (questions != null) roundList.add(new Round(questions, roundList.size() + 1, roundTitle.get()));
                questions = new ArrayList<>();
                line = parseSection(reader, line, "тур:", roundTitle::set);
            }

            // Обрабатываем вопрос и создаём новый, если найдено ключевое слово
            if (line != null && line.toLowerCase().replaceAll(" ", "").startsWith("вопрос")) {
                if (currentQuestion != null) {
                    AbstractQuestion question = currentQuestion.build();
                    System.out.println(question.getQuestion());
                    questions.add(currentQuestion.build());
                }
                currentQuestion = new AbstractQuestion.Builder();
                line = parseSection(reader, line, "вопрос", currentQuestion::setQuestion);
            }

            line = parseSection(reader, line, "ответ:", currentQuestion::setAnswer);
            line = parseSection(reader, line, "зачет:", currentQuestion::setAltAnswers);
            line = parseSection(reader, line, "комментарий:", currentQuestion::setComment);
            line = parseSection(reader, line, "источник:", currentQuestion::setSource);
            line = parseSection(reader, line, "автор:", currentQuestion::setAuthor);
        }
        if (currentQuestion != null) questions.add(currentQuestion.build());
        if (questions != null) roundList.add(new Round(questions, roundList.size() + 1, roundTitle.get()));
        System.out.println("Запись в файл");
        System.out.println("Введите путь к директории (по умолчанию " + DEFAULT_PATH + "):");
        String dir = scanner.nextLine();
        if (dir == null || dir.equals("")) {
            dir = DEFAULT_PATH;
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dir +
                championshipName.get().replaceAll(":", "").replaceAll("\"", "")
                        .replace("\\", "") + ".ser"));
        Championship res = new Championship(roundList, championshipName.get(), about.get(), date.get(), editor.get());
        oos.writeObject(res);
        oos.close();
        return res;
    }

    public static void runSimpleGame() {
        // Шаг 1: Ввод пути к директории
        System.out.println("Введите название команды (по умолчанию Player):");
        String teamName = scanner.nextLine();
        if (teamName == null || teamName.equals("")) {
            teamName = "Player";
        }

        TeamListener player = new TeamListener(teamName);
        try {
            Organizer organizer = (new Organizer.Builder()).setChampionship(ImportQuestions.getChampionshipFromFile()).build();
            organizer.addTeamListener(player);
            organizer.startGame();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

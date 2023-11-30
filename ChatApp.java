package task2;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatApp {

    private static final String USER_DATA_FILE = "user_data.txt";
    private static final String MESSAGES_FILE = "messages.txt";
    private static final List<User> users = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Scanner scanNum = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static ArrayList<Message> messages = new ArrayList<>();
    // Logger initialization
    private static final Logger logger = Logger.getLogger(ChatApp.class.getName());

    static {
        // ConsoleHandler to display log messages on the console
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);

        // FileHandler to save log messages to a file
        try {
            FileHandler fileHandler = new FileHandler("chat_app.log");
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setting the log level for the logger
        logger.setLevel(Level.ALL);
    }

    public static void main(String[] args) {
        loadUserData();
        loadMessages();

//        boolean isUserIn = false;
        User currentUser = null;

        while(currentUser==null){
            System.out.println("\nChat App");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Select option: ");

            int choice = scanNum.nextInt();
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    currentUser = loginUser();
                    break;


                case 3:
                    saveUserData();
                    logger.info("Exiting Chat App. Goodbye!");
                    System.exit(0);
                default:
                    logger.warning("Invalid option! Please try again.");
            }
        }

        while (true) {
            System.out.println("\nChat App");

            System.out.println("1. Inbox");
            System.out.println("2. Exit");

            System.out.println("Select option: ");

            int choice = scanNum.nextInt();
//            scanNum.nextLine(); // Consume newline

            switch (choice) {

                case 1:
                    sendAndReceiveMessages();
                    break;

                case 2:
                    saveUserData();
                    saveMessages();
                    logger.info("Exiting Chat App. Goodbye!");
                    System.exit(0);
                default:
                    logger.warning("Invalid option! Please try again.");
            }
        }
    }

    private static void registerUser() {
        logger.info("Enter your email: ");
        String email = scanner.nextLine();
        if (!isValidEmail(email)) {
            logger.warning("Invalid email format! Please try again.");
            return;
        }

        if (userExists(email)) {
            logger.warning("User with this email already exists! Please log in or use a different email.");
            return;
        }

        logger.info("Enter your username: ");
        String username = scanner.nextLine();

        logger.info("Enter your password: ");
        String password = scanner.nextLine();

        User newUser = new User(email, username, password);
        users.add(newUser);
        logger.info("Registration successful! Welcome, " + username);

        log("User registered: " + newUser);
    }

    private static User loginUser() {
        logger.info("Enter your email: ");
        String email = scanner.nextLine();
        if (!isValidEmail(email)) {
            logger.warning("Invalid email format! Please try again.");
            return null;
        }

        logger.info("Enter your password: ");
        String password = scanner.nextLine();

        User foundUser = findUser(email, password);
        if (foundUser != null) {
            logger.info("Login successful! Welcome back, " + foundUser.getUsername());
            log("User logged in: " + foundUser);
        } else {
            logger.warning("Invalid email or password! Please try again.");
        }

        return foundUser;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean userExists(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private static User findUser(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private static void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = parseUser(line);
                users.add(user);
            }
        } catch (IOException e) {
            logger.warning("Error loading user data: " + e.getMessage());
        }
    }

    private static void loadMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MESSAGES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Message message = parseMessage(line);
                messages.add(message);
            }
        } catch (IOException e) {
            logger.warning("Error loading messages: " + e.getMessage());
        }
    }

    private static Message parseMessage(String line) {
        String[] parts = line.split(",");
        String senderEmail = parts[0];
        String receiverEmail = parts[1];
        String content = parts[2];
        String timestampStr = parts[3];
        System.out.println("timestampStr = " + timestampStr);
        LocalDateTime timestamp = LocalDateTime.parse(timestampStr, formatter);
        return new Message(senderEmail, receiverEmail, content, timestamp);
    }


    private static void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (User user : users) {
                writer.write(userToString(user));
                writer.newLine();
            }
        } catch (IOException e) {
            logger.warning("Error saving user data: " + e.getMessage());
        }
    }

    private static void saveMessages() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MESSAGES_FILE))) {
            for (Message message : messages) {
                writer.write(messageToString(message));
                writer.newLine();
            }
        } catch (IOException e) {
            logger.warning("Error saving messages: " + e.getMessage());
        }
    }

    private static String messageToString(Message message) {
        return message.getSenderEmail() + "," +
                message.getReceiverEmail() + "," +
                message.getContent() + "," +
                message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



    private static User parseUser(String line) {
        String[] parts = line.split(",");
        String email = parts[0];
        String username = parts[1];
        String password = parts[2];
        String registrationTimeStr = parts[3];
        LocalDateTime registrationTime = LocalDateTime.parse(registrationTimeStr, formatter);
        return new User(email, username, password);
    }

    private static String userToString(User user) {
        return user.getEmail() + "," + user.getUsername() + "," + user.getPassword() + "," +
                user.getRegistrationTime().format(formatter);
    }

    private static void sendAndReceiveMessages() {

        System.out.println("Enter your choice: ");
        System.out.println("[0] Send a message");
        System.out.println("[1] Display received messages");

        int opt = scanNum.nextInt();

        logger.info("Enter your email: ");
        String senderEmail = scanner.nextLine();
        if (!isValidEmail(senderEmail) || !userExists(senderEmail)) {
            logger.warning("Invalid email or user not found! Please try again.");
            return;
        }

        if (opt==1){
            // Display received messages for the recipient
            displayReceivedMessages(senderEmail);
            return;
        }

        logger.info("Enter the recipient's email: ");
        String receiverEmail = scanner.nextLine();
        if (!isValidEmail(receiverEmail) || !userExists(receiverEmail)) {
            logger.warning("Invalid recipient email or user not found! Please try again.");
            return;
        }

        logger.info("Enter your message: ");
        String content = scanner.nextLine();
        LocalDateTime timestamp = LocalDateTime.now();

        Message newMessage = new Message(senderEmail, receiverEmail, content, timestamp);
        messages.add(newMessage);

        logger.info("Message sent successfully!");
        log("Message sent: " + newMessage);


    }

    private static void displayReceivedMessages(String email) {
        logger.info("Received messages:");
        for (Message message : messages) {
            if (message.getReceiverEmail().equalsIgnoreCase(email)) {
                logger.info(message.toString());
            }
        }
    }

    private static void log(String message) {
        logger.info("LOG: " + message);
    }
}

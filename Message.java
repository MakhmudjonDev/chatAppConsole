package task2;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Message {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    private LocalDateTime timestamp;

    public Message(String senderEmail, String receiverEmail, String content, LocalDateTime timestamp) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " +
                senderEmail + " to " + receiverEmail + ": " + content;
    }
}
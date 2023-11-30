package task2;


import java.time.LocalDateTime;

class User {
    private String email;
    private String username;
    private String password;
    private LocalDateTime registrationTime;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.registrationTime = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registrationTime=" + registrationTime +
                '}';
    }
}

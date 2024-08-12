package Board;

public class User {
    private String userId;
    private String userName;
    private boolean isAdmin;

    public User(String userId, String userName, boolean isAdmin) {
        this.userId = userId;
        this.userName = userName;
        this.isAdmin = isAdmin;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

package htl.steyr.passwortmanager.security;

public final class UserContext {

    private static Integer userId;
    private static String username;

    private UserContext() {} // no instances

    // ================= SET =================

    public static void setUser(int id, String name) {
        userId = id;
        username = name;
    }

    // ================= GET =================

    public static int getUserId() {
        if (userId == null)
            throw new IllegalStateException("No user logged in");
        return userId;
    }

    public static String getUsername() {
        return username;
    }

    // ================= CLEAR =================

    public static void clear() {
        userId = null;
        username = null;
    }

    public static boolean isLoggedIn() {
        return userId != null;
    }
}

package htl.steyr.passwortmanager.service;

import htl.steyr.passwortmanager.dao.UserDAO;
import htl.steyr.passwortmanager.security.Argon2Util;

import java.util.Arrays;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public boolean register(String username,
                            String password,
                            String confirmPassword) throws Exception {

        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;
        if (confirmPassword == null || confirmPassword.isBlank()) return false;
        if (!password.equals(confirmPassword)) return false;
        if (password.length() < 8) return false;

        username = username.trim();

        if (userDAO.usernameExists(username)) return false;

        char[] pw = password.toCharArray();
        String hash = Argon2Util.hash(pw);
        Arrays.fill(pw, '\0');   // Passwort aus RAM löschen

        userDAO.insertUser(username, hash);
        return true;
    }




    public boolean login(String username, String password) throws Exception {

        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty");

        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty");

        String storedHash = userDAO.getPasswordHashByUsername(username);

        if (storedHash == null)
            return false;


        // Checks both hashes and verifies them if they're the same ones
        return Argon2Util.verify(
                storedHash,
                password.toCharArray()
        );
    }


}

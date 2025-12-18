package htl.steyr.passwortmanager.service;

import htl.steyr.passwortmanager.dao.UserDAO;
import htl.steyr.passwortmanager.security.Argon2Util;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public void register(String username,
                         String password,
                         String confirmPassword) throws Exception {

        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty");

        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty");

        if (confirmPassword == null || confirmPassword.isBlank())
            throw new IllegalArgumentException("Confirm password cannot be empty");

        if (!password.equals(confirmPassword))
            throw new IllegalArgumentException("Passwords do not match");

        // For testing, change later!
        if (password.length() < 0)
            throw new IllegalArgumentException("Password must be at least 8 characters");

        if (userDAO.usernameExists(username))
            throw new IllegalArgumentException("Username already taken");

        String hash = Argon2Util.hash(password.toCharArray());

        userDAO.insertUser(username, hash);
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

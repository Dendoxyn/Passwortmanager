package htl.steyr.passwortmanager.service;

import htl.steyr.passwortmanager.dao.UserDAO;
import htl.steyr.passwortmanager.security.Argon2Util;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public void register(String username,
                         String password,
                         String confirmPassword) throws Exception {

        if (username == null || username.isBlank())
            throw new Exception("Username cannot be empty");

        if (password == null || password.isBlank())
            throw new Exception("Password cannot be empty");

        if (!password.equals(confirmPassword))
            throw new Exception("Passwords do not match");

        // For testing, change later!
        if (password.length() < 0)
            throw new Exception("Password must be at least 8 characters");

        if (userDAO.usernameExists(username))
            throw new Exception("Username already taken");

        String hashedPwd = Argon2Util.hash(password.toCharArray());

        // 4️⃣ Insert user into DB
        userDAO.insertUser(username, hashedPwd);
    }
}

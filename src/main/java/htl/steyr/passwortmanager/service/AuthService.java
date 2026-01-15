package htl.steyr.passwortmanager.service;

import htl.steyr.passwortmanager.dao.UserDAO;
import htl.steyr.passwortmanager.security.Argon2Util;
import htl.steyr.passwortmanager.security.AesKeyUtil;
import htl.steyr.passwortmanager.security.CryptoService;
import htl.steyr.passwortmanager.security.UserContext;
import htl.steyr.passwortmanager.utils.ErrorMessages;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    // ================= REGISTER =================

    public boolean register(String username,
                         String password,
                         String confirmPassword) throws Exception {

        if (username == null || username.isBlank())
            throw new IllegalArgumentException(ErrorMessages.USERNAME_EMPTY.getMessage());

        if (password == null || password.isBlank())
            throw new IllegalArgumentException(ErrorMessages.PASSWORD_EMPTY.getMessage());

        if (confirmPassword == null || confirmPassword.isBlank())
            throw new IllegalArgumentException(ErrorMessages.CONFIRM_PASSWORD_EMPTY.getMessage());

        if (!password.equals(confirmPassword))
            throw new IllegalArgumentException(ErrorMessages.PASSWORDS_DO_NOT_MATCH.getMessage());

        if (userDAO.usernameExists(username))
            throw new IllegalArgumentException(ErrorMessages.USERNAME_TAKEN.getMessage());

        // 1. hash master password for login verification
        String passwordHash = Argon2Util.hash(password.toCharArray());

        // 2. generate encryption salt for AES key derivation
        byte[] encryptionSalt = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(encryptionSalt);

        // 3. store user
        userDAO.insertUser(username, passwordHash, encryptionSalt);

        return true;
    }

    // ================= LOGIN =================

    public boolean login(String username, String password) throws Exception {

        if (username == null || username.isBlank())
            throw new IllegalArgumentException(ErrorMessages.USERNAME_EMPTY.getMessage());

        if (password == null || password.isBlank())
            throw new IllegalArgumentException(ErrorMessages.PASSWORD_EMPTY.getMessage());

        UserDAO.UserAuthData data = userDAO.getAuthDataByUsername(username);
        if (data == null) return false;

        boolean ok = Argon2Util.verify(
                data.passwordHash(),
                password.toCharArray()
        );

        if (!ok) return false;

        SecretKey key = AesKeyUtil.deriveKey(
                password.toCharArray(),
                data.encryptionSalt()
        );

        CryptoService.getInstance().init(key);

        UserContext.setUser(data.userId(), data.username());

        return true;
    }

}

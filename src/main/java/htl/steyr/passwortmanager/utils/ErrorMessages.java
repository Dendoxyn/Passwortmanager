package htl.steyr.passwortmanager.utils;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    USERNAME_EMPTY("Username cannot be empty"),
    PASSWORD_EMPTY("Password cannot be empty"),
    CONFIRM_PASSWORD_EMPTY("Confirm password cannot be empty"),
    PASSWORDS_DO_NOT_MATCH("Passwords do not match"),
    PASSWORD_TOO_SHORT("Password must be at least 8 characters"),
    USERNAME_TAKEN("Username already taken");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}

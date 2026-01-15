package htl.steyr.passwortmanager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class User {

    private final int userId;

    @NonNull
    private final String username;

    @NonNull
    private final String hashedPwd;

    @NonNull
    private final byte[] encryptionSalt;
}

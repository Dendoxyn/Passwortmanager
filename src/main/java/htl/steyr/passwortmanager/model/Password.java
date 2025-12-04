package htl.steyr.passwortmanager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Getter
@RequiredArgsConstructor
public class Password {

    private final int passwordId;

    @NonNull
    private final String websiteApp;

    @NonNull
    private final String loginName;

    @NonNull
    private final byte[] encryptedPwd;

    private final String note;

    @NonNull
    private final PasswordTag tag;     // ENUM

    @NonNull
    private final SecurityLevel security; // ENUM

    private final int userId;

    private final Integer groupId;     // nullable FK
}

package htl.steyr.passwortmanager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
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

    @NonNull
    private final String note; // Just use an empty string "" if no note is given

    @NonNull
    private final PasswordTag tag;     // ENUM

    @NonNull
    private final SecurityLevel security; // ENUM

    private final int userId;

    private final Integer groupId;     // nullable FK
}

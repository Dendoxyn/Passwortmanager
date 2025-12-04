package htl.steyr.passwortmanager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Getter
@RequiredArgsConstructor
public class Password {
    private final int passwordId;

    @NonNull
    private final String website_app;

    @NonNull
    private final String loginName;

    @NonNull
    private final String encryptedPwd;

    @NonNull
    private final String note;

    @NonNull



}

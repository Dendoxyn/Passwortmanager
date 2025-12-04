package htl.steyr.passwortmanager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Getter
@RequiredArgsConstructor
public class Group {

    private final int groupId;

    @NonNull
    private final String groupName;

    private final int creatorId;
}

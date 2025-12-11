package htl.steyr.passwortmanager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Group {

    private final int groupId;

    @NonNull
    private final String groupName;

    private final int creatorId;
}

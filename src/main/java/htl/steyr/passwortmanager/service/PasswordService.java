package htl.steyr.passwortmanager.service;

import htl.steyr.passwortmanager.dao.GroupDAO;
import htl.steyr.passwortmanager.dao.PasswordDAO;
import htl.steyr.passwortmanager.model.Password;

import java.util.ArrayList;
import java.util.List;

public class PasswordService {

    private final PasswordDAO passwordDAO = new PasswordDAO();
    private final GroupDAO groupDAO = new GroupDAO();

    public void addPrivatePassword(Password password, int currentUserId) throws Exception {

        if (password.getUserId() != currentUserId) {
            throw new SecurityException("Not your password");
        }

        if (password.getGroupId() != null) {
            throw new IllegalArgumentException("Private password cannot have groupId");
        }

        passwordDAO.insert(password);
    }

    public void addGroupPassword(
            Password password,
            int currentUserId
    ) throws Exception {

        if (!groupDAO.groupExists(password.getGroupId())) {
            throw new IllegalArgumentException("Group does not exist");
        }

        // later: check UserGroup membership here
        passwordDAO.insert(password);
    }

    public List<Password> getVisiblePasswords(int currentUserId) throws Exception {

        List<Password> result = new ArrayList<>();

        result.addAll(passwordDAO.findPrivatePasswords(currentUserId));

        for (int groupId : groupDAO.getGroupIdsForUser(currentUserId)) {
            result.addAll(passwordDAO.findGroupPasswords(groupId));
        }

        return result;
    }
}

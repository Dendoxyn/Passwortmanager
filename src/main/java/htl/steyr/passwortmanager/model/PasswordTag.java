package htl.steyr.passwortmanager.model;

public enum PasswordTag {
    WEBSITE("Website"),
    APPLICATION("Application"),
    SERVER("Server"),
    DATABASE("Database"),
    EMAIL("Email");

    private final String label;

    PasswordTag(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

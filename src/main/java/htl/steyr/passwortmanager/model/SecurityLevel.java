package htl.steyr.passwortmanager.model;

public enum SecurityLevel {
    LOW("Low"),
    MIDDLE("Medium"),
    HIGH("High");

    private final String label;

    SecurityLevel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

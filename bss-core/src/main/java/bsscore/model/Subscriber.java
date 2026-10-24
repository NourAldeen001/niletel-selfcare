package bsscore.model;

public record Subscriber(String msisdn, String fullName, String pinHash, String status) {
    private static final String STATUS_ACTIVE = "A";

    public boolean isActive() {
        return STATUS_ACTIVE.equals(status);
    }
}

package bsscore.model;

import java.time.LocalDate;

public record SubscriberProfile(String msisdn, String fullName, String status,
                                String tariffCode, String tariffName,
                                long balancePiasters, LocalDate activationDate) { }

package org.example.payment.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record CustomerId(UUID value) {}
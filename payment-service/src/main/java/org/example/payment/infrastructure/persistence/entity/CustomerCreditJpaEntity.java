package org.example.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_credits")
public class CustomerCreditJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // ID nội bộ của bảng

    @Column(unique = true, nullable = false)
    private UUID customerId; // ID nghiệp vụ

    @Column(nullable = false)
    private BigDecimal creditLimit;

    @Column(nullable = false)
    private BigDecimal currentBalance;
}
package org.example.payment.infrastructure.persistence.mapper;

import org.example.payment.domain.entity.CustomerCredit;
import org.example.payment.domain.valueobject.CustomerId;
import org.example.payment.domain.valueobject.Money;
import org.example.payment.infrastructure.persistence.entity.CustomerCreditJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // <-- THÊM IMPORT
import org.mapstruct.MappingTarget; // <-- THÊM IMPORT

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerCreditPersistenceMapper {

    CustomerCredit jpaEntityToDomain(CustomerCreditJpaEntity entity);

    @Mapping(target = "id", ignore = true) // ID được generate tự động bởi JPA
    CustomerCreditJpaEntity domainToJpaEntity(CustomerCredit domain);

    /**
     * @param domain Đối tượng nguồn (từ logic nghiệp vụ)
     * @param entity Đối tượng đích (đã tồn tại, lấy từ DB)
     */
    @Mapping(target = "id", ignore = true) // Không bao giờ update Primary Key
    @Mapping(target = "customerId", ignore = true) // Cũng không update Business Key
    void updateJpaEntityFromDomain(CustomerCredit domain, @MappingTarget CustomerCreditJpaEntity entity);


    // === Các phương thức trợ giúp Map Value Objects (giữ nguyên) ===
    default CustomerId map(UUID value) { return (value == null) ? null : new CustomerId(value); }
    default UUID map(CustomerId value) { return (value == null) ? null : value.value(); }

    default Money map(BigDecimal value) { return (value == null) ? null : new Money(value); }
    default BigDecimal map(Money value) { return (value == null) ? null : value.amount(); }
}
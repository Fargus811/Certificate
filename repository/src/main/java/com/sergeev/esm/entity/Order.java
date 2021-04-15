package com.sergeev.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Table(name = "orders")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited(targetAuditMode = NOT_AUDITED)
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "gift_certificate_in_orders",
            joinColumns = @JoinColumn(name = "orders_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id", nullable = false)
    )
    private List<GiftCertificate> giftCertificateList;
}

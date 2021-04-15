package com.sergeev.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * The type Gift certificate.
 */
@Entity
@Table(name = "gift_certificate", schema = "gift")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited(targetAuditMode = NOT_AUDITED)
public class GiftCertificate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "duration", nullable = false)
    private Integer duration;
    @Column(name = "createDate", updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createDate;
    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "gift_tags", schema = "gift",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}

package com.sergeev.esm.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * The type Gift certificate return dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GiftCertificateReturnDTO extends AbstractDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Integer duration;
    private Set<TagDTO> tags;
}

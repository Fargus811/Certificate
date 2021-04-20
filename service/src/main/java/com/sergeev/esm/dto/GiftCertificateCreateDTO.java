package com.sergeev.esm.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

/**
 * The type Gift certificate create dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GiftCertificateCreateDTO extends AbstractDTO {

    @SuppressWarnings("deprecation")
    @NotBlank
    @Length(min = 2, max = 25)
    private String name;
    @SuppressWarnings("deprecation")
    @NotBlank
    @Length(min = 2, max = 255)
    private String description;
    @NotNull
    @DecimalMin(value = "1.00")
    @DecimalMax(value = "1000.00")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal price;
    @NotNull
    @Range(min = 1, max = 99)
    private Integer duration;
    private Set<TagDTO> tags;
}

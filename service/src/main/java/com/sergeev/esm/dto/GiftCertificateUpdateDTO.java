package com.sergeev.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * The type Gift certificate update dto, all fields should be not empty and satisfy the conditions of validation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GiftCertificateUpdateDTO extends AbstractDTO {

    @NotNull
    @Min(1L)
    private Long id;
    @Pattern(regexp = "\\w{2,25}")
    private String name;
    @Pattern(regexp = "\\w{2,255}")
    private String description;
    @Pattern(regexp = "^\\d{1,3}([.,]\\d{1,2})?$")
    private String price;
    @Pattern(regexp = "^\\d{1,2}?$")
    private String duration;
    private Set<TagDTO> tags;
}

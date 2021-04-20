package com.sergeev.esm.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * The type Tag dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TagDTO extends AbstractDTO {

    @Min(1L)
    private Long id;
    @SuppressWarnings("deprecation")
    @NotBlank
    @Length(min = 2, max = 25)
    private String name;
}

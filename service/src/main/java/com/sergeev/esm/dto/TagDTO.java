package com.sergeev.esm.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    @Length(min = 2, max = 25)
    private String name;
}

package com.sergeev.esm.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type User dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDTO extends AbstractDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    @JsonBackReference
    private List<OrderDTO> orders;
}

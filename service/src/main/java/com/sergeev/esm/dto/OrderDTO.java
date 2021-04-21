package com.sergeev.esm.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDTO extends AbstractDTO {

    private long id;
    private BigDecimal price;
    private LocalDateTime date;
    @JsonManagedReference
    private UserDTO user;
    private List<GiftCertificateReturnDTO> giftCertificateList;
}

package com.sergeev.esm.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderDTO extends AbstractDTO {

    private long id;
    private BigDecimal price;
    private LocalDateTime date;
    @JsonManagedReference
    private UserDTO user;
    private List<GiftCertificateReturnDTO> giftCertificateList;
}

package com.sergeev.esm.dto;

import lombok.*;

import java.util.List;

/**
 * The type Create order dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateOrderDTO extends AbstractDTO {

    private long userId;
    private List<Long> giftCertificateIdList;
}

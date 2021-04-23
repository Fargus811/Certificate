package com.sergeev.esm.util;

import com.sergeev.esm.dto.GiftCertificateCreateDTO;
import com.sergeev.esm.dto.GiftCertificateUpdateDTO;
import com.sergeev.esm.entity.GiftCertificate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GiftCertificateMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGiftCertificateFromDto(GiftCertificateUpdateDTO giftCertificateCreateDTO,
                                      @MappingTarget GiftCertificate giftCertificate);
}

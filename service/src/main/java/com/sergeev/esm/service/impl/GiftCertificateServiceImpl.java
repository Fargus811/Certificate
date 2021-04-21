package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.AbstractDTO;
import com.sergeev.esm.dto.GiftCertificateReturnDTO;
import com.sergeev.esm.entity.GiftCertificate;
import com.sergeev.esm.entity.Tag;
import com.sergeev.esm.exception.ResourceFoundException;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.GiftCertificateRepository;
import com.sergeev.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Gift certificate service.
 */
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final ModelMapper modelMapper;

    @Override
    public GiftCertificateReturnDTO findById(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate giftCertificate = optionalGiftCertificate.get();
            return modelMapper.map(giftCertificate, GiftCertificateReturnDTO.class);
        } else {
            throw new ResourceIdNotFoundException(
                    new ObjectError(id.toString(), "Exception.certificateWithIdNotFound"));
        }
    }

    @Override
    public Page<GiftCertificateReturnDTO> findAll(Pageable pageable) {
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAll(pageable);
        return getGiftReturnDTOS(pageable, giftCertificatePage);
    }

    private PageImpl<GiftCertificateReturnDTO> getGiftReturnDTOS(Pageable pageable,
                                                                 Page<GiftCertificate> giftCertificatePage) {
        return new PageImpl<>(giftCertificatePage.get()
                .map(order -> modelMapper.map(order, GiftCertificateReturnDTO.class))
                .collect(Collectors.toList()), pageable, giftCertificatePage.getTotalElements());
    }

    @Override
    @Transactional
    public <T extends AbstractDTO> GiftCertificateReturnDTO create(T giftCertificateCreateDTO) {
        GiftCertificate giftCertificate = modelMapper.map(giftCertificateCreateDTO, GiftCertificate.class);
        String giftCertificateName = giftCertificate.getName();
        if (giftCertificateRepository.findByName(giftCertificateName).isPresent()) {
            throw new ResourceFoundException(new ObjectError(giftCertificateName,
                    "Exception.certificateWithNameFound"));
        } else {
            giftCertificate.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()));
            giftCertificate.setLastUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
            return modelMapper.map(giftCertificateRepository.save(giftCertificate),
                    GiftCertificateReturnDTO.class);
        }
    }

    @Override
    public Page<GiftCertificateReturnDTO> findAllByTagNames(Set<String> tagNames, Pageable pageable) {
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAllByTagNames(tagNames, pageable);
        return getGiftReturnDTOS(pageable, giftCertificatePage);
    }

    @Override
    @Transactional
    public <T extends AbstractDTO>  GiftCertificateReturnDTO update(T giftCertificateToUpdateDTO) {
        GiftCertificate giftCertificateToUpdate = modelMapper.map(giftCertificateToUpdateDTO, GiftCertificate.class);
        Optional<GiftCertificate> giftCertificateOptional =
                giftCertificateRepository.findById(giftCertificateToUpdate.getId());
        if (giftCertificateOptional.isPresent()) {
            giftCertificateToUpdate.setLastUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
            return modelMapper.map(giftCertificateRepository.save(giftCertificateToUpdate),
                    GiftCertificateReturnDTO.class);
        } else {
            throw new ResourceIdNotFoundException(new ObjectError(giftCertificateToUpdate.getId().toString(),
                    "Exception.certificateWithIdNotFound"));
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (giftCertificateRepository.findById(id).isPresent()) {
            giftCertificateRepository.deleteById(id);
        } else {
            throw new ResourceIdNotFoundException
                    (new ObjectError(id.toString(), "Exception.certificateWithIdNotFound"));
        }
    }
}


package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.AbstractDTO;
import com.sergeev.esm.dto.GiftCertificateCreateDTO;
import com.sergeev.esm.dto.GiftCertificateReturnDTO;
import com.sergeev.esm.dto.GiftCertificateUpdateDTO;
import com.sergeev.esm.entity.GiftCertificate;
import com.sergeev.esm.exception.ResourceFoundException;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.GiftCertificateRepository;
import com.sergeev.esm.service.GiftCertificateService;
import com.sergeev.esm.util.GiftCertificateMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;

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
    private final GiftCertificateMapper giftCertificateMapper;

    @Override
    public GiftCertificateReturnDTO findById(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        checkGiftCertificateByIdInDB(id, optionalGiftCertificate);
        return modelMapper.map(optionalGiftCertificate.get(), GiftCertificateReturnDTO.class);
    }

    private void checkGiftCertificateByIdInDB(Long id, Optional<GiftCertificate> optionalGiftCertificate) {
        if (optionalGiftCertificate.isEmpty()) {
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
                .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateReturnDTO.class))
                .collect(Collectors.toList()), pageable, giftCertificatePage.getTotalElements());
    }

    @Override
    public <T extends AbstractDTO> GiftCertificateReturnDTO createOrUpdate(T giftCertificateCreateOrUpdateDTO) {
        GiftCertificate giftCertificate = modelMapper.map(giftCertificateCreateOrUpdateDTO, GiftCertificate.class);
        GiftCertificate resultToSave;
        if (Objects.nonNull(giftCertificate.getId())) {
            Long giftCertificateIdToUpdate = giftCertificate.getId();
            checkIfNameUniqBesidesThisCertificate(giftCertificate.getName(), giftCertificate.getId());
            Optional<GiftCertificate> certificateFromDB = giftCertificateRepository.findById(giftCertificateIdToUpdate);
            checkGiftCertificateByIdInDB(giftCertificateIdToUpdate, certificateFromDB);
            resultToSave = certificateFromDB.get();
            giftCertificateMapper.updateGiftCertificateFromDto(
                    (GiftCertificateUpdateDTO) giftCertificateCreateOrUpdateDTO, resultToSave);
        } else {
            checkGiftCertificateByName(giftCertificate.getName());
            resultToSave = giftCertificate;
        }
        GiftCertificate resultCertificate = giftCertificateRepository.save(resultToSave);
        return modelMapper.map(resultCertificate, GiftCertificateReturnDTO.class);
    }

    private void checkIfNameUniqBesidesThisCertificate(String giftCertificateNameToUpdate, Long idToUpdate) {
        Optional<GiftCertificate> certificateFromDB = giftCertificateRepository.findByName(giftCertificateNameToUpdate);
        if (certificateFromDB.isPresent() && !(certificateFromDB.get().getId().equals(idToUpdate))) {
            throw new ResourceFoundException(new ObjectError(giftCertificateNameToUpdate,
                    "Exception.certificateWithNameFound"));
        }

    }

    private void checkGiftCertificateByName(String giftCertificateName) {
        if (giftCertificateRepository.findByName(giftCertificateName).isPresent()) {
            throw new ResourceFoundException(new ObjectError(giftCertificateName,
                    "Exception.certificateWithNameFound"));
        }
    }

    @Override
    public Page<GiftCertificateReturnDTO> findAllByTagNames(Set<String> tagNames, Pageable pageable) {
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAllByTagNames(tagNames, pageable);
        return getGiftReturnDTOS(pageable, giftCertificatePage);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<GiftCertificate> giftCertificateFromDB = giftCertificateRepository.findById(id);
        checkGiftCertificateByIdInDB(id, giftCertificateFromDB);
        giftCertificateRepository.deleteById(id);
    }
}


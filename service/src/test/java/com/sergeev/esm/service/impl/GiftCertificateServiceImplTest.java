package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.GiftCertificateCreateDTO;
import com.sergeev.esm.dto.GiftCertificateReturnDTO;
import com.sergeev.esm.dto.GiftCertificateUpdateDTO;
import com.sergeev.esm.dto.TagDTO;
import com.sergeev.esm.entity.GiftCertificate;
import com.sergeev.esm.entity.Tag;
import com.sergeev.esm.exception.ResourceFoundException;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.GiftCertificateRepository;
import com.sergeev.esm.util.GiftCertificateMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GiftCertificateMapper giftCertificateMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByIdTest() {
        //given
        GiftCertificate giftCertificateFromDB = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .build();

        GiftCertificateReturnDTO giftCertificateReturnDTO = GiftCertificateReturnDTO.builder()
                .name("testing")
                .description("description")
                .build();

        //when
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFromDB));
        when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);

        //then
        assertEquals(giftCertificateReturnDTO, giftCertificateService.findById(1L));
        verify(giftCertificateRepository, atLeastOnce()).findById(1L);
        verify(modelMapper, times(1)).map(giftCertificateFromDB, GiftCertificateReturnDTO.class);
        verifyNoMoreInteractions(giftCertificateRepository, modelMapper);
    }

    @Test
    void findByWrongIdThrowsResourceIdNotFoundExceptionTest() {
        //given & when
        when(giftCertificateRepository.findById(10000000L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                giftCertificateService.findById(10000000L));
        verify(giftCertificateRepository, times(1)).findById(10000000L);
        verifyNoMoreInteractions(giftCertificateRepository);
    }

    @Test
    void findAll() {
        //given
        GiftCertificate giftCertificateFromDB = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .build();

        GiftCertificateReturnDTO giftCertificateReturnDTO = GiftCertificateReturnDTO.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .build();

        PageImpl<GiftCertificateReturnDTO> giftCertificateReturnDTOPage = new PageImpl<>(List.of(giftCertificateReturnDTO));

        //when
        when(giftCertificateRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(giftCertificateFromDB)));
        when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);

        //then
        assertEquals(giftCertificateReturnDTOPage, giftCertificateService.findAll(Pageable.unpaged()));
        verify(giftCertificateRepository, times(1)).findAll(Pageable.unpaged());
        verify(modelMapper, times(1)).map(giftCertificateFromDB, GiftCertificateReturnDTO.class);
        verifyNoMoreInteractions(giftCertificateRepository, modelMapper);
    }

    @Test
    void createGiftCertificateTest() {
        //given
        GiftCertificateCreateDTO giftCertificateCreateDTO = GiftCertificateCreateDTO.builder()
                .name("testing")
                .description("description")
                .build();

        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("testing")
                .description("description")
                .build();

        GiftCertificateReturnDTO giftCertificateReturnDTO = GiftCertificateReturnDTO.builder()
                .name("testing")
                .description("description")
                .build();

        //when
        when(modelMapper.map(giftCertificateCreateDTO, GiftCertificate.class)).thenReturn(giftCertificate);
        when(giftCertificateRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
        when(modelMapper.map(giftCertificate, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);

        //then
        GiftCertificateReturnDTO savedGiftCertificate = giftCertificateService.upsert(giftCertificateCreateDTO);
        assertEquals(giftCertificateReturnDTO, savedGiftCertificate);
        verify(giftCertificateRepository, times(1)).findByName(giftCertificate.getName());
        verify(giftCertificateRepository, times(1)).save(giftCertificate);
        verify(modelMapper, times(1)).map(giftCertificateCreateDTO, GiftCertificate.class);
        verify(modelMapper, times(1)).map(giftCertificate, GiftCertificateReturnDTO.class);
        verifyNoMoreInteractions(giftCertificateRepository, modelMapper);
    }

    @Test
    void createGiftCertificateWithSameNameTest() {
        //given
        GiftCertificateCreateDTO giftCertificateCreateDTO = GiftCertificateCreateDTO.builder()
                .name("testing")
                .description("description")
                .build();

        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("testing")
                .description("description")
                .build();

        //when
        when(modelMapper.map(giftCertificateCreateDTO, GiftCertificate.class)).thenReturn(giftCertificate);
        when(giftCertificateRepository.findByName(any(String.class))).thenReturn(Optional.of(giftCertificate));

        //then
        assertThrows(ResourceFoundException.class, () ->
                giftCertificateService.upsert(giftCertificateCreateDTO));
        verify(giftCertificateRepository, times(1)).findByName(giftCertificate.getName());
        verify(modelMapper, times(1)).map(giftCertificateCreateDTO, GiftCertificate.class);
        verifyNoMoreInteractions(giftCertificateRepository, modelMapper);
    }

    @Test
    void updateCertificateTest() {
        //given
        GiftCertificateUpdateDTO giftCertificateUpdateDTO = GiftCertificateUpdateDTO.builder()
                .id(1L)
                .name("testing")
                .description("updateDescription")
                .build();

        GiftCertificate giftCertificateToUpdate = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("updateDescription")
                .build();

        GiftCertificate giftCertificateFromDB = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .price(BigDecimal.valueOf(200.00))
                .duration(91)
                .build();

        GiftCertificateReturnDTO giftCertificateReturnDTO = GiftCertificateReturnDTO.builder()
                .id(1L)
                .name("testing")
                .description("updateDescription")
                .price(BigDecimal.valueOf(200.00))
                .duration(91)
                .build();

        //when
        when(modelMapper.map(giftCertificateUpdateDTO, GiftCertificate.class)).thenReturn(giftCertificateToUpdate);
        when(giftCertificateRepository.findByName(giftCertificateToUpdate.getName()))
                .thenReturn(Optional.of(giftCertificateFromDB));
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFromDB));
        when(giftCertificateRepository.save(giftCertificateFromDB)).thenReturn(giftCertificateFromDB);
        when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);

        //then
        GiftCertificateReturnDTO savedGiftCertificate = giftCertificateService.upsert(giftCertificateUpdateDTO);
        assertEquals(giftCertificateReturnDTO, savedGiftCertificate);
        verify(giftCertificateRepository, times(1)).findByName(giftCertificateFromDB.getName());
        verify(giftCertificateRepository, times(1)).findById(giftCertificateToUpdate.getId());
        verify(giftCertificateRepository, times(1)).save(giftCertificateFromDB);
        verify(modelMapper, times(1)).map(giftCertificateUpdateDTO, GiftCertificate.class);
        verify(modelMapper, times(1)).map(giftCertificateFromDB, GiftCertificateReturnDTO.class);
        verify(giftCertificateMapper, times(1)).updateGiftCertificateFromDto(giftCertificateUpdateDTO,
                giftCertificateFromDB);
        verifyNoMoreInteractions(giftCertificateRepository, modelMapper, giftCertificateMapper);
    }

    @Test
    void updateCertificateWithSameNameThrowsResourceFoundExceptionTest() {
        //given
        GiftCertificateUpdateDTO giftCertificateUpdateDTO = GiftCertificateUpdateDTO.builder()
                .id(1L)
                .name("testing")
                .description("updateDescription")
                .build();

        GiftCertificate giftCertificateToUpdate = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("updateDescription")
                .build();

        GiftCertificate giftCertificateFromDB = GiftCertificate.builder()
                .id(2L)
                .name("testing")
                .description("description")
                .price(BigDecimal.valueOf(200.00))
                .duration(91)
                .build();

        GiftCertificateReturnDTO giftCertificateReturnDTO = GiftCertificateReturnDTO.builder()
                .id(2L)
                .name("testing")
                .description("updateDescription")
                .price(BigDecimal.valueOf(200.00))
                .duration(91)
                .build();

        //when
        when(modelMapper.map(giftCertificateUpdateDTO, GiftCertificate.class)).thenReturn(giftCertificateToUpdate);
        when(giftCertificateRepository.findByName(giftCertificateToUpdate.getName()))
                .thenReturn(Optional.of(giftCertificateFromDB));
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFromDB));
        when(giftCertificateRepository.save(giftCertificateFromDB)).thenReturn(giftCertificateFromDB);
        when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);

        //then
        assertThrows(ResourceFoundException.class, () ->
                giftCertificateService.upsert(giftCertificateUpdateDTO));
        verify(giftCertificateRepository, times(1)).findByName(giftCertificateFromDB.getName());
        verify(modelMapper, times(1)).map(giftCertificateUpdateDTO, GiftCertificate.class);
        verifyNoMoreInteractions(giftCertificateRepository, modelMapper);
    }

    @Test
    void updateNotExistCertificateInDatabaseThrowsResourceIdNotFoundExceptionTest() {
        //given
        GiftCertificateUpdateDTO giftCertificateUpdateDTO = GiftCertificateUpdateDTO.builder()
                .id(1000000000L)
                .name("testing")
                .description("updateDescription")
                .build();

        GiftCertificate giftCertificateToUpdate = GiftCertificate.builder()
                .id(1000000000L)
                .name("testing")
                .description("updateDescription")
                .build();

        //when
        when(modelMapper.map(giftCertificateUpdateDTO, GiftCertificate.class)).thenReturn(giftCertificateToUpdate);
        when(giftCertificateRepository.findByName(giftCertificateToUpdate.getName()))
                .thenReturn(Optional.empty());
        when(giftCertificateRepository.findById(1000000000L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                giftCertificateService.upsert(giftCertificateUpdateDTO));
        verify(giftCertificateRepository, times(1)).findByName(giftCertificateToUpdate.getName());
        verify(giftCertificateRepository, times(1)).findById(giftCertificateToUpdate.getId());
        verify(modelMapper, times(1)).map(giftCertificateUpdateDTO, GiftCertificate.class);
        verifyNoMoreInteractions(giftCertificateRepository, modelMapper);
    }

    @Test
    void findAllByTagNamesTest() {
        //given
        Set<String> tagNames = Set.of("sea");
        Tag tagFromDB = Tag.builder().id(1L).name("sea").build();
        GiftCertificate giftCertificateFromDB = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .tags(Set.of(tagFromDB))
                .build();

        TagDTO tagDTO = TagDTO.builder().id(1L).name("sea").build();
        GiftCertificateReturnDTO giftCertificateReturnDTO = GiftCertificateReturnDTO.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .tags(Set.of(tagDTO))
                .build();

        PageImpl<GiftCertificateReturnDTO> giftCertificateReturnDTOPage =
                new PageImpl<>(List.of(giftCertificateReturnDTO));

        //when
        when(giftCertificateRepository.findAllByTagNames(tagNames, Pageable.unpaged()))
                .thenReturn(new PageImpl<>(List.of(giftCertificateFromDB)));
        when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class))
                .thenReturn(giftCertificateReturnDTO);

        //then
        assertEquals(giftCertificateReturnDTOPage,
                giftCertificateService.findAllByTagNames(tagNames, Pageable.unpaged()));
        verify(giftCertificateRepository, times(1)).findAllByTagNames(tagNames, Pageable.unpaged());
        verify(modelMapper, times(1)).map(giftCertificateFromDB, GiftCertificateReturnDTO.class);
        verifyNoMoreInteractions(giftCertificateRepository, modelMapper);
    }

    @Test
    void deleteTest() {
        //given
        GiftCertificate giftCertificateFromDB = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .build();

        //when
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFromDB));
        doNothing().when(giftCertificateRepository).deleteById(1L);

        //then
        giftCertificateService.delete(1L);
        verify(giftCertificateRepository, times(1)).deleteById(1L);
        verify(giftCertificateRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(giftCertificateRepository);
    }

    @Test
    void deleteThrowResourceIdNotFoundExceptionTest() {
        //given & when
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.empty());
        doNothing().when(giftCertificateRepository).deleteById(1L);

        //then
        assertThrows(ResourceIdNotFoundException.class, () -> {
            giftCertificateService.delete(1L);
        });
        verify(giftCertificateRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(giftCertificateRepository);
    }
}
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        Mockito.when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFromDB));
        Mockito.when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);
        //then
        Assertions.assertEquals(giftCertificateReturnDTO, giftCertificateService.findById(1L));

        verify(giftCertificateRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(giftCertificateFromDB, GiftCertificateReturnDTO.class);
    }

    @Test
    void findByWrongIdThrowsResourceIdNotFoundExceptionTest() {
        //when
        Mockito.when(giftCertificateRepository.findById(10000000L)).thenReturn(Optional.empty());
        //then
        Assertions.assertThrows(ResourceIdNotFoundException.class, ()->
                giftCertificateService.findById(10000000L));

        verify(giftCertificateRepository, times(1)).findById(10000000L);
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
        Mockito.when(giftCertificateRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(giftCertificateFromDB)));
        Mockito.when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);
        //then
        Assertions.assertEquals(giftCertificateReturnDTOPage, giftCertificateService.findAll(Pageable.unpaged()));

        verify(giftCertificateRepository, times(1)).findAll(Pageable.unpaged());
        verify(modelMapper, times(1)).map(giftCertificateFromDB, GiftCertificateReturnDTO.class);
    }

    @Test
    void createGiftCertificateTest() {
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

        Mockito.when(modelMapper.map(giftCertificateCreateDTO, GiftCertificate.class)).thenReturn(giftCertificate);
        Mockito.when(giftCertificateRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        Mockito.when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
        Mockito.when(modelMapper.map(giftCertificate, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);

        GiftCertificateReturnDTO savedGiftCertificate = giftCertificateService.createOrUpdate(giftCertificateCreateDTO);
        Assertions.assertEquals(giftCertificateReturnDTO, savedGiftCertificate);

        verify(giftCertificateRepository, times(1)).findByName(giftCertificate.getName());
        verify(giftCertificateRepository, times(1)).save(giftCertificate);
        verify(modelMapper, times(1)).map(giftCertificateCreateDTO, GiftCertificate.class);
        verify(modelMapper, times(1)).map(giftCertificate, GiftCertificateReturnDTO.class);
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

        GiftCertificateReturnDTO giftCertificateReturnDTO = GiftCertificateReturnDTO.builder()
                .name("testing")
                .description("description")
                .build();
        //when
        Mockito.when(modelMapper.map(giftCertificateCreateDTO, GiftCertificate.class)).thenReturn(giftCertificate);
        Mockito.when(giftCertificateRepository.findByName(any(String.class))).thenReturn(Optional.of(giftCertificate));
        //then
        Assertions.assertThrows(ResourceFoundException.class, ()->
                giftCertificateService.createOrUpdate(giftCertificateCreateDTO));

        verify(giftCertificateRepository, times(1)).findByName(giftCertificate.getName());
        verify(modelMapper, times(1)).map(giftCertificateCreateDTO, GiftCertificate.class);
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
        Mockito.when(modelMapper.map(giftCertificateUpdateDTO, GiftCertificate.class)).thenReturn(giftCertificateToUpdate);
        Mockito.when(giftCertificateRepository.findByName(giftCertificateToUpdate.getName()))
                .thenReturn(Optional.of(giftCertificateFromDB));
        Mockito.when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFromDB));
        Mockito.when(giftCertificateRepository.save(giftCertificateFromDB)).thenReturn(giftCertificateFromDB);
        Mockito.when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);
        //then
        GiftCertificateReturnDTO savedGiftCertificate = giftCertificateService.createOrUpdate(giftCertificateUpdateDTO);
        Assertions.assertEquals(giftCertificateReturnDTO, savedGiftCertificate);

        verify(giftCertificateRepository, times(1)).findByName(giftCertificateFromDB.getName());
        verify(giftCertificateRepository, times(1)).findById(giftCertificateToUpdate.getId());
        verify(giftCertificateRepository, times(1)).save(giftCertificateFromDB);
        verify(modelMapper, times(1)).map(giftCertificateUpdateDTO, GiftCertificate.class);
        verify(modelMapper, times(1)).map(giftCertificateFromDB, GiftCertificateReturnDTO.class);
        verify(giftCertificateMapper, times(1)).updateGiftCertificateFromDto(giftCertificateUpdateDTO,
                giftCertificateFromDB);
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
        Mockito.when(modelMapper.map(giftCertificateUpdateDTO, GiftCertificate.class)).thenReturn(giftCertificateToUpdate);
        Mockito.when(giftCertificateRepository.findByName(giftCertificateToUpdate.getName()))
                .thenReturn(Optional.of(giftCertificateFromDB));
        Mockito.when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFromDB));
        Mockito.when(giftCertificateRepository.save(giftCertificateFromDB)).thenReturn(giftCertificateFromDB);
        Mockito.when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class)).thenReturn(giftCertificateReturnDTO);
        //then
        Assertions.assertThrows(ResourceFoundException.class, ()->
                giftCertificateService.createOrUpdate(giftCertificateUpdateDTO));

        verify(giftCertificateRepository, times(1)).findByName(giftCertificateFromDB.getName());
        verify(modelMapper, times(1)).map(giftCertificateUpdateDTO, GiftCertificate.class);

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
        Mockito.when(modelMapper.map(giftCertificateUpdateDTO, GiftCertificate.class)).thenReturn(giftCertificateToUpdate);
        Mockito.when(giftCertificateRepository.findByName(giftCertificateToUpdate.getName()))
                .thenReturn(Optional.empty());
        Mockito.when(giftCertificateRepository.findById(1000000000L)).thenReturn(Optional.empty());
        //then
        Assertions.assertThrows(ResourceIdNotFoundException.class, ()->
                giftCertificateService.createOrUpdate(giftCertificateUpdateDTO));

        verify(giftCertificateRepository, times(1)).findByName(giftCertificateToUpdate.getName());
        verify(modelMapper, times(1)).map(giftCertificateUpdateDTO, GiftCertificate.class);

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
        Mockito.when(giftCertificateRepository.findAllByTagNames(tagNames, Pageable.unpaged()))
                .thenReturn(new PageImpl<>(List.of(giftCertificateFromDB)));
        Mockito.when(modelMapper.map(giftCertificateFromDB, GiftCertificateReturnDTO.class))
                .thenReturn(giftCertificateReturnDTO);
        //then
        Assertions.assertEquals(giftCertificateReturnDTOPage,
                giftCertificateService.findAllByTagNames(tagNames,Pageable.unpaged()));

        verify(giftCertificateRepository, times(1)).findAllByTagNames(tagNames, Pageable.unpaged());
        verify(modelMapper, times(1)).map(giftCertificateFromDB, GiftCertificateReturnDTO.class);
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
        Mockito.when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFromDB));
        Mockito.doNothing().when(giftCertificateRepository).deleteById(1L);
        //then
        giftCertificateService.delete(1L);
        verify(giftCertificateRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteThrowResourceIdNotFoundExceptionTest() {
        //when
        Mockito.when(giftCertificateRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.doNothing().when(giftCertificateRepository).deleteById(1L);
        //then
        Assertions.assertThrows(ResourceIdNotFoundException.class, () -> {
            giftCertificateService.delete(1L);
        });
    }
}
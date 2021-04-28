package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.TagDTO;
import com.sergeev.esm.entity.Tag;
import com.sergeev.esm.exception.ResourceFoundException;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        //given
        Tag tag = Tag.builder().id(1L).build();
        TagDTO tagDTO = TagDTO.builder().id(1L).build();

        //when
        Mockito.when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        Mockito.when(modelMapper.map(tag, TagDTO.class)).thenReturn(tagDTO);

        //then
        assertEquals(tagDTO, tagService.findById(1L));
        verify(tagRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(tag, TagDTO.class);
        verifyNoMoreInteractions(tagRepository, modelMapper);
    }

    @Test
    void findByWrongIdThrowsResourceIdNotFoundExceptionTest() {
        //when
        Mockito.when(tagRepository.findById(10000000L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                tagService.findById(10000000L));
        verify(tagRepository, times(1)).findById(10000000L);
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void findAll() {
        //given
        Tag tagFromDB = Tag.builder().id(1L).build();

        TagDTO tagDTO = TagDTO.builder().id(1L).build();

        PageImpl<TagDTO> orderDTOPage = new PageImpl<>(List.of(tagDTO));

        //when
        when(tagRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(tagFromDB)));
        when(modelMapper.map(tagFromDB, TagDTO.class)).thenReturn(tagDTO);

        //then
        assertEquals(orderDTOPage, tagService.findAll(Pageable.unpaged()));
        verify(tagRepository, times(1)).findAll(Pageable.unpaged());
        verify(modelMapper, times(1)).map(tagFromDB, TagDTO.class);
        verifyNoMoreInteractions(tagRepository, modelMapper);
    }

    @Test
    void createSuccess() {
        //given
        TagDTO tagDTO = TagDTO.builder().name("test").build();
        Tag tag = Tag.builder().name("test").build();

        //when
        when(modelMapper.map(tagDTO, Tag.class)).thenReturn(tag);
        when(tagRepository.findByName("test")).thenReturn(Optional.empty());
        when(tagRepository.save(tag)).thenReturn(tag);
        when(modelMapper.map(tag, TagDTO.class)).thenReturn(tagDTO);

        //then
        assertEquals(tagDTO, tagService.upsert(tagDTO));
        verify(tagRepository, times(1)).save(tag);
        verify(tagRepository, times(1)).findByName("test");
        verify(modelMapper, times(1)).map(tag, TagDTO.class);
        verify(modelMapper, times(1)).map(tagDTO, Tag.class);
        verifyNoMoreInteractions(tagRepository,modelMapper);
    }

    @Test
    void createThrowsResourceFoundException(){
        //given
        TagDTO tagDTO = TagDTO.builder().name("test").build();
        Tag tag = Tag.builder().name("test").build();

        //when
        when(modelMapper.map(tagDTO, Tag.class)).thenReturn(tag);
        when(tagRepository.findByName("test")).thenReturn(Optional.of(tag));

        //then
        assertThrows(ResourceFoundException.class, () ->
                tagService.upsert(tagDTO));
        verify(tagRepository, times(1)).findByName("test");
        verify(modelMapper, times(1)).map(tagDTO, Tag.class);
    }

    @Test
    void updateIfNameNotUniqBesidesThisCertificateThrowsResourceFoundException(){
        //given
        TagDTO tagDTO = TagDTO.builder().id(1L).name("test").build();
        Tag tagFromDTO = Tag.builder().id(1L).name("test").build();
        Tag tag = Tag.builder().id(2L).name("test").build();

        //when
        when(modelMapper.map(tagDTO, Tag.class)).thenReturn(tagFromDTO);
        when(tagRepository.findByName("test")).thenReturn(Optional.of(tag));

        //then
        assertThrows(ResourceFoundException.class, () ->
                tagService.upsert(tagDTO));
        verify(tagRepository, times(1)).findByName("test");
        verify(modelMapper, times(1)).map(tagDTO, Tag.class);
    }

    @Test
    void delete() {
        //given
        Tag tag = Tag.builder().id(1L).name("test").build();

        //when
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        doNothing().when(tagRepository).deleteById(1L);

        //then
        tagService.delete(1L);
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void  deleteThrowsIdNotFoundException(){
        //given & when
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                tagService.findById(1L));
        verify(tagRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void findUsersMostWidelyUsedTag() {
        //given
        TagDTO tagDTO = TagDTO.builder().name("test").build();
        Tag tag = Tag.builder().name("test").build();

        //when
        when(tagRepository.findUsersMostWidelyUsedTag()).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag);
        when(modelMapper.map(tag, TagDTO.class)).thenReturn(tagDTO);

        //then
        assertEquals(tagDTO, tagService.findUsersMostWidelyUsedTag());
        verify(tagRepository, times(1)).findUsersMostWidelyUsedTag();
        verify(modelMapper, times(1)).map(tag, TagDTO.class);
        verifyNoMoreInteractions(tagRepository,modelMapper);
    }

    @Test
    void findUsersMostWidelyUsedTagThrowsResourceNotFoundException() {
        //given & when
        when(tagRepository.findUsersMostWidelyUsedTag()).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                tagService.findUsersMostWidelyUsedTag());
        verify(tagRepository, times(1)).findUsersMostWidelyUsedTag();
        verifyNoMoreInteractions(tagRepository);
    }
}
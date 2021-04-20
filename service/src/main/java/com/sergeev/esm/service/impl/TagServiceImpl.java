package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.AbstractDTO;
import com.sergeev.esm.dto.TagDTO;
import com.sergeev.esm.entity.Tag;
import com.sergeev.esm.exception.ResourceFoundException;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.TagRepository;
import com.sergeev.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Tag service.
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public <T extends AbstractDTO> TagDTO create(T tagDTO) {
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new ResourceFoundException(new ObjectError(tag.getName(),
                    "Exception.tagWithNameFound"));
        }
        return modelMapper.map(tagRepository.save(tag), TagDTO.class);
    }


    @Override
    @Transactional
    public void update(AbstractDTO tagDTO) {
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new ResourceFoundException(new ObjectError(tag.getName(),
                    "Exception.tagWithNameFound"));
        }
        this.findById(tag.getId());
        tagRepository.save(tag);
    }

    @Override
    public void delete(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) {
            throw new ResourceIdNotFoundException
                    (new ObjectError(id.toString(), "Exception.tagWithIdNotFound"));
        }
        tagRepository.deleteById(id);
    }

    @Override
    public TagDTO findById(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) {
            throw new ResourceIdNotFoundException
                    (new ObjectError(id.toString(), "Exception.tagWithIdNotFound"));
        }
        return modelMapper.map(tagOptional.get(), TagDTO.class);
    }

    @Override
    public Page<TagDTO> findAll(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);
        return getTagDTOPage(pageable, tagPage);
    }

    private PageImpl<TagDTO> getTagDTOPage(Pageable pageable, Page<Tag> tagPage) {
        return new PageImpl<>(tagPage.get().map(tag -> modelMapper.map(tag, TagDTO.class)).
                collect(Collectors.toList()), pageable, tagPage.getTotalElements());
    }

    @Override
    public TagDTO findUsersMostWidelyUsedTag() {
        Optional<Tag> usersMostWidelyUsedTag = tagRepository.findUsersMostWidelyUsedTag();
        if (usersMostWidelyUsedTag.isEmpty()) {
            throw new ResourceIdNotFoundException
                    (new ObjectError(usersMostWidelyUsedTag.toString(), "Exception.tagNotFound"));
        }
        return modelMapper.map(usersMostWidelyUsedTag.get(), TagDTO.class);
    }
}

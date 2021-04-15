package com.sergeev.esm.service;

import com.sergeev.esm.dto.TagDTO;

/**
 * The interface Tag service.
 */
public interface TagService extends BaseService<TagDTO> {

    /**
     * Gets users most widely used tag.
     *
     * @return the users most widely used tag
     */
    TagDTO findUsersMostWidelyUsedTag();
}

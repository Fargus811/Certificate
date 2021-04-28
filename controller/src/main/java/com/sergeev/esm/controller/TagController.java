package com.sergeev.esm.controller;

import com.sergeev.esm.dto.AbstractDTO;
import com.sergeev.esm.dto.TagDTO;
import com.sergeev.esm.service.TagService;
import com.sergeev.esm.util.HATEOASBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * The type Tag controller.
 */
@RestController
@Validated
@RequestMapping(value = "/api/v3/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    /**
     * Options response entity shows all the ways to manipulate the resource.
     *
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS, HttpMethod.PUT, HttpMethod.DELETE)
                .build();
    }

    /**
     * Find all paged model.
     *
     * @param pageable the pageable
     * @return the paged model
     */
    @GetMapping
    public PagedModel<? extends AbstractDTO> findAll(Pageable pageable) {
        Page<TagDTO> tagsDTO = tagService.findAll(pageable);
        HATEOASBuilder.addLinksToListTag(tagsDTO.getContent());
        return pagedResourcesAssembler.toModel(tagsDTO);
    }

    /**
     * Find by id of tag .
     *
     * @param id the id
     * @return the tag dto
     */
    @GetMapping(value = "/{id}")
    public TagDTO findById(@PathVariable("id") @Min(1L) @Max(10000L) Long id) {
        TagDTO tagDTO = tagService.findById(id);
        HATEOASBuilder.addLinksToTag(tagDTO);
        return tagDTO;
    }

    /**
     * Create tag.
     *
     * @param tagDTO the tagDTO
     * @return the response entity
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagDTO tagDTO) {
        TagDTO resultDTO = tagService.upsert(tagDTO);
        HATEOASBuilder.addLinksToTag(resultDTO);
        return ResponseEntity.ok(resultDTO);
    }

    /**
     * Delete tag by ID.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") @Min(1L) @Max(10000L) Long id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Update tag.
     *
     * @param tagDTO the tag
     */
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TagDTO> updateTag(@Valid @RequestBody TagDTO tagDTO) {
        TagDTO updatedTag = tagService.upsert(tagDTO);
        HATEOASBuilder.addLinksToTag(updatedTag);
        return ResponseEntity.ok(updatedTag);
    }

    /**
     * Gets users most widely used tag.
     *
     * @return the users most widely used tag
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/top")
    public ResponseEntity<TagDTO> getUsersMostWidelyUsedTag() {
        TagDTO tagDTO = tagService.findUsersMostWidelyUsedTag();
        HATEOASBuilder.addLinksToTag(tagDTO);
        return ResponseEntity.ok(tagDTO);

    }
}

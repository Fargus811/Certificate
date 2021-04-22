package com.sergeev.esm.controller;

import com.sergeev.esm.dto.AbstractDTO;
import com.sergeev.esm.dto.GiftCertificateCreateDTO;
import com.sergeev.esm.dto.GiftCertificateReturnDTO;
import com.sergeev.esm.dto.GiftCertificateUpdateDTO;
import com.sergeev.esm.service.GiftCertificateService;
import com.sergeev.esm.util.HATEOASBuilder;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The type Gift certificate controller.
 */
@RestController
@RequestMapping(value = "/api/v3/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
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
     * Find all by params paged model.
     *
     * @param tagNames the tag names
     * @param pageable the pageable
     * @return the paged model
     */
    @SuppressWarnings("deprecation")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public PagedModel<GiftCertificateReturnDTO> findAllByParams(
            @RequestParam(required = false) Set<@NotEmpty String> tagNames, Pageable pageable) {
        Page<GiftCertificateReturnDTO> giftCertificateReturnDTOPage = Objects.isNull(tagNames)
                ? giftCertificateService.findAll(pageable)
                : giftCertificateService.findAllByTagNames(tagNames, pageable);
        HATEOASBuilder.addLinksToListCertificate(giftCertificateReturnDTOPage.getContent());
        return pagedResourcesAssembler.toModel(giftCertificateReturnDTOPage);
    }

    /**
     * Find by idLine gift certificate.
     *
     * @param id the idLine
     * @return the gift certificate
     */
    @GetMapping("/{id}")
    public AbstractDTO findById(@PathVariable("id") @Min(1L) @Max(10000L) Long id) {
        GiftCertificateReturnDTO giftCertificateToReturnDTO = giftCertificateService.findById(id);
        return HATEOASBuilder.addLinksToCertificate(giftCertificateToReturnDTO);
    }

    /**
     * Create new giftCertificate.
     *
     * @param giftCertificateCreateDTO the gift certificate
     * @return the response entity
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GiftCertificateReturnDTO> create(
            @Valid @RequestBody GiftCertificateCreateDTO giftCertificateCreateDTO) {
        GiftCertificateReturnDTO giftCertificateReturnDTO = giftCertificateService.createOrUpdate(giftCertificateCreateDTO);
        HATEOASBuilder.addLinksToCertificate(giftCertificateReturnDTO);
        return ResponseEntity.of(Optional.of(giftCertificateReturnDTO));
    }

    /**
     * Update the giftCertificate. All fields should be not empty and satisfy the conditions of validation.
     *
     * @param giftCertificateUpdateDTO the gift certificate
     */
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GiftCertificateReturnDTO> update(@Valid @RequestBody GiftCertificateUpdateDTO giftCertificateUpdateDTO) {
        GiftCertificateReturnDTO updatedCertificate = giftCertificateService.createOrUpdate(giftCertificateUpdateDTO);
        HATEOASBuilder.addLinksToCertificate(updatedCertificate);
        return ResponseEntity.ok(updatedCertificate);
    }

    /**
     * Delete giftCertificate by id.
     *
     * @param id the id.
     * @return the response entity
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") @Min(1L) @Max(10000L) Long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

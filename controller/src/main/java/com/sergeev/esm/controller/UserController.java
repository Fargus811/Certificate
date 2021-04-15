package com.sergeev.esm.controller;

import com.sergeev.esm.dto.UserDTO;
import com.sergeev.esm.service.UserService;
import com.sergeev.esm.util.HATEOASBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * The type User controller.
 */
@RestController
@Validated
@RequestMapping(value = "/api/v3/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    /**
     * Find all paged model.
     *
     * @param pageable the pageable
     * @return the paged model
     */
    @GetMapping
    public PagedModel<UserDTO> findAll(Pageable pageable) {
        Page<UserDTO> userDTOPage = userService.findAll(pageable);
        HATEOASBuilder.addLinksToListUser(userDTOPage.getContent());
        return pagedResourcesAssembler.toModel(userDTOPage);
    }

    /**
     * Find by id abstract dto.
     *
     * @param id the id
     * @return the abstract dto
     */
    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") @Min(1L) @Max(10000L) Long id) {
        UserDTO userDTO = userService.findById(id);
        return HATEOASBuilder.addLinksToUser(userDTO);
    }
}

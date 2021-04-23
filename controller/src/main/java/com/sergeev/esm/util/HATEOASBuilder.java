package com.sergeev.esm.util;

import com.sergeev.esm.controller.GiftCertificateController;
import com.sergeev.esm.controller.OrderController;
import com.sergeev.esm.controller.TagController;
import com.sergeev.esm.controller.UserController;
import com.sergeev.esm.dto.GiftCertificateReturnDTO;
import com.sergeev.esm.dto.OrderDTO;
import com.sergeev.esm.dto.TagDTO;
import com.sergeev.esm.dto.UserDTO;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The type Hateoas builder.
 */
@UtilityClass
public class HATEOASBuilder {

    private final int DEFAULT_NUMBER_OF_PAGE = 1;
    private final int DEFAULT_SIZE_OF_PAGE = 10;
    private final String USER = "user";
    private final String DELETE = "delete";
    private final String USER_ORDERS = "user's orders";
    private final String DELETE_TAG = "delete tag";

    /**
     * Add links to certificate gift certificate return dto.
     *
     * @param giftCertificateDto the gift certificate dto
     * @return the gift certificate return dto
     */
    public static GiftCertificateReturnDTO addLinksToCertificate(GiftCertificateReturnDTO giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class).
                delete(giftCertificateDto.getId())).withRel(DELETE));
        if (Objects.nonNull(giftCertificateDto.getTags())) {
            addLinksToListTag(new ArrayList<>(giftCertificateDto.getTags()));
        }
        return giftCertificateDto;
    }

    /**
     * Add links to tag tag dto.
     *
     * @param tagDto the tag dto
     * @return the tag dto
     */
    public static TagDTO addLinksToTag(TagDTO tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class).deleteTag(tagDto.getId())).withRel(DELETE_TAG));
        return tagDto;
    }

    /**
     * Add links to user user dto.
     *
     * @param user the user
     * @return the user dto
     */
    public static UserDTO addLinksToUser(UserDTO user) {
        user.add(linkTo(methodOn(OrderController.class).findOrders(user.getId(),
                PageRequest.of(DEFAULT_NUMBER_OF_PAGE, DEFAULT_SIZE_OF_PAGE))).
                withRel(USER_ORDERS));
        return user;
    }

    /**
     * Add links to order order dto.
     *
     * @param orderDTO the order dto
     * @return the order dto
     */
    public static OrderDTO addLinksToOrder(OrderDTO orderDTO) {
        orderDTO.add(linkTo(methodOn(OrderController.class).findById(orderDTO.getId())).withRel("currentOrder"));
        if (Objects.nonNull(orderDTO.getGiftCertificateList())) {
            addLinksToListCertificate(orderDTO.getGiftCertificateList());
        }
        UserDTO user = orderDTO.getUser();
        user.add(linkTo(methodOn(UserController.class).findById(user.getId())).withRel(USER));
        return orderDTO;
    }

    /**
     * Add links to list certificate.
     *
     * @param certificateDtoList the certificate dto list
     */
    public static void addLinksToListCertificate(List<GiftCertificateReturnDTO> certificateDtoList) {
        for (GiftCertificateReturnDTO giftCertificateDto : certificateDtoList) {
            giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                    .findById(giftCertificateDto.getId())).withSelfRel());
            if (Objects.nonNull(giftCertificateDto.getTags())) {
                addLinksToListTag(new ArrayList<>(giftCertificateDto.getTags()));
            }
        }
    }

    /**
     * Add links to list tag.
     *
     * @param tagDTOList the tag dto set
     */
    public static void addLinksToListTag(List<TagDTO> tagDTOList) {
        for (TagDTO tagDto : tagDTOList) {
            tagDto.add(linkTo(methodOn(TagController.class).findById(tagDto.getId())).withSelfRel());
        }
    }

    /**
     * Add links to list user.
     *
     * @param userDtoList the user dto list
     */
    public static void addLinksToListUser(List<UserDTO> userDtoList) {
        for (UserDTO userDto : userDtoList) {
            userDto.add(linkTo(methodOn(UserController.class).findById(userDto.getId())).withSelfRel());
        }
    }

    /**
     * Add links to list order.
     *
     * @param orderDTOList the order dto list
     */
    public static void addLinksToListOrder(List<OrderDTO> orderDTOList, Pageable pageable) {
        for (OrderDTO orderDTO : orderDTOList) {
            orderDTO.add(linkTo(methodOn(OrderController.class)
                    .findOrders(orderDTO.getUser().getId(), pageable)).withRel(USER_ORDERS));
            if (Objects.nonNull(orderDTO.getGiftCertificateList())) {
                addLinksToListCertificate(orderDTO.getGiftCertificateList());
            }
            UserDTO user = orderDTO.getUser();
            user.add(linkTo(methodOn(UserController.class).findById(user.getId())).withRel(USER));
        }
    }
}

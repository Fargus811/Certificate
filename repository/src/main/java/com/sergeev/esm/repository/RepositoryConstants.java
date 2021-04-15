package com.sergeev.esm.repository;

import lombok.experimental.UtilityClass;

/**
 * The type Dao constants.
 */
@UtilityClass
public class RepositoryConstants {

    /**
     * The constant ID.
     */
    public static final String ID = "id";
    /**
     * The constant PART_OF_NAME.
     */
    public static final String NAME = "name";
    /**
     * The constant INSERT_TAG.
     */
    public static final String PRICE = "price";
    /**
     * The constant USER.
     */
    public static final String USER = "user";
    /**
     * The constant DURATION.
     */
    public static final String DURATION = "duration";
    /**
     * The constant SEPARATOR_OR.
     */
    public static final String SEPARATOR_OR = "|";
    /**
     * The constant PARAMETER_TAG_NAME_LIST.
     */
    public static final String PARAMETER_TAG_NAME_LIST = "tagNames";
    /**
     * The constant SELECT_TAG_BY_NAMES.
     */
    public static final String SELECT_TAG_BY_NAMES = "from Tag tag where tag.name in (:" + PARAMETER_TAG_NAME_LIST + ")";
    /**
     * The constant TAGS.
     */
    public static final String TAGS = "tags";
    /**
     * The constant DESC.
     */
    public static final String DESC = "desc";
    /**
     * The constant ASC.
     */
    public static final String ASC = "asc";
    /**
     * The constant TAG_ID.
     */
    public static final String TAG_ID = "tag_id";
    /**
     * The constant DELETE_TAG_FROM_ALL_CERTIFICATES.
     */
    public static final String DELETE_TAG_FROM_ALL_CERTIFICATES = "delete from gift_tags where tag_id = :tag_id";
    /**
     * The constant SELECT_ALL_CERTIFICATES.
     */
    public static final String FIND_USER_MOST_WIDELY_USED_TAG = "SELECT tag.id, tag.name, COUNT(tag.name) AS mut from orders\n" +
            "inner join gift_certificate_in_orders on orders.id = orders_id\n" +
            "inner join gift_tags on (gift_certificate_in_orders.gift_certificate_id=gift_tags.gift_certificate_id)\n" +
            "inner join tag on (tag.id=gift_tags.tag_id) where orders.user_id = \n" +
            "(select orders.user_id from orders\n" +
            "group by orders.user_id\n" +
            "order by sum(orders.price) desc\n" +
            "limit 1) \n" +
            "group by tag.name\n" +
            "order by mut desc\n" +
            "limit 1";
}

package com.sergeev.esm.repository;

import com.sergeev.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TagRepositoryTest {

    @Autowired
    TagRepository tagRepository;

    @Test
    void findByName() {
        //given
        String expectedTagName = "ut";

        //then
        Optional<Tag> tag = tagRepository.findByName("ut");
        assertEquals(expectedTagName, tag.get().getName());
    }

    @Test
    void findUsersMostWidelyUsedTag() {

    }
}
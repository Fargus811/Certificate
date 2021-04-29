package com.sergeev.esm.repository;

import com.sergeev.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Test
    void findByName() {
        //given & when
        String expectedNameOfGiftCertificate = "alias";

        //then
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findByName("alias");
        assertEquals(expectedNameOfGiftCertificate, giftCertificate.get().getName());
    }

    @Test
    void findAllByTagNames() {
        //given & when
        int expectedSizeOfResultList = 7;

        Set<String> tagNames = Set.of("ut", "similique");

        //then
        Page<GiftCertificate> allCertificatesByTagNames = giftCertificateRepository.findAllByTagNames(tagNames,
                Pageable.unpaged());
        assertEquals(expectedSizeOfResultList, allCertificatesByTagNames.getTotalElements());
    }
}
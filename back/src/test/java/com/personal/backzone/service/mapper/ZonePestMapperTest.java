package com.personal.backzone.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZonePestMapperTest {

    private ZonePestMapper zonePestMapper;

    @BeforeEach
    public void setUp() {
        zonePestMapper = new ZonePestMapperImpl();
    }
}

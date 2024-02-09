package com.personal.backzone.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PestMapperTest {

    private PestMapper pestMapper;

    @BeforeEach
    public void setUp() {
        pestMapper = new PestMapperImpl();
    }
}

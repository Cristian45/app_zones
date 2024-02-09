package com.personal.backzone.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.personal.backzone.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ZonePestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZonePestDTO.class);
        ZonePestDTO zonePestDTO1 = new ZonePestDTO();
        zonePestDTO1.setId(1L);
        ZonePestDTO zonePestDTO2 = new ZonePestDTO();
        assertThat(zonePestDTO1).isNotEqualTo(zonePestDTO2);
        zonePestDTO2.setId(zonePestDTO1.getId());
        assertThat(zonePestDTO1).isEqualTo(zonePestDTO2);
        zonePestDTO2.setId(2L);
        assertThat(zonePestDTO1).isNotEqualTo(zonePestDTO2);
        zonePestDTO1.setId(null);
        assertThat(zonePestDTO1).isNotEqualTo(zonePestDTO2);
    }
}

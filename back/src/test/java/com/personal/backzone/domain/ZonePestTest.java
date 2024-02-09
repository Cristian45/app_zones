package com.personal.backzone.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.personal.backzone.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ZonePestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZonePest.class);
        ZonePest zonePest1 = new ZonePest();
        zonePest1.setId(1L);
        ZonePest zonePest2 = new ZonePest();
        zonePest2.setId(zonePest1.getId());
        assertThat(zonePest1).isEqualTo(zonePest2);
        zonePest2.setId(2L);
        assertThat(zonePest1).isNotEqualTo(zonePest2);
        zonePest1.setId(null);
        assertThat(zonePest1).isNotEqualTo(zonePest2);
    }
}

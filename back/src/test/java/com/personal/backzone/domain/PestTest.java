package com.personal.backzone.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.personal.backzone.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pest.class);
        Pest pest1 = new Pest();
        pest1.setId(1L);
        Pest pest2 = new Pest();
        pest2.setId(pest1.getId());
        assertThat(pest1).isEqualTo(pest2);
        pest2.setId(2L);
        assertThat(pest1).isNotEqualTo(pest2);
        pest1.setId(null);
        assertThat(pest1).isNotEqualTo(pest2);
    }
}

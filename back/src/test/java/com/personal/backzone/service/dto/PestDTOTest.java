package com.personal.backzone.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.personal.backzone.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PestDTO.class);
        PestDTO pestDTO1 = new PestDTO();
        pestDTO1.setId(1L);
        PestDTO pestDTO2 = new PestDTO();
        assertThat(pestDTO1).isNotEqualTo(pestDTO2);
        pestDTO2.setId(pestDTO1.getId());
        assertThat(pestDTO1).isEqualTo(pestDTO2);
        pestDTO2.setId(2L);
        assertThat(pestDTO1).isNotEqualTo(pestDTO2);
        pestDTO1.setId(null);
        assertThat(pestDTO1).isNotEqualTo(pestDTO2);
    }
}

package com.rts.gestor.academia.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rts.gestor.academia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TutorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tutor.class);
        Tutor tutor1 = new Tutor();
        tutor1.setId(1L);
        Tutor tutor2 = new Tutor();
        tutor2.setId(tutor1.getId());
        assertThat(tutor1).isEqualTo(tutor2);
        tutor2.setId(2L);
        assertThat(tutor1).isNotEqualTo(tutor2);
        tutor1.setId(null);
        assertThat(tutor1).isNotEqualTo(tutor2);
    }
}

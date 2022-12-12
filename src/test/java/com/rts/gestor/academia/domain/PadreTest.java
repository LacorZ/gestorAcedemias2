package com.rts.gestor.academia.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rts.gestor.academia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PadreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Padre.class);
        Padre padre1 = new Padre();
        padre1.setId(1L);
        Padre padre2 = new Padre();
        padre2.setId(padre1.getId());
        assertThat(padre1).isEqualTo(padre2);
        padre2.setId(2L);
        assertThat(padre1).isNotEqualTo(padre2);
        padre1.setId(null);
        assertThat(padre1).isNotEqualTo(padre2);
    }
}

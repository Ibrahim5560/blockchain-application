package com.isoft.blockchain.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.blockchain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientAccountDTO.class);
        ClientAccountDTO clientAccountDTO1 = new ClientAccountDTO();
        clientAccountDTO1.setId("id1");
        ClientAccountDTO clientAccountDTO2 = new ClientAccountDTO();
        assertThat(clientAccountDTO1).isNotEqualTo(clientAccountDTO2);
        clientAccountDTO2.setId(clientAccountDTO1.getId());
        assertThat(clientAccountDTO1).isEqualTo(clientAccountDTO2);
        clientAccountDTO2.setId("id2");
        assertThat(clientAccountDTO1).isNotEqualTo(clientAccountDTO2);
        clientAccountDTO1.setId(null);
        assertThat(clientAccountDTO1).isNotEqualTo(clientAccountDTO2);
    }
}

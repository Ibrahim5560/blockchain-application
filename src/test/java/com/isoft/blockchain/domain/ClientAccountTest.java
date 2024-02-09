package com.isoft.blockchain.domain;

import static com.isoft.blockchain.domain.AccountTransactionTestSamples.*;
import static com.isoft.blockchain.domain.ClientAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.blockchain.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientAccount.class);
        ClientAccount clientAccount1 = getClientAccountSample1();
        ClientAccount clientAccount2 = new ClientAccount();
        assertThat(clientAccount1).isNotEqualTo(clientAccount2);

        clientAccount2.setId(clientAccount1.getId());
        assertThat(clientAccount1).isEqualTo(clientAccount2);

        clientAccount2 = getClientAccountSample2();
        assertThat(clientAccount1).isNotEqualTo(clientAccount2);
    }

    @Test
    void clientAccountTest() throws Exception {
        ClientAccount clientAccount = getClientAccountRandomSampleGenerator();
        AccountTransaction accountTransactionBack = getAccountTransactionRandomSampleGenerator();

        clientAccount.addClientAccount(accountTransactionBack);
        assertThat(clientAccount.getClientAccounts()).containsOnly(accountTransactionBack);
        assertThat(accountTransactionBack.getClientAccount()).isEqualTo(clientAccount);

        clientAccount.removeClientAccount(accountTransactionBack);
        assertThat(clientAccount.getClientAccounts()).doesNotContain(accountTransactionBack);
        assertThat(accountTransactionBack.getClientAccount()).isNull();

        clientAccount.clientAccounts(new HashSet<>(Set.of(accountTransactionBack)));
        assertThat(clientAccount.getClientAccounts()).containsOnly(accountTransactionBack);
        assertThat(accountTransactionBack.getClientAccount()).isEqualTo(clientAccount);

        clientAccount.setClientAccounts(new HashSet<>());
        assertThat(clientAccount.getClientAccounts()).doesNotContain(accountTransactionBack);
        assertThat(accountTransactionBack.getClientAccount()).isNull();
    }
}

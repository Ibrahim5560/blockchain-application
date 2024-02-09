package com.isoft.blockchain.domain;

import static com.isoft.blockchain.domain.AccountTransactionTestSamples.*;
import static com.isoft.blockchain.domain.ClientAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.blockchain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountTransaction.class);
        AccountTransaction accountTransaction1 = getAccountTransactionSample1();
        AccountTransaction accountTransaction2 = new AccountTransaction();
        assertThat(accountTransaction1).isNotEqualTo(accountTransaction2);

        accountTransaction2.setId(accountTransaction1.getId());
        assertThat(accountTransaction1).isEqualTo(accountTransaction2);

        accountTransaction2 = getAccountTransactionSample2();
        assertThat(accountTransaction1).isNotEqualTo(accountTransaction2);
    }

    @Test
    void clientAccountTest() throws Exception {
        AccountTransaction accountTransaction = getAccountTransactionRandomSampleGenerator();
        ClientAccount clientAccountBack = getClientAccountRandomSampleGenerator();

        accountTransaction.setClientAccount(clientAccountBack);
        assertThat(accountTransaction.getClientAccount()).isEqualTo(clientAccountBack);

        accountTransaction.clientAccount(null);
        assertThat(accountTransaction.getClientAccount()).isNull();
    }
}

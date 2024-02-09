package com.isoft.blockchain.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AccountTransactionMapperTest {

    private AccountTransactionMapper accountTransactionMapper;

    @BeforeEach
    public void setUp() {
        accountTransactionMapper = new AccountTransactionMapperImpl();
    }
}

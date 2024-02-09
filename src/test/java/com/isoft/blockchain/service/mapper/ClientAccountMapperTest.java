package com.isoft.blockchain.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ClientAccountMapperTest {

    private ClientAccountMapper clientAccountMapper;

    @BeforeEach
    public void setUp() {
        clientAccountMapper = new ClientAccountMapperImpl();
    }
}

package com.isoft.blockchain.domain;

import java.util.UUID;

public class ClientAccountTestSamples {

    public static ClientAccount getClientAccountSample1() {
        return new ClientAccount().id("id1").accountNumber("accountNumber1").owner("owner1");
    }

    public static ClientAccount getClientAccountSample2() {
        return new ClientAccount().id("id2").accountNumber("accountNumber2").owner("owner2");
    }

    public static ClientAccount getClientAccountRandomSampleGenerator() {
        return new ClientAccount()
            .id(UUID.randomUUID().toString())
            .accountNumber(UUID.randomUUID().toString())
            .owner(UUID.randomUUID().toString());
    }
}

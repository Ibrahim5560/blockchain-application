package com.isoft.blockchain.domain;

import java.util.UUID;

public class AccountTransactionTestSamples {

    public static AccountTransaction getAccountTransactionSample1() {
        return new AccountTransaction().id("id1").transactionName("transactionName1");
    }

    public static AccountTransaction getAccountTransactionSample2() {
        return new AccountTransaction().id("id2").transactionName("transactionName2");
    }

    public static AccountTransaction getAccountTransactionRandomSampleGenerator() {
        return new AccountTransaction().id(UUID.randomUUID().toString()).transactionName(UUID.randomUUID().toString());
    }
}

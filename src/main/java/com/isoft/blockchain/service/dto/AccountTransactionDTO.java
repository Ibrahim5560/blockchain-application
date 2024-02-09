package com.isoft.blockchain.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.isoft.blockchain.domain.AccountTransaction} entity.
 */
@Schema(description = "AccountTransaction (account_transaction) entity.\n @author Ibrahim Mohamed.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountTransactionDTO implements Serializable {

    private String id;

    private String transactionName;

    private Double amount;

    private ClientAccountDTO clientAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ClientAccountDTO getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(ClientAccountDTO clientAccount) {
        this.clientAccount = clientAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountTransactionDTO)) {
            return false;
        }

        AccountTransactionDTO accountTransactionDTO = (AccountTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountTransactionDTO{" +
            "id='" + getId() + "'" +
            ", transactionName='" + getTransactionName() + "'" +
            ", amount=" + getAmount() +
            ", clientAccount=" + getClientAccount() +
            "}";
    }
}

package com.isoft.blockchain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * AccountTransaction (account_transaction) entity.
 *  @author Ibrahim Mohamed.
 */
@Document(collection = "account_transaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("transaction_name")
    private String transactionName;

    @Field("amount")
    private Double amount;

    @DBRef
    @Field("clientAccount")
    @JsonIgnoreProperties(value = { "clientAccounts" }, allowSetters = true)
    private ClientAccount clientAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public AccountTransaction id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionName() {
        return this.transactionName;
    }

    public AccountTransaction transactionName(String transactionName) {
        this.setTransactionName(transactionName);
        return this;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Double getAmount() {
        return this.amount;
    }

    public AccountTransaction amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ClientAccount getClientAccount() {
        return this.clientAccount;
    }

    public void setClientAccount(ClientAccount clientAccount) {
        this.clientAccount = clientAccount;
    }

    public AccountTransaction clientAccount(ClientAccount clientAccount) {
        this.setClientAccount(clientAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((AccountTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountTransaction{" +
            "id=" + getId() +
            ", transactionName='" + getTransactionName() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}

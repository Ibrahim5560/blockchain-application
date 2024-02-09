package com.isoft.blockchain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * ClientAccount (client_account) entity.
 *  @author Ibrahim Mohamed.
 */
@Document(collection = "client_account")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("account_number")
    private String accountNumber;

    @Field("owner")
    private String owner;

    @Field("balance")
    private Double balance;

    @Field("creation_date")
    private LocalDate creationDate;

    @DBRef
    @Field("clientAccount")
    @JsonIgnoreProperties(value = { "clientAccount" }, allowSetters = true)
    private Set<AccountTransaction> clientAccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ClientAccount id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public ClientAccount accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwner() {
        return this.owner;
    }

    public ClientAccount owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Double getBalance() {
        return this.balance;
    }

    public ClientAccount balance(Double balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public ClientAccount creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Set<AccountTransaction> getClientAccounts() {
        return this.clientAccounts;
    }

    public void setClientAccounts(Set<AccountTransaction> accountTransactions) {
        if (this.clientAccounts != null) {
            this.clientAccounts.forEach(i -> i.setClientAccount(null));
        }
        if (accountTransactions != null) {
            accountTransactions.forEach(i -> i.setClientAccount(this));
        }
        this.clientAccounts = accountTransactions;
    }

    public ClientAccount clientAccounts(Set<AccountTransaction> accountTransactions) {
        this.setClientAccounts(accountTransactions);
        return this;
    }

    public ClientAccount addClientAccount(AccountTransaction accountTransaction) {
        this.clientAccounts.add(accountTransaction);
        accountTransaction.setClientAccount(this);
        return this;
    }

    public ClientAccount removeClientAccount(AccountTransaction accountTransaction) {
        this.clientAccounts.remove(accountTransaction);
        accountTransaction.setClientAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientAccount{" +
            "id=" + getId() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", owner='" + getOwner() + "'" +
            ", balance=" + getBalance() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}

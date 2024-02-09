package com.isoft.blockchain.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.isoft.blockchain.domain.ClientAccount} entity.
 */
@Schema(description = "ClientAccount (client_account) entity.\n @author Ibrahim Mohamed.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientAccountDTO implements Serializable {

    private String id;

    private String accountNumber;

    private String owner;

    private Double balance;

    private LocalDate creationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientAccountDTO)) {
            return false;
        }

        ClientAccountDTO clientAccountDTO = (ClientAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientAccountDTO{" +
            "id='" + getId() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", owner='" + getOwner() + "'" +
            ", balance=" + getBalance() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}

package com.isoft.blockchain.service;

import com.isoft.blockchain.service.dto.AccountTransactionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.isoft.blockchain.domain.AccountTransaction}.
 */
public interface AccountTransactionService {
    /**
     * Save a accountTransaction.
     *
     * @param accountTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    AccountTransactionDTO save(AccountTransactionDTO accountTransactionDTO);

    /**
     * Updates a accountTransaction.
     *
     * @param accountTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    AccountTransactionDTO update(AccountTransactionDTO accountTransactionDTO);

    /**
     * Partially updates a accountTransaction.
     *
     * @param accountTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountTransactionDTO> partialUpdate(AccountTransactionDTO accountTransactionDTO);

    /**
     * Get all the accountTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountTransactionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accountTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountTransactionDTO> findOne(String id);

    /**
     * Delete the "id" accountTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}

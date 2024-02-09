package com.isoft.blockchain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.blockchain.IntegrationTest;
import com.isoft.blockchain.domain.AccountTransaction;
import com.isoft.blockchain.repository.AccountTransactionRepository;
import com.isoft.blockchain.service.dto.AccountTransactionDTO;
import com.isoft.blockchain.service.mapper.AccountTransactionMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link AccountTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountTransactionResourceIT {

    private static final String DEFAULT_TRANSACTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/account-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    @Autowired
    private AccountTransactionMapper accountTransactionMapper;

    @Autowired
    private MockMvc restAccountTransactionMockMvc;

    private AccountTransaction accountTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountTransaction createEntity() {
        AccountTransaction accountTransaction = new AccountTransaction().transactionName(DEFAULT_TRANSACTION_NAME).amount(DEFAULT_AMOUNT);
        return accountTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountTransaction createUpdatedEntity() {
        AccountTransaction accountTransaction = new AccountTransaction().transactionName(UPDATED_TRANSACTION_NAME).amount(UPDATED_AMOUNT);
        return accountTransaction;
    }

    @BeforeEach
    public void initTest() {
        accountTransactionRepository.deleteAll();
        accountTransaction = createEntity();
    }

    @Test
    void createAccountTransaction() throws Exception {
        int databaseSizeBeforeCreate = accountTransactionRepository.findAll().size();
        // Create the AccountTransaction
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(accountTransaction);
        restAccountTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        AccountTransaction testAccountTransaction = accountTransactionList.get(accountTransactionList.size() - 1);
        assertThat(testAccountTransaction.getTransactionName()).isEqualTo(DEFAULT_TRANSACTION_NAME);
        assertThat(testAccountTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    void createAccountTransactionWithExistingId() throws Exception {
        // Create the AccountTransaction with an existing ID
        accountTransaction.setId("existing_id");
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(accountTransaction);

        int databaseSizeBeforeCreate = accountTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAccountTransactions() throws Exception {
        // Initialize the database
        accountTransactionRepository.save(accountTransaction);

        // Get all the accountTransactionList
        restAccountTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountTransaction.getId())))
            .andExpect(jsonPath("$.[*].transactionName").value(hasItem(DEFAULT_TRANSACTION_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    void getAccountTransaction() throws Exception {
        // Initialize the database
        accountTransactionRepository.save(accountTransaction);

        // Get the accountTransaction
        restAccountTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, accountTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountTransaction.getId()))
            .andExpect(jsonPath("$.transactionName").value(DEFAULT_TRANSACTION_NAME))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    void getNonExistingAccountTransaction() throws Exception {
        // Get the accountTransaction
        restAccountTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingAccountTransaction() throws Exception {
        // Initialize the database
        accountTransactionRepository.save(accountTransaction);

        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();

        // Update the accountTransaction
        AccountTransaction updatedAccountTransaction = accountTransactionRepository.findById(accountTransaction.getId()).orElseThrow();
        updatedAccountTransaction.transactionName(UPDATED_TRANSACTION_NAME).amount(UPDATED_AMOUNT);
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(updatedAccountTransaction);

        restAccountTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
        AccountTransaction testAccountTransaction = accountTransactionList.get(accountTransactionList.size() - 1);
        assertThat(testAccountTransaction.getTransactionName()).isEqualTo(UPDATED_TRANSACTION_NAME);
        assertThat(testAccountTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    void putNonExistingAccountTransaction() throws Exception {
        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();
        accountTransaction.setId(UUID.randomUUID().toString());

        // Create the AccountTransaction
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(accountTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAccountTransaction() throws Exception {
        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();
        accountTransaction.setId(UUID.randomUUID().toString());

        // Create the AccountTransaction
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(accountTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAccountTransaction() throws Exception {
        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();
        accountTransaction.setId(UUID.randomUUID().toString());

        // Create the AccountTransaction
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(accountTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAccountTransactionWithPatch() throws Exception {
        // Initialize the database
        accountTransactionRepository.save(accountTransaction);

        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();

        // Update the accountTransaction using partial update
        AccountTransaction partialUpdatedAccountTransaction = new AccountTransaction();
        partialUpdatedAccountTransaction.setId(accountTransaction.getId());

        partialUpdatedAccountTransaction.transactionName(UPDATED_TRANSACTION_NAME);

        restAccountTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountTransaction))
            )
            .andExpect(status().isOk());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
        AccountTransaction testAccountTransaction = accountTransactionList.get(accountTransactionList.size() - 1);
        assertThat(testAccountTransaction.getTransactionName()).isEqualTo(UPDATED_TRANSACTION_NAME);
        assertThat(testAccountTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    void fullUpdateAccountTransactionWithPatch() throws Exception {
        // Initialize the database
        accountTransactionRepository.save(accountTransaction);

        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();

        // Update the accountTransaction using partial update
        AccountTransaction partialUpdatedAccountTransaction = new AccountTransaction();
        partialUpdatedAccountTransaction.setId(accountTransaction.getId());

        partialUpdatedAccountTransaction.transactionName(UPDATED_TRANSACTION_NAME).amount(UPDATED_AMOUNT);

        restAccountTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountTransaction))
            )
            .andExpect(status().isOk());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
        AccountTransaction testAccountTransaction = accountTransactionList.get(accountTransactionList.size() - 1);
        assertThat(testAccountTransaction.getTransactionName()).isEqualTo(UPDATED_TRANSACTION_NAME);
        assertThat(testAccountTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    void patchNonExistingAccountTransaction() throws Exception {
        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();
        accountTransaction.setId(UUID.randomUUID().toString());

        // Create the AccountTransaction
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(accountTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAccountTransaction() throws Exception {
        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();
        accountTransaction.setId(UUID.randomUUID().toString());

        // Create the AccountTransaction
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(accountTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAccountTransaction() throws Exception {
        int databaseSizeBeforeUpdate = accountTransactionRepository.findAll().size();
        accountTransaction.setId(UUID.randomUUID().toString());

        // Create the AccountTransaction
        AccountTransactionDTO accountTransactionDTO = accountTransactionMapper.toDto(accountTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountTransaction in the database
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAccountTransaction() throws Exception {
        // Initialize the database
        accountTransactionRepository.save(accountTransaction);

        int databaseSizeBeforeDelete = accountTransactionRepository.findAll().size();

        // Delete the accountTransaction
        restAccountTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountTransaction> accountTransactionList = accountTransactionRepository.findAll();
        assertThat(accountTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

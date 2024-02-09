package com.isoft.blockchain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.blockchain.IntegrationTest;
import com.isoft.blockchain.domain.ClientAccount;
import com.isoft.blockchain.repository.ClientAccountRepository;
import com.isoft.blockchain.service.dto.ClientAccountDTO;
import com.isoft.blockchain.service.mapper.ClientAccountMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ClientAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientAccountResourceIT {

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/client-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ClientAccountRepository clientAccountRepository;

    @Autowired
    private ClientAccountMapper clientAccountMapper;

    @Autowired
    private MockMvc restClientAccountMockMvc;

    private ClientAccount clientAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientAccount createEntity() {
        ClientAccount clientAccount = new ClientAccount()
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .owner(DEFAULT_OWNER)
            .balance(DEFAULT_BALANCE)
            .creationDate(DEFAULT_CREATION_DATE);
        return clientAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientAccount createUpdatedEntity() {
        ClientAccount clientAccount = new ClientAccount()
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .owner(UPDATED_OWNER)
            .balance(UPDATED_BALANCE)
            .creationDate(UPDATED_CREATION_DATE);
        return clientAccount;
    }

    @BeforeEach
    public void initTest() {
        clientAccountRepository.deleteAll();
        clientAccount = createEntity();
    }

    @Test
    void createClientAccount() throws Exception {
        int databaseSizeBeforeCreate = clientAccountRepository.findAll().size();
        // Create the ClientAccount
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(clientAccount);
        restClientAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeCreate + 1);
        ClientAccount testClientAccount = clientAccountList.get(clientAccountList.size() - 1);
        assertThat(testClientAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testClientAccount.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testClientAccount.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testClientAccount.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    void createClientAccountWithExistingId() throws Exception {
        // Create the ClientAccount with an existing ID
        clientAccount.setId("existing_id");
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(clientAccount);

        int databaseSizeBeforeCreate = clientAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllClientAccounts() throws Exception {
        // Initialize the database
        clientAccountRepository.save(clientAccount);

        // Get all the clientAccountList
        restClientAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientAccount.getId())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    void getClientAccount() throws Exception {
        // Initialize the database
        clientAccountRepository.save(clientAccount);

        // Get the clientAccount
        restClientAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, clientAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientAccount.getId()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    void getNonExistingClientAccount() throws Exception {
        // Get the clientAccount
        restClientAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingClientAccount() throws Exception {
        // Initialize the database
        clientAccountRepository.save(clientAccount);

        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();

        // Update the clientAccount
        ClientAccount updatedClientAccount = clientAccountRepository.findById(clientAccount.getId()).orElseThrow();
        updatedClientAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .owner(UPDATED_OWNER)
            .balance(UPDATED_BALANCE)
            .creationDate(UPDATED_CREATION_DATE);
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(updatedClientAccount);

        restClientAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
        ClientAccount testClientAccount = clientAccountList.get(clientAccountList.size() - 1);
        assertThat(testClientAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testClientAccount.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testClientAccount.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testClientAccount.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    void putNonExistingClientAccount() throws Exception {
        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();
        clientAccount.setId(UUID.randomUUID().toString());

        // Create the ClientAccount
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(clientAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClientAccount() throws Exception {
        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();
        clientAccount.setId(UUID.randomUUID().toString());

        // Create the ClientAccount
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(clientAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClientAccount() throws Exception {
        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();
        clientAccount.setId(UUID.randomUUID().toString());

        // Create the ClientAccount
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(clientAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClientAccountWithPatch() throws Exception {
        // Initialize the database
        clientAccountRepository.save(clientAccount);

        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();

        // Update the clientAccount using partial update
        ClientAccount partialUpdatedClientAccount = new ClientAccount();
        partialUpdatedClientAccount.setId(clientAccount.getId());

        partialUpdatedClientAccount.balance(UPDATED_BALANCE).creationDate(UPDATED_CREATION_DATE);

        restClientAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClientAccount))
            )
            .andExpect(status().isOk());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
        ClientAccount testClientAccount = clientAccountList.get(clientAccountList.size() - 1);
        assertThat(testClientAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testClientAccount.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testClientAccount.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testClientAccount.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    void fullUpdateClientAccountWithPatch() throws Exception {
        // Initialize the database
        clientAccountRepository.save(clientAccount);

        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();

        // Update the clientAccount using partial update
        ClientAccount partialUpdatedClientAccount = new ClientAccount();
        partialUpdatedClientAccount.setId(clientAccount.getId());

        partialUpdatedClientAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .owner(UPDATED_OWNER)
            .balance(UPDATED_BALANCE)
            .creationDate(UPDATED_CREATION_DATE);

        restClientAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClientAccount))
            )
            .andExpect(status().isOk());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
        ClientAccount testClientAccount = clientAccountList.get(clientAccountList.size() - 1);
        assertThat(testClientAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testClientAccount.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testClientAccount.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testClientAccount.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    void patchNonExistingClientAccount() throws Exception {
        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();
        clientAccount.setId(UUID.randomUUID().toString());

        // Create the ClientAccount
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(clientAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClientAccount() throws Exception {
        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();
        clientAccount.setId(UUID.randomUUID().toString());

        // Create the ClientAccount
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(clientAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClientAccount() throws Exception {
        int databaseSizeBeforeUpdate = clientAccountRepository.findAll().size();
        clientAccount.setId(UUID.randomUUID().toString());

        // Create the ClientAccount
        ClientAccountDTO clientAccountDTO = clientAccountMapper.toDto(clientAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientAccount in the database
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClientAccount() throws Exception {
        // Initialize the database
        clientAccountRepository.save(clientAccount);

        int databaseSizeBeforeDelete = clientAccountRepository.findAll().size();

        // Delete the clientAccount
        restClientAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClientAccount> clientAccountList = clientAccountRepository.findAll();
        assertThat(clientAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

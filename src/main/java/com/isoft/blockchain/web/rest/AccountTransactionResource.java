package com.isoft.blockchain.web.rest;

import com.isoft.blockchain.repository.AccountTransactionRepository;
import com.isoft.blockchain.service.AccountTransactionService;
import com.isoft.blockchain.service.dto.AccountTransactionDTO;
import com.isoft.blockchain.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.isoft.blockchain.domain.AccountTransaction}.
 */
@RestController
@RequestMapping("/api/account-transactions")
public class AccountTransactionResource {

    private final Logger log = LoggerFactory.getLogger(AccountTransactionResource.class);

    private static final String ENTITY_NAME = "accountTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountTransactionService accountTransactionService;

    private final AccountTransactionRepository accountTransactionRepository;

    public AccountTransactionResource(
        AccountTransactionService accountTransactionService,
        AccountTransactionRepository accountTransactionRepository
    ) {
        this.accountTransactionService = accountTransactionService;
        this.accountTransactionRepository = accountTransactionRepository;
    }

    /**
     * {@code POST  /account-transactions} : Create a new accountTransaction.
     *
     * @param accountTransactionDTO the accountTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountTransactionDTO, or with status {@code 400 (Bad Request)} if the accountTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountTransactionDTO> createAccountTransaction(@RequestBody AccountTransactionDTO accountTransactionDTO)
        throws URISyntaxException {
        log.debug("REST request to save AccountTransaction : {}", accountTransactionDTO);
        if (accountTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountTransactionDTO result = accountTransactionService.save(accountTransactionDTO);
        return ResponseEntity
            .created(new URI("/api/account-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /account-transactions/:id} : Updates an existing accountTransaction.
     *
     * @param id the id of the accountTransactionDTO to save.
     * @param accountTransactionDTO the accountTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the accountTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountTransactionDTO> updateAccountTransaction(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AccountTransactionDTO accountTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountTransaction : {}, {}", id, accountTransactionDTO);
        if (accountTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountTransactionDTO result = accountTransactionService.update(accountTransactionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountTransactionDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-transactions/:id} : Partial updates given fields of an existing accountTransaction, field will ignore if it is null
     *
     * @param id the id of the accountTransactionDTO to save.
     * @param accountTransactionDTO the accountTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the accountTransactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountTransactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountTransactionDTO> partialUpdateAccountTransaction(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AccountTransactionDTO accountTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountTransaction partially : {}, {}", id, accountTransactionDTO);
        if (accountTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountTransactionDTO> result = accountTransactionService.partialUpdate(accountTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountTransactionDTO.getId())
        );
    }

    /**
     * {@code GET  /account-transactions} : get all the accountTransactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccountTransactionDTO>> getAllAccountTransactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AccountTransactions");
        Page<AccountTransactionDTO> page = accountTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-transactions/:id} : get the "id" accountTransaction.
     *
     * @param id the id of the accountTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountTransactionDTO> getAccountTransaction(@PathVariable("id") String id) {
        log.debug("REST request to get AccountTransaction : {}", id);
        Optional<AccountTransactionDTO> accountTransactionDTO = accountTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountTransactionDTO);
    }

    /**
     * {@code DELETE  /account-transactions/:id} : delete the "id" accountTransaction.
     *
     * @param id the id of the accountTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountTransaction(@PathVariable("id") String id) {
        log.debug("REST request to delete AccountTransaction : {}", id);
        accountTransactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}

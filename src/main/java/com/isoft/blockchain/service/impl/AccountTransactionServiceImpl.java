package com.isoft.blockchain.service.impl;

import com.isoft.blockchain.domain.AccountTransaction;
import com.isoft.blockchain.repository.AccountTransactionRepository;
import com.isoft.blockchain.service.AccountTransactionService;
import com.isoft.blockchain.service.dto.AccountTransactionDTO;
import com.isoft.blockchain.service.mapper.AccountTransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.isoft.blockchain.domain.AccountTransaction}.
 */
@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {

    private final Logger log = LoggerFactory.getLogger(AccountTransactionServiceImpl.class);

    private final AccountTransactionRepository accountTransactionRepository;

    private final AccountTransactionMapper accountTransactionMapper;

    public AccountTransactionServiceImpl(
        AccountTransactionRepository accountTransactionRepository,
        AccountTransactionMapper accountTransactionMapper
    ) {
        this.accountTransactionRepository = accountTransactionRepository;
        this.accountTransactionMapper = accountTransactionMapper;
    }

    @Override
    public AccountTransactionDTO save(AccountTransactionDTO accountTransactionDTO) {
        log.debug("Request to save AccountTransaction : {}", accountTransactionDTO);
        AccountTransaction accountTransaction = accountTransactionMapper.toEntity(accountTransactionDTO);
        accountTransaction = accountTransactionRepository.save(accountTransaction);
        return accountTransactionMapper.toDto(accountTransaction);
    }

    @Override
    public AccountTransactionDTO update(AccountTransactionDTO accountTransactionDTO) {
        log.debug("Request to update AccountTransaction : {}", accountTransactionDTO);
        AccountTransaction accountTransaction = accountTransactionMapper.toEntity(accountTransactionDTO);
        accountTransaction = accountTransactionRepository.save(accountTransaction);
        return accountTransactionMapper.toDto(accountTransaction);
    }

    @Override
    public Optional<AccountTransactionDTO> partialUpdate(AccountTransactionDTO accountTransactionDTO) {
        log.debug("Request to partially update AccountTransaction : {}", accountTransactionDTO);

        return accountTransactionRepository
            .findById(accountTransactionDTO.getId())
            .map(existingAccountTransaction -> {
                accountTransactionMapper.partialUpdate(existingAccountTransaction, accountTransactionDTO);

                return existingAccountTransaction;
            })
            .map(accountTransactionRepository::save)
            .map(accountTransactionMapper::toDto);
    }

    @Override
    public Page<AccountTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountTransactions");
        return accountTransactionRepository.findAll(pageable).map(accountTransactionMapper::toDto);
    }

    @Override
    public Optional<AccountTransactionDTO> findOne(String id) {
        log.debug("Request to get AccountTransaction : {}", id);
        return accountTransactionRepository.findById(id).map(accountTransactionMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AccountTransaction : {}", id);
        accountTransactionRepository.deleteById(id);
    }
}

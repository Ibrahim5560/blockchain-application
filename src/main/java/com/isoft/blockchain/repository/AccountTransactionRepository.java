package com.isoft.blockchain.repository;

import com.isoft.blockchain.domain.AccountTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AccountTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountTransactionRepository extends MongoRepository<AccountTransaction, String> {}

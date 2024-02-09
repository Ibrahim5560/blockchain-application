package com.isoft.blockchain.repository;

import com.isoft.blockchain.domain.ClientAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ClientAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientAccountRepository extends MongoRepository<ClientAccount, String> {}

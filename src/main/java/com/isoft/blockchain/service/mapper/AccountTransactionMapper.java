package com.isoft.blockchain.service.mapper;

import com.isoft.blockchain.domain.AccountTransaction;
import com.isoft.blockchain.domain.ClientAccount;
import com.isoft.blockchain.service.dto.AccountTransactionDTO;
import com.isoft.blockchain.service.dto.ClientAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountTransaction} and its DTO {@link AccountTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccountTransactionMapper extends EntityMapper<AccountTransactionDTO, AccountTransaction> {
    @Mapping(target = "clientAccount", source = "clientAccount", qualifiedByName = "clientAccountId")
    AccountTransactionDTO toDto(AccountTransaction s);

    @Named("clientAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientAccountDTO toDtoClientAccountId(ClientAccount clientAccount);
}

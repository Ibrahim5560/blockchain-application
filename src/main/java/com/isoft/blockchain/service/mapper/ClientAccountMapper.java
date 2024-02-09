package com.isoft.blockchain.service.mapper;

import com.isoft.blockchain.domain.ClientAccount;
import com.isoft.blockchain.service.dto.ClientAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClientAccount} and its DTO {@link ClientAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientAccountMapper extends EntityMapper<ClientAccountDTO, ClientAccount> {}

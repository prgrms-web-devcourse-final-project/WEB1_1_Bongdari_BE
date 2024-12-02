package com.somemore.center.usecase.query;

import java.util.UUID;

public interface CenterSignUseCase {

    UUID getIdByAccountId(String accountId);
    String getPasswordByAccountId(String accountId);
}

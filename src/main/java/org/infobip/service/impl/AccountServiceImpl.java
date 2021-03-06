package org.infobip.service.impl;

import org.infobip.request.CreateAccountRequest;
import org.infobip.response.CreateAccountResponse;
import org.infobip.domain.Account;
import org.infobip.helper.RandomStringGenerator;
import org.infobip.repository.AccountRepository;
import org.infobip.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {
    private final static int PASSWORD_LENGTH = 8;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RandomStringGenerator randomStringGenerator;

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        String requestedAccountId = createAccountRequest.getAccountId();
        Account alreadyExistsAccount = accountRepository.findByAccountId(requestedAccountId);

        if (alreadyExistsAccount != null) {
            return new CreateAccountResponse(false, "Account with that ID already exists");
        }

        String generatedPassword = randomStringGenerator.generateAlphanumericStringWithLength(PASSWORD_LENGTH);
        Account account = new Account(requestedAccountId, generatedPassword);
        accountRepository.save(account);
        return new CreateAccountResponse(true, "Your account is opened", generatedPassword);
    }

    @Override
    public Account getAccountByAccountId(String accountId) {
        return accountRepository.findByAccountId(accountId);
    }
}

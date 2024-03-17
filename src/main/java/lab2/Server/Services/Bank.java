package lab2.Server.Services;

import lab2.Server.Entity.Account;
import lab2.Server.Entity.Person;
import lab2.Server.Repos.AccountRepository;
import lab2.Server.Repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Bank {

    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;

    @Autowired
    public Bank(AccountRepository accountRepository, PersonRepository personRepository) {
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public void removeAccount(long accountId) {
        accountRepository.deleteById(accountId);
    }

    public Optional<Account> getAcount(long accountId) {
        return accountRepository.findById(accountId);
    }

    public boolean deposit(long accountId, double amount) {
        Optional<Account> accountOptional = getAcount(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.addBalance(amount);

            accountRepository.save(account);
            return true;
        }
        return false;
    }

    public Optional<Double> withdraw(long accountId, double amount) {
        Optional<Account> accountOpt = getAcount(accountId);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            if (account.getBalance() >= amount) {
                account.withdrawBalance(amount);

                accountRepository.save(account);
                return Optional.of(amount); // beetje vals spelen maar ik zie niet hoe ik ander "Remove money" en "Get money" van elkaar kan verschillen.
            }
        }
        return Optional.empty();
    }

    public boolean remove(long accountId, double amount) {
        Optional<Account> accountOpt = getAcount(accountId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            if (account.getBalance() >= amount) {
                account.removeBalance(amount);

                accountRepository.save(account);
                return true;
            }
        }
        return false;
    }

    public Optional<Double> getBalance(long accountId) {
        Optional<Account> accountOpt = getAcount(accountId);
        return accountOpt.map(Account::getBalance);
    }

    public Optional<Account> createAcount(long personID) {
        Account account = new Account();
        Optional<Person> owner = personRepository.findById(personID);
        if (owner.isPresent()) {
            account.addOwner(owner.get());
            owner.get().addAccount(account);
            return Optional.of(this.addAccount(account));
        }
        return Optional.empty();
    }

    public boolean addPersonToAccount(long personID, long accountID) {
        Optional<Account> accountOptional = accountRepository.findById(accountID);
        if (accountOptional.isEmpty()) {
            return false;
        }

        Optional<Person> personOptional = personRepository.findById(personID);
        if (personOptional.isEmpty()) {
            return false;
        }

        Account account = accountOptional.get();
        Person person = personOptional.get();

        account.addOwner(person);
        accountRepository.save(account);
        return true;

    }

    public boolean checkPrivalge(long userId, long accountId){
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        Optional<Person> personOptional = personRepository.findById(userId);

        if (accountOptional.isEmpty()){
            return false;
        }

        Account account = accountOptional.get();
        Person person = personOptional.get();

        return account.isOwner(person);
    }
}

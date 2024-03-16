package lab2.Server.Controllers;

import lab2.Server.Entity.Account;
import lab2.Server.Services.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private final Bank bank;

    public AccountController(Bank bank){
        this.bank = bank;
    }

    // PUT
    @PutMapping("/{accountId}/deposit")
    public ResponseEntity<?> addBalance(@RequestParam long personId, @PathVariable long accountId, @RequestParam double amount) {
        if (!bank.checkPrivalge(personId, accountId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (bank.deposit(accountId, amount)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("{\"error\": \"Deposit failed\"}");
    }

    @PutMapping("/{accountId}/person")
    public ResponseEntity<?> addPersonToAccount(@RequestParam long personId, @PathVariable long accountId) {
        if (bank.addPersonToAccount(personId, accountId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("{\"error\": \"couldn't add person to account\"}");
    }

    // DELETE
    @DeleteMapping("/{accountId}/withdrawals")
    public ResponseEntity<?> withdrawBalance(@RequestParam long personId, @PathVariable long accountId, @RequestParam double amount) {
        if (!bank.checkPrivalge(personId, accountId)){
            return ResponseEntity.badRequest().body("{\"error\": \"you can't\"}");
        }

        Optional<Double> withdrawl = bank.withdraw(accountId, amount);
        if(withdrawl.isPresent()) {
            return ResponseEntity.ok().body("here is your €" + withdrawl.get());
        }
        return ResponseEntity.badRequest().body("{\"error\": \"Withdrawal failed\"}");
    }

    // GET
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@RequestParam long personId, @PathVariable long accountId){
        if (!bank.checkPrivalge(personId, accountId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Double> accountOptional = bank.getBalance(accountId);
        if (accountOptional.isPresent()){
            HashMap<String, Object> ans = new HashMap<>();
            ans.put("balance", accountOptional.get());
            return ResponseEntity.ok(ans);
        }

        return ResponseEntity.notFound().build();
    }

    // POST
    @PostMapping("/")
    private ResponseEntity<HashMap<String, Object>> createAccount(@RequestParam long personId){
        Optional<Account> newAccount = bank.createAcount(personId);

        if (newAccount.isPresent()){
            HashMap<String, Object> ans = new HashMap<>();
            ans.put("accountID", newAccount.get().getId());
            return ResponseEntity.ok().body(ans);
        }
        return ResponseEntity.badRequest().build();
    }

}

package lab2.Server.Entity;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToMany
    @JoinTable(
            name = "person_account",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> owners;

    private Double balance;

    public Account(Person ... persons){
        owners = new HashSet<>();
        owners.addAll(Arrays.asList(persons));
        balance = 0.0;
    }

    public Account() {
        owners = new HashSet<>();
        balance = 0.0;
    }


    public void addOwner(Person ... persons){
        owners.addAll(Arrays.asList(persons));
    }

    public boolean isOwner(Person person){
        return owners.contains(person);
    }

    public void removeOwner(Person ... persons){
        Arrays.asList(persons).forEach(person -> {
            person.removeAccount(this);
            owners.remove(person);
        });
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void addBalance(Double balance) {
        this.balance += balance;
    }

    public long getId() {
        return id;
    }

    public void removeBalance(Double balance) {
        this.balance -= balance;
    }

    public void withdrawBalance(Double balance) {
        this.balance -= balance;
    }
}

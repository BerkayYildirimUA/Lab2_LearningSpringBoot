package lab2.Server.Entity;

import jakarta.persistence.*;

import java.util.*;


@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(mappedBy = "owners")
    private Set<Account> myAccounts;

    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAccount(Account ... accounts){
        myAccounts.addAll(Arrays.asList(accounts));
    }

    public void removeAccount(Account ... accounts){
        Arrays.asList(accounts).forEach(myAccounts::remove);
    }
}

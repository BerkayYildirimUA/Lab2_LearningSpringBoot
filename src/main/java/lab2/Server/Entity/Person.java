package lab2.Server.Entity;

import jakarta.persistence.*;

import java.util.*;


@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(mappedBy = "owners")
    private Set<Account> myAcounts;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAcount(Account ... accounts){
        myAcounts.addAll(Arrays.asList(accounts));
    }

    public void removeAcount(Account ... accounts){
        Arrays.asList(accounts).forEach(myAcounts::remove);
    }
}

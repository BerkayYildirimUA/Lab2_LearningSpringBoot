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

    private Double balence;

    public Account(Person ... persons){
        owners = new HashSet<>();
        owners.addAll(Arrays.asList(persons));
        balence = 0.0;
    }

    public Account() {
        owners = new HashSet<>();

        balence = 0.0;
    }


    public void addOwner(Person ... persons){
        owners.addAll(Arrays.asList(persons));
    }

    public void removeOwner(Person ... persons){
        Arrays.asList(persons).forEach(person -> {
            person.removeAcount(this);
            owners.remove(person);
        });
    }

    public Double getBalence() {
        return balence;
    }

    public void setBalence(Double balence) {
        this.balence = balence;
    }

    public void addBalence(Double balence) {
        this.balence += balence;
    }

    public long getId() {
        return id;
    }

    public void removeBalence(Double balence) {
        this.balence -= balence;
    }

    public void withdrawBalence(Double balence) {
        this.balence -= balence;
    }
}

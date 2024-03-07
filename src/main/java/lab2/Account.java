package lab2;

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
    }

    public Account() {
        owners = new HashSet<>();
    }


    public void addOwner(Person person){
        owners.add(person);
    }

    public void removeOwner(Person person){
        owners.remove(person);
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


    public void removeBalence(Double balence) {
        this.balence -= balence;
    }
}

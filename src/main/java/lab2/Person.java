package lab2;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
}

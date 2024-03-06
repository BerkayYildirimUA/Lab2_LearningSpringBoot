package lab1.lab2;

import jakarta.persistence.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person {

    private @Id @GeneratedValue Long id;

}

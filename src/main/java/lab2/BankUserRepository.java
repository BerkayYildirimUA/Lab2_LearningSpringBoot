package lab2;

import org.springframework.data.repository.CrudRepository;

public interface BankUserRepository extends CrudRepository<Person, Long> {

    Person findByName(String name);
}

package lab2.Server.Controllers;

import lab2.Server.Entity.Person;
import lab2.Server.Services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/create")
    public ResponseEntity<Person> createPerson(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        System.out.println(name);

        Person person = new Person();
        person.setName(name);

        Person savedPerson = personService.createPerson(person);
        return ResponseEntity.ok(savedPerson);
    }
}


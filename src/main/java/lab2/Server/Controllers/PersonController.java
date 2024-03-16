package lab2.Server.Controllers;

import lab2.Server.Entity.Person;
import lab2.Server.Services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<HashMap<String, Object>> createPerson(@RequestParam String name) {
        Person person = new Person();
        person.setName(name);
        Person savedPerson = personService.createPerson(person);

        HashMap<String, Object> ans = new HashMap<>();
        ans.put("personID", savedPerson.getId());
        ans.put("name", savedPerson.getName());

        return ResponseEntity.ok().body(ans);
    }
}


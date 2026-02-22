package library.services;

import library.models.Person;
import library.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index(){
        return peopleRepository.findAll();
    }

    public Person show(int id){
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    public Optional<Person> show(String fullName){
        return peopleRepository.findByFullName(fullName);
    }

    @Transactional
    public void create(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void edit(int id, Person updatedPerson){
        updatedPerson.setPersonId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }
}
package library.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private int personId;

    @NotEmpty(message = "Поле не може бути пустим")
    private String fullName;
    @NotNull(message = "Рік народження обовʼязковий")
    @Min(value = 1920, message = "Мінімальний дозволений рік: 1920")
    private int birthYear;

    public Person(){}

    public Person(String fullName, int birthYear) {
        this.fullName = fullName;
        this.birthYear = birthYear;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}

import java.time.LocalDate;

//TODO: Add profile list with data persistence
public class Profile {

    String name;
    LocalDate birthDate;
    int expectedAge;

    public Profile(String name, LocalDate birthDate, int expectedAge) {
        this.name = name;
        this.birthDate = birthDate;
        this.expectedAge = expectedAge;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getExpectedAge() {
        return expectedAge;
    }

    public void setExpectedAge(int expectedAge) {
        this.expectedAge = expectedAge;
    }
    
    
}

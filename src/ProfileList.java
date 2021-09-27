import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ProfileList {

    static ObservableList<Profile> profileList = FXCollections.observableArrayList(Arrays.asList(
            new Profile("I Gusti Bagus Ananda", LocalDate.of(1998, 7,18)),
            new Profile("Jessica Iskandar", LocalDate.of(1988, 1, 29))
    ));

    static ObservableList<Profile> getAllProfile() {
        return profileList;
    }

    static ArrayList<String> getAllProfileName() {
        ArrayList<String> tempArray = new ArrayList<>();

        profileList.forEach(e -> {
            tempArray.add(e.getName());
        });

        return tempArray;
    }

    static Profile getProfile(int index) {
        return profileList.get(index);
    }
}

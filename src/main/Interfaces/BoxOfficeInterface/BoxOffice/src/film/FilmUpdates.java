package film;

import java.util.ArrayList;
import java.util.List;

// Film Updates Implementation
public class FilmUpdates implements FilmUpdatesInterface {
    @Override
    public List<String> getFilmSchedule() {
        return new ArrayList<>(); // Retrieve actual film schedule
    }

    @Override
    public void notifyFilmChange(String filmID) {
        // Implement notification logic
    }
}
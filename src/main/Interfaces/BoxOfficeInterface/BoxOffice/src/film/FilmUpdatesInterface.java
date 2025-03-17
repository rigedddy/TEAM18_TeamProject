package film;

import java.util.List;

// Film Updates Interface
public interface FilmUpdatesInterface {
    List<String> getFilmSchedule(); // Retrieves the films that have been booked in the Main Hall
    void notifyFilmChange(String filmID); // Notifies Box Office of film schedule changes
}




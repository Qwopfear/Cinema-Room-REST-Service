package cinema.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class SeatNotAvailableException extends RuntimeException {
    public SeatNotAvailableException(){
        super("The ticket has been already purchased!");
    }
}

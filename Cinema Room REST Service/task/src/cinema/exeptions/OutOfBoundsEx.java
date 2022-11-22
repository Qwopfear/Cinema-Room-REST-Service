package cinema.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The number of a row or a column is out of bounds!")
public class OutOfBoundsEx extends RuntimeException{
    public OutOfBoundsEx() {
        super("The number of a row or a column is out of bounds!");
    }
}

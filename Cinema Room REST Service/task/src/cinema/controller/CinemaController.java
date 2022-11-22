package cinema.controller;


import cinema.components.Cinema;
import cinema.components.Seat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Supplier;

@RestController
public class CinemaController {

    private final Cinema cinema;

    public CinemaController() {
        this.cinema = new Cinema(9, 9);
    }


    @GetMapping("/seats")
    public Cinema getCinema() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody Seat seat) {
        if ((seat.getRow() < 1
                || seat.getColumn() < 1)
                || (seat.getRow() > cinema.getTotalRows()
                || seat.getColumn() > cinema.getTotalColumns())) {
            return new ResponseEntity<>(
                    """
                            {
                              "error": "The number of a row or a column is out of bounds!"
                            }""",
                    HttpStatus.BAD_REQUEST);
        }
        Seat s = cinema.getAvailableSeats().get((seat.getRow() - 1) * cinema.getTotalRows() + seat.getColumn() - 1);

        if (s.isBuying()) {
            return new ResponseEntity<>(
                    """
                            {
                              "error": "The ticket has been already purchased!"
                            }
                            """,
                    HttpStatus.BAD_REQUEST);
        }
        s = cinema.buyTicket(seat);

        return new ResponseEntity<>(
                String.format(
                        "{\n\"token\": \"%s\",\n \"ticket\": {\n\"row\": %s,\n\"column\": %s,\n\"price\": %s\n}\n} ",
                        s.getToken(), s.getRow(), s.getColumn(), s.getPrice()),
                HttpStatus.OK);
    }


    @PostMapping("/return")
    public ResponseEntity<?> returnSeat(@RequestBody String uuid) {


        Seat seat = cinema.findByToken(uuid).orElse(null);
        if (seat != null) {
            seat.setBuying(false);
            cinema.returnTicket(seat);
            return new ResponseEntity<>(
                    String.format("{\n\t\"returned_ticket\": {\n\t \"row\": %s,\n \"column\": %s,\n \"price\": %s\n\t}\n}",
                            seat.getRow(), seat.getColumn(), seat.getPrice()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>("{\n\t\"error\": \"Wrong token!\"\n}", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stats")
    public ResponseEntity<?> getStatistic(@RequestBody(required = false) String password){
        if (password!=null){
            System.out.println(password);
        }
        try {
            if (password != null && password.contains("super_secret")) {
                return new ResponseEntity<>(String.format("{\n\t\"current_income\" : %s," +
                                "\n\t\"number_of_available_seats\": %s," +
                                "\n\t\"number_of_purchased_tickets\": %s\n}"
                        , cinema.getCurrentIncome(), cinema.countFreeSeats(), cinema.countBuyingSeats()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("""
                    {
                        "error": "The password is wrong!"
                    }
                    """,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            return new ResponseEntity<>("""
                    {
                        "error": "The password is wrong!"
                    }
                    """,HttpStatus.UNAUTHORIZED);
        }

    }
}

package cinema.components;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@JsonPropertyOrder({
        "total_rows",
        "total_columns",
        "available_seats"
})
public class Cinema {

    @JsonProperty("total_rows")
    private int totalRows;
    @JsonProperty("total_columns")
    private int totalColumns;
    @JsonProperty("available_seats")
    private List<Seat> availableSeats;

    @JsonIgnore
    private long currentIncome = 0;

    public Cinema(int totalRows,int totalColumns){
        this.totalColumns = totalColumns;
        this.totalRows = totalRows;
        this.availableSeats = new ArrayList<>();
        generateSeats();
    }

    private void generateSeats(){
        for (int r = 1; r <= totalRows ; r++) {
            for (int c = 1; c <= totalColumns ; c++) {
                availableSeats.add(new Seat(r,c));
            }
        }
    }


    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(int totalColumns) {
        this.totalColumns = totalColumns;
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(List<Seat> availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Seat buyTicket(Seat seat){
        seat.countPrice();
        availableSeats.set((seat.getRow() - 1) * totalRows + seat.getColumn() - 1,seat);
        availableSeats.get((seat.getRow() - 1) * totalRows + seat.getColumn() - 1).setToken(UUID.randomUUID());
        currentIncome+=seat.getPrice();
        return  availableSeats.get((seat.getRow() - 1) * totalRows + seat.getColumn() - 1);
    }


    public Optional<Seat> findByToken(String uuid) throws NoSuchElementException{

        System.out.println("uuid");
        System.out.println(uuid);
        final String cmp = uuid.split(":")[1].replaceAll("\"","").replaceAll("}","");
        System.out.println("cmp");
        System.out.println(cmp);
        return availableSeats.stream().filter(el -> el.getToken().toString().equals(cmp)).findFirst();

    }

    @JsonIgnore
    public long getCurrentIncome() {
        return currentIncome;
    }

    public int countBuyingSeats(){
        return (int) availableSeats.stream().filter(Seat::isBuying).count();
    }

    public int countFreeSeats(){
        return (int) availableSeats.stream().filter(el -> !el.isBuying()).count();
    }

    public void returnTicket(Seat seat){
        currentIncome -= seat.getPrice();

    }
}

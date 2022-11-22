package cinema.components;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Comparator;
import java.util.UUID;

public class Seat implements Comparator<Seat> {
    private int row;
    private int column;
    private int price;

    private UUID token;
    @JsonIgnore
    private boolean isBuying = false;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.token = UUID.randomUUID();
        countPrice();
    }

    public Seat(){
        isBuying = true;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void countPrice(){
        this.price = row > 4 ? 8 : 10;
    }

    @Override
    public int compare(Seat o1, Seat o2) {
        if (o1.getRow() - o2.getRow() != 0)
            return o1.getRow() - o2.getRow();
        else
            return o1.getColumn() - o2.getColumn();
    }

    @JsonIgnore

    public boolean isBuying() {
        return isBuying;
    }

    public void setBuying(boolean buying) {
        isBuying = buying;
    }

    @JsonIgnore
    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}

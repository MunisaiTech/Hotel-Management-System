package com.munisai;

import jakarta.persistence.*;


@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="available")
    private boolean available;
    @Column(name="room_number")
    private int room_number;
    @Column(name="type")
    private String type;
    @Column(name="price_per_night")
    private int price_per_night;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice_per_night() {
        return price_per_night;
    }

    public void setPrice_per_night(int price_per_night) {
        this.price_per_night = price_per_night;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", available=" + available +
                ", room_number=" + room_number +
                ", type='" + type + '\'' +
                ", price_per_night=" + price_per_night +
                '}';
    }
}

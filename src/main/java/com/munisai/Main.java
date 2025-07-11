package com.munisai;


import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class Main{


    public static void addRoom(Room room){
        Transaction transaction;
        try (Session session = Utility.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(room);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void addCustomer(Customer customer){
        Transaction transaction;
        try (Session session = Utility.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(customer);
            transaction.commit();
        }

    }

    public static void allRooms() {
        try (Session session = Utility.getSessionFactory().openSession()) {
            String hql = "from Room";
            Query qry = session.createQuery(hql, Room.class);
            List<Room> rooms = qry.getResultList();

            if (rooms.isEmpty()) {
                System.out.println("No rooms added at...");
            } else {
                System.out.println("---Rooms Data---");
                for (Room room : rooms) {
                    System.out.println(room);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bookRoom(Scanner sc) {
        try (Session session = Utility.getSessionFactory().openSession()) {
            Transaction ts = session.beginTransaction();
            String hql = "from Room where available = true";
            Query qry = session.createQuery(hql, Room.class);
            List<Room> availRooms = qry.getResultList();

            if (availRooms.isEmpty()) {
                System.out.println("No rooms available today...");
                ts.commit(); // still commit even if no action, to close transaction cleanly
                return;
            }

            System.out.println("Available Rooms");
            for (Room room : availRooms) {
                System.out.println(room);
            }

            System.out.print("Enter customer id: ");
            int cid = sc.nextInt();
            sc.nextLine();

            Customer customer = session.find(Customer.class, cid);
            if (customer == null) {
                System.out.println("Invalid customer id");
                ts.rollback();
                return;
            }

            System.out.print("Enter room id to book: ");
            int rid = sc.nextInt();
            sc.nextLine();

            Room room = session.find(Room.class, rid);
            if (room == null || !room.isAvailable()) {
                System.out.println("Invalid or already booked room id");
                ts.rollback();
                return;
            }

            System.out.print("Enter check-in-date like(YYYY-MM-DD): ");
            String checkInput = sc.nextLine();
            LocalDate checkIn = LocalDate.parse(checkInput);

            System.out.print("Enter check-out-date like(YYYY-MM-DD): ");
            String checkOutInput = sc.nextLine();
            LocalDate checkOut = LocalDate.parse(checkOutInput);

            long noOfDays = ChronoUnit.DAYS.between(checkIn, checkOut);
            long totalPrice = noOfDays * room.getPrice_per_night();

            Booking booking = new Booking();
            booking.setRoom(room);
            booking.setCustomer(customer);
            booking.setCheckIn(checkIn);
            booking.setCheckOut(checkOut);
            booking.setTotalPrice(totalPrice);

            room.setAvailable(false);
            session.persist(booking);
            ts.commit();

            System.out.println("Customer " + customer.getName() + " booked room-" + rid + " successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void viewAllBookings() {
        try (Session session = Utility.getSessionFactory().openSession()) {
            Transaction ts = session.beginTransaction();
            String hql = "from Booking";
            Query qry = session.createQuery(hql, Booking.class);
            List<Booking> bookings = qry.getResultList();
            ts.commit();

            if (bookings.isEmpty()) {
                System.out.println("No bookings found till now");
            } else {
                System.out.println("---Bookings Data---");
                for (Booking booking : bookings) {
                    System.out.println(booking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }











    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        while(true){
            System.out.println("\n************* Hotel Management System *************");
            System.out.println("1.Add a room");
            System.out.println("2.Add a customer");
            System.out.println("3.View all rooms");
            System.out.println("4.Book a room");
            System.out.println("5.View all bookings");
            System.out.println("6.Exit");

            System.out.print("Enter your choice: ");
            int choice=sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1:
                    System.out.print("Enter room number: ");
                    int roomNum=sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter room type: ");
                    String type=sc.nextLine();
                    System.out.print("Enter price per night: ");
                    int price=sc.nextInt();
                    sc.nextLine();
                    Room room=new Room();
                    room.setRoom_number(roomNum);
                    room.setType(type);
                    room.setPrice_per_night(price);
                    room.setAvailable(true);
                    addRoom(room);
                    System.out.println("Room "+roomNum+" added successfully");
                    break;
                case 2:
                    System.out.print("Enter customer name: ");
                    String name=sc.nextLine();
                    System.out.print("Enter phone number: ");
                    long num=sc.nextLong();
                    sc.nextLine();
                    System.out.print("Enter email: ");
                    String email=sc.nextLine();
                    Customer customer=new Customer();
                    customer.setName(name);
                    customer.setEmail(email);
                    customer.setPhone(num);
                    addCustomer(customer);
                    System.out.println("Customer "+name+" added successfully");
                    break;
                case 3:
                    allRooms();
                    break;
                case 4:
                    bookRoom(sc);
                    break;
                case 5:
                    viewAllBookings();
                    break;
                case 6:
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice...");

            }

        }

    }
}
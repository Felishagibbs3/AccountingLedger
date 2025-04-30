package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountLedgerApp {
    static Scanner myScanner = new Scanner(System.in);
    static final String csvFile = "transactions.csv";


    public static void main (String[]args) throws IOException {
        List<String> ledger = new ArrayList<>();

        boolean running = true;
        while (running) {
            System.out.println("\n ------ Ledger Main Menu ------");
            System.out.println("What would you like to do?");
            System.out.println("1. Add Deposit");
            System.out.println("2. Add Payment");
            System.out.println("3. View Ledger");
            System.out.println("4. Exit");
            int choice = myScanner.nextInt();
            myScanner.nextLine();

            switch (choice) {
                case 1 -> addDeposit();
                case 2 -> addPayment();
                case 3 -> showLedger();
                case 4 -> running = false;
                default -> System.out.println("Invalid input");

            }
        }

    }
    public static class Deposit {
         String name;
         String accountNumber;
         double amount;
         String date;
         String vendor;
         LocalTime time  = LocalTime.of(11,15, 0);

         // constructor setup for deposit
        public Deposit(String name, String accountNumber, double amount, String date, String vendor, String time) {
            this.name = name;
            this.accountNumber = accountNumber;
            this.amount = amount;
            this.date = date;
            this.vendor = vendor;
            this.time = LocalTime.parse(time);

        }

    }

    public static class Payment {
         String nameOnCard;
         String cardNumber;
         String expire;
         double amountPay;
         String datePayed;
         String vendor;


        public Payment(String nameOnCard, String cardNumber, String expire, double amountPay, String datePayed) {
            this.nameOnCard = nameOnCard;
            this.cardNumber = cardNumber;
            this.expire = expire;
            this.amountPay = amountPay;
            this.datePayed = datePayed;

        }


    }

    public static void addDeposit() {
        System.out.println("Enter name");
        String nameOnAccount = myScanner.nextLine();
        System.out.println("Enter account number");
        String account = myScanner.nextLine();
        System.out.println("Enter Deposit amount");
        double amount = myScanner.nextDouble();
        System.out.println("Vendor");
        String vendor = myScanner.nextLine();
        LocalDate date = LocalDate.of(2023, 04, 15);




        // using buffered writer to write deposit information to the csv file
            try {
                BufferedWriter bufWriter = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
                bufWriter.write("Name, Account Number, Amount, Description,Vendor,Date");
                bufWriter.newLine();
                bufWriter.write(nameOnAccount + "|" + " " + account + "|" + " " + (double) amount + "|" + " " + vendor + "|" + " " + date + "|" + " " + LocalDateTime.now());
                bufWriter.newLine();
                System.out.println("Deposit saved");
                System.out.println("We have saved your deposit information!");

                bufWriter.close();

            } catch (IOException e) {
                System.out.println("An error occurred. Couldn't write to file" + e.getMessage());


            }


        }

    public static void addPayment() throws IOException {
        System.out.println("Enter name");
        String name = myScanner.nextLine();
        System.out.println("Enter card number");
        String card = myScanner.nextLine();
        System.out.println("Expiration Date (MM/YY");
        String expire = myScanner.nextLine();
        System.out.println("Payment Amount");
        double payment = myScanner.nextDouble();
        String date = LocalDateTime.now().toString();

        Payment Payment = new Payment(name, card, expire, payment, date);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, true))) {
            bw.write(Payment.toString());
            bw.newLine();
            System.out.println("Successful Payment");


            bw.close();


        } catch (IOException e) {
            System.out.println("Could not process payment: " + e.getMessage());
        }


    }

    public static void showLedger() {
        System.out.println("Date|Time|Name|Account Number|Description|Vendor|Amazon|Amount");
        System.out.println("2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50");
        System.out.println("2023-04-15|11:15:00|ergonomic keyboard|Invoice 1001 paid||Joe|1500.00");
    }
}

package com.pluralsight;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountingLedger {

    static Scanner scanner = new Scanner(System.in);
    String csvFile = "src/main/resources/transactions.csv";
    static DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static LocalDateTime timeNow = LocalDateTime.now();


    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("HOME PAGE");
            System.out.println("What awould you like to do?");
            System.out.println("1. Add Deposit");
            System.out.println("2. Make Payment");
            System.out.println("3.Ledger Details");
            System.out.println("4. Exit");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addTransaction(true);
                    break;
                case "2":
                    addTransaction(false);
                    break;
                case "3":
                    showLedger();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option chosen");

            }


        }
    }


    private static void addTransaction(boolean isDeposit) {
        try {
            System.out.println("Description");
            String description = scanner.nextLine();

            System.out.println("Vendor");
            String vendor = scanner.nextLine();

            System.out.println("Amount");
            double amount = Double.parseDouble(scanner.nextLine());
            if (!isDeposit) amount *= -1;
            LocalDateTime now = LocalDateTime.now();
            String date = now.toLocalDate().toString();
            String time = now.toLocalTime().withNano(0).toString();
            String transaction = String.format("%s| %s| %s| %s| %-10.2f",
                    date, time, description, vendor, amount);

            BufferedWriter bufWriter = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
            bufWriter.write(transaction);
            bufWriter.newLine();
            bufWriter.close();
            System.out.println("Transaction ready to view.");

        } catch (IOException e) {
            System.out.println("Error saving transactions");
        }
    }
    private static void displayDebitEntries() {

        try {
            System.out.println("Debit Entries");
            BufferedReader bufReader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
            String line;
            while ((line = bufReader.readLine()) != null) {
                String[] transactionParts = line.split("\\|");
                if (Double.parseDouble(transactionParts[4]) < 0) {
                    System.out.println(line);

                }


            }

        } catch (IOException e) {
            System.out.println("Error loading transactions");
        }
    }
    private static void showLedger() {
        boolean ledgerRun = true;
        while (ledgerRun) {
            System.out.println("Ledger Menu");
            System.out.println("What would you like to do?");
            System.out.println("1. List All Entries");
            System.out.println("2. Deposits");
            System.out.println("3. Payment");
            System.out.println("4. View Reports");
            System.out.println("5. Exit to home");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayEntries();
                    break;
                case "2":
                    addTransaction(true);
                    break;
                case "3":
                    displayDebitEntries();
                    break;
                case "4":
                    showReports();
                    break;
                case "5":
                    ledgerRun = false;
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }

    }

    public static ArrayList<Transaction> getTransactions() throws FileNotFoundException {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        //use getFileReader() to get us a buffered reader for inventory.csv
        BufferedReader bufReader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));

        //read the file line by line
        try {

            String line;
            while ((line = bufReader.readLine()) != null) {
                //split the line into the individual product parts
                String[] transactionParts = line.split("\\|");
                //generate a new product using the correct data types for the product attributes
                Transaction newTransaction = new Transaction(LocalDate.parse(transactionParts[0]),LocalTime.parse(transactionParts[1]),transactionParts[2],transactionParts[3],Double.parseDouble(transactionParts[4]));
                //add the product to our inventory ArrayList
                transactions.add(newTransaction);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }




        //return the inventory
        return transactions;
    }

    private static void displayEntries() {

        try {
            System.out.println("All Entries");
            BufferedReader bufReader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
            String line;
            while ((line = bufReader.readLine()) != null) {
                System.out.println(line);

            }

        } catch (IOException e) {
            System.out.println("Error loading transactions");
        }
    }

    private static void showReports() {
        boolean reportsRunning = true;
        while (reportsRunning) {
            System.out.println("View Reports");
            System.out.println("Sort by");
            System.out.println("1. Month to Date");
            System.out.println("2. Previous Month");
            System.out.println("3. Year to Date");
            System.out.println("4. Previous Year");
            System.out.println("5. Search by Vendor");
            System.out.println("6. Go back");
            int choice = scanner.nextInt();
            LocalDate today = LocalDate.now();
            LocalDate start;
            LocalDate end = today;
            List<AccountingLedger> filter = new ArrayList<>();

            switch (choice) {
                case 1:
                    start = today.withDayOfMonth(1);
                    System.out.println("From" + start + "to" + end);
                    break;

                case 2:
                    YearMonth previous = YearMonth.from(today).minusMonths(1);
                    start = previous.atDay(1);
                    end = previous.atEndOfMonth();
                    System.out.println("From " + start + "to" + end);
                    break;
                case 3:
                    start = today.withDayOfYear(1);
                    System.out.println("From " + start + "to" + end);
                    break;
                case 4:
                    start = today.minusYears(1).withDayOfYear(1);
                    end = today.minusYears(1).withDayOfYear(today.minusYears(1).lengthOfYear());
                    System.out.println("From " + start + "to" + end);
                    break;
                case 5:
                    searchByVendor();
                    ;


                    break;

                case 6:
                    System.out.println("Returning to home");
                    reportsRunning = false;
                    return;
            }


        }
    }

    private static void searchByVendor() {
        System.out.print("Enter vendor name to search: ");
        String vendor = scanner.nextLine().toLowerCase();

        System.out.println("\nVendor Entries:");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("|" + vendor + "|")) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
        }
        System.out.println("Error reading file.");
    }


}












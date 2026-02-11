package main.java.grid;
import java.io.*;
import java.util.*;

public class App {
    private Scanner scanner;

    public App(InputStream input) {
        this.scanner = new Scanner(input);
    }

    public void run() {
        //comanda
        String lineCommand=scanner.nextLine();
        GridController grid = new GridController();
        while(!lineCommand.equals("7")){
            String [] command=lineCommand.split(" ");
            //identificare elemente din comanda
            int commandNumber=-1;
            //tipul comenzii si tratare cazuri de eroare
            try {
                    commandNumber = Integer.parseInt(command[0]);
            }catch (IllegalArgumentException e){
                System.out.println("EROARE: Comanda necunoscuta.");
            }
            if(commandNumber>7)
                System.out.println("EROARE: Comanda necunoscuta.");

            if(commandNumber==0){
                //adaugare producator
                if(command[1].equals("solar") || command[1].equals("reactor") || command[1].equals("turbina") ){
                    if(command.length==4) {
                        //verificare numar parametri
                        double power = Double.parseDouble(command[3]);
                        if (power > 0.0) {
                            String adaugare = grid.addProducer(command[1], command[2], power);
                            System.out.println(adaugare);
                        } else {
                            System.out.println("EROARE: Putere invalida\n");
                        }
                    } else {
                        System.out.println("EROARE: Format comanda invalid\n");
                    }
                }
                else {
                    System.out.println("EROARE: Tip producator invalid\n");
                }
            }

            if(commandNumber==1){
                //adaugare consumator
                if(command[1].equals("suport_viata") || command[1].equals("laborator") || command[1].equals("iluminat")){
                    if(command.length==4) {
                        //verificare numar parametri
                        double maxCapacity = Double.parseDouble(command[3]);
                        if (maxCapacity > 0.0) {
                            String adaugare = grid.addConsumer(command[1], command[2], maxCapacity);
                            System.out.println(adaugare);
                        } else {
                            System.out.println("EROARE: Cerere putere invalida\n");
                        }
                    } else {
                        System.out.println("EROARE: Format comanda invalid\n");
                    }
                }
                else {
                    System.out.println("EROARE: Tip consumator invalid\n");
                }
            }

            if(commandNumber==2){
                //adaugare baterie
                if(command.length==3) {
                    //verificare numar parametri
                    double capacitate_max = Double.parseDouble(command[2]);
                    if (capacitate_max > 0.0) {
                        String adaugare = grid.addBattery(command[1], capacitate_max);
                        System.out.println(adaugare);
                    } else {
                        System.out.println("EROARE: Capacitate invalida\n");
                    }
                } else {
                    System.out.println("EROARE: Format comanda invalid\n");
                }
            }

            if(commandNumber==3){
                if(command.length==3){
                    //verificare numar parametri
                    try{
                        //tipul parametrilor sa fie double
                        double sunFactor=Double.parseDouble(command[1]);
                        double windFactor=Double.parseDouble(command[2]);
                        String simulationAnswer=grid.simulateTick(sunFactor, windFactor);
                        System.out.println(simulationAnswer);
                    } catch (IllegalArgumentException e) {
                        System.out.println("EROARE: Factori invalizi");
                    }
                }
                else{
                    System.out.println("EROARE: Format comanda invalid\n");
                }
            }

            if(commandNumber==4){
                //verificare tip status
                if(command[2].equals("true") || command[2].equals("false")){
                    boolean status=Boolean.parseBoolean(command[2]);
                    String verification=grid.statusVerification(command[1],status);
                    System.out.println(verification);
                }
                else System.out.println("EROARE: Status invalid\n");
            }

            if(commandNumber==5){
                String answer=grid.NetworkState();
                System.out.println(answer);
            }

            if(commandNumber==6){
                String history=grid.historyTick();
                if(history.equals("")){
                    System.out.println("Istoric evenimente gol");
                }
                else
                    System.out.println(history);
            }
            //citire urmatoarea comanda
            lineCommand=scanner.nextLine();
        }
        if(lineCommand.equals("7")) {
            System.out.println("Simulatorul se inchide.\n");
        }

    }

    public static void main(String[] args) {
        App app = new App(System.in);
        app.run();
    }
}
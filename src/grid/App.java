package grid;

import main.java.Tema1.GridController;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.text.*;

public class App {
    private Scanner scanner;

    public App(InputStream input) {
        this.scanner = new Scanner(input);
    }

    public void run() {
        //comanda
        String linieComanda=scanner.nextLine();
        GridController grid = new GridController();
        while(!linieComanda.equals("7")){
            String [] comanda=linieComanda.split(" ");
            //identificare elemente din comanda
            int nr_comanda=-1;
            //tipul comenzii si tratare cazuri de eroare
            try {
                    nr_comanda = Integer.parseInt(comanda[0]);
            }catch (IllegalArgumentException e){
                System.out.println("EROARE: Comanda necunoscuta.");
            }
            if(nr_comanda>7)
                System.out.println("EROARE: Comanda necunoscuta.");

            if(nr_comanda==0){
                //adaugare producator
                if(comanda[1].equals("solar") || comanda[1].equals("reactor") || comanda[1].equals("turbina") ){
                    if(comanda.length==4) {
                        //verificare numar parametri
                        double putere = Double.parseDouble(comanda[3]);
                        if (putere > 0.0) {
                            String adaugare = grid.adaugareProducator(comanda[1], comanda[2], putere);
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

            if(nr_comanda==1){
                //adaugare consumator
                if(comanda[1].equals("suport_viata") || comanda[1].equals("laborator") || comanda[1].equals("iluminat")){
                    if(comanda.length==4) {
                        //verificare numar parametri
                        double capacitate_max = Double.parseDouble(comanda[3]);
                        if (capacitate_max > 0.0) {
                            String adaugare = grid.adaugareConsumator(comanda[1], comanda[2], capacitate_max);
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

            if(nr_comanda==2){
                //adaugare baterie
                if(comanda.length==3) {
                    //verificare numar parametri
                    double capacitate_max = Double.parseDouble(comanda[2]);
                    if (capacitate_max > 0.0) {
                        String adaugare = grid.adaugareBaterie(comanda[1], capacitate_max);
                        System.out.println(adaugare);
                    } else {
                        System.out.println("EROARE: Capacitate invalida\n");
                    }
                } else {
                    System.out.println("EROARE: Format comanda invalid\n");
                }
            }

            if(nr_comanda==3){
                if(comanda.length==3){
                    //verificare numar parametri
                    try{
                        //tipul parametrilor sa fie double
                        double factorSoare=Double.parseDouble(comanda[1]);
                        double factorVant=Double.parseDouble(comanda[2]);
                        String raspunsSimulare=grid.simuleazaTick(factorSoare, factorVant);
                        System.out.println(raspunsSimulare);
                    } catch (IllegalArgumentException e) {
                        System.out.println("EROARE: Factori invalizi");
                    }
                }
                else{
                    System.out.println("EROARE: Format comanda invalid\n");
                }
            }

            if(nr_comanda==4){
                //verificare tip status
                if(comanda[2].equals("true") || comanda[2].equals("false")){
                    boolean status=Boolean.parseBoolean(comanda[2]);
                    String verificare=grid.verificareStatus(comanda[1],status);
                    System.out.println(verificare);
                }
                else System.out.println("EROARE: Status invalid\n");
            }

            if(nr_comanda==5){
                String raspuns=grid.stareRetea();
                System.out.println(raspuns);
            }

            if(nr_comanda==6){
                String istoric=grid.afisareIstoriTick();
                if(istoric.equals("")){
                    System.out.println("Istoric evenimente gol");
                }
                else
                    System.out.println(istoric);
            }
            //citire urmatoarea comanda
            linieComanda=scanner.nextLine();
        }
        if(linieComanda.equals("7")) {
            System.out.println("Simulatorul se inchide.\n");
        }

    }

    public static void main(String[] args) {
        App app = new App(System.in);
        app.run();
    }
}
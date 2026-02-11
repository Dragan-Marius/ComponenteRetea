package main.java.grid;

public class PanouSolar extends ProducatorEnergie {
    private double putereMaxima;
    PanouSolar(double putereMaxima, String id) {
        super(id);
        this.putereMaxima=putereMaxima;
    }
    public double calculeazaProductie(double procentajSoare, double vitezaVant) {
        return procentajSoare*putereMaxima;
    }
    public String afisareDetalii() {
        String putere = String.format("%.2f",this.putereMaxima);
        String s = "Producator "+id+" (PanouSolar) - PutereBaza: "+putere+" - Status: ";
        if(statusOperational==true){
            s = s+"Operational\n";
        }
        else s = s + "Defect\n";
        return s;
    }
}

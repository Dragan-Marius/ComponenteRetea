package main.java.Tema1;

public class ReactorNuclear extends ProducatorEnergie{
   protected double putereConstanta;
    ReactorNuclear(double putereConstanta, String id) {
        super(id);
        this.putereConstanta=putereConstanta;
    }
    public double calculeazaProductie(double procentasjSoare, double vitezaVant){
        if (statusOperational) {
            return putereConstanta;
        }
        return 0;
    }
    public String afisareDetalii() {
        String putere = String.format("%.2f", this.putereConstanta);
        String s = "Producator "+id+" (ReactorNuclear) - PutereBaza: " + putere + " - Status: ";
        if(statusOperational == true) {
            s = s + "Operational\n";
        }
        else s = s + "Defect\n";
        return s;
    }
}

package main.java.grid;

public class TurbinaEoliana extends ProducatorEnergie {
    private double putereBaza;

    TurbinaEoliana(double putereBaza, String id) {
        super(id);
        this.putereBaza = putereBaza;
    }

    public double calculeazaProductie(double procentasjSoare, double vitezaVant) {
        return vitezaVant * putereBaza;
    }

    public String afisareDetalii() {
        String putere = String.format("%.2f",this.putereBaza);
        String s = "Producator " + id + " (TurbinaEoliana) - PutereBaza: " + putere+ " - Status: ";
        if(statusOperational == true){
            s = s+"Operational\n";
        }
        else s = s + "Defect\n";
        return s;
    }
}

package main.java.Tema1;

public class Baterie extends ComponenteRetea{
    private double capacitateMaxima;
    private double energieStocata;
    Baterie(String id,double capacitateMaxima) {
        super(id);
        this.capacitateMaxima = capacitateMaxima;
        this.energieStocata=0.0;
    }
    public double getEnergieStocata() {
        return energieStocata;
    }
    public double incarca(double energieDisponibila) {
            this.energieStocata = this.energieStocata + energieDisponibila;
            if(this.energieStocata > this.capacitateMaxima) {
                double surplus = this.energieStocata - this.capacitateMaxima;
                this.energieStocata = this.capacitateMaxima;
                return surplus;
            }
            else return 0.0;
    }
    public double descarca(double energieCeruta) {
        double energie_ramasa = this.energieStocata - energieCeruta;
        if(energie_ramasa < 0.0) {
            double energie_return = this.energieStocata;
            this.energieStocata = 0.0;
            return energie_return;
        }
        this.energieStocata = energie_ramasa;
        return energieCeruta;
    }

    public String afisareDetalii(){
        String energieStocataFormat = String.format("%.2f",this.energieStocata);
        String capacitateFormat = String.format("%.2f",this.capacitateMaxima);
        String s = "Baterie " + id + " - Stocare: " + energieStocataFormat + "/" + capacitateFormat + " - Status: ";
        if(statusOperational == true){
            s = s + "Operational\n";
        }
        else s = s + "Neoperational\n";
        return s;
    }
}

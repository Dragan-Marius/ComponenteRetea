package main.java.grid;

public class SistemSuportViata extends ConsumatorEnergie{
    SistemSuportViata(String id, double cerereEnergie){
        super(id,1,cerereEnergie);
    }
    public String afisareDetalii() {
        String energie = String.format("%.2f", getCerereEnergie());
        String s = "Consumator "+id+" (SistemSuportViata) - Cerere: " + energie+" - Prioritate: " + getPrioritate()+" - Status: ";
        if(esteAlimentat == true) {
            s = s + "Alimentat\n";
        }
        else s = s + "Decuplat\n";
        return s;
    }
}

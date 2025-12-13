package main.java.Tema1;

public class SistemIluminat extends ConsumatorEnergie{
    SistemIluminat(String id,double cerereEnergie){
        super(id,3,cerereEnergie);
    }
    public String afisareDetalii() {
        String energie = String.format("%.2f", getCerereEnergie());
        String s = "Consumator " + id + " (SistemIluminat) - Cerere: " + energie + " - Prioritate: " + getPrioritate() + " - Status: ";
        if(esteAlimentat == true) {
            s = s + "Alimentat\n";
        }
        else s = s + "Decuplat\n";
        return s;
    }
}

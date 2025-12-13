package main.java.Tema1;

public class LaboratorStiintific extends ConsumatorEnergie{
    public LaboratorStiintific(String id,double cerereEnergie){
        super(id,2,cerereEnergie);
    }
    public String afisareDetalii(){
        String energie = String.format("%.2f", getCerereEnergie());
        String s = "Consumator " + id + " (LaboratorStiintific) - Cerere: " + energie + " - Prioritate: " + getPrioritate() + " - Status: ";
        if (esteAlimentat == true){
            s = s + "Alimentat\n";
        }
        else s = s + "Decuplat\n";
        return s;
    }
}

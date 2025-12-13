package main.java.Tema1;

public abstract class ConsumatorEnergie extends ComponenteRetea{
    private double cerereEnergie;
    private int prioritate;
    protected boolean esteAlimentat;
    ConsumatorEnergie(String id, int prioritate, double cerereEnergie) {
        super(id);
        this.prioritate = prioritate;
        this.cerereEnergie = cerereEnergie;
        this.esteAlimentat = true;
    }
    public double getCerereCurenta() {
        if (this.esteAlimentat) {
            return this.cerereEnergie;
        }
        return 0.0;
    }
    public void cupleazaLaRetea() {

        this.esteAlimentat=true;
    }
    public void decupleazaDeLaRetea() {
        this.esteAlimentat=false;
    }
    int  getPrioritate(){
        return this.prioritate;
    }
    double getCerereEnergie(){
        return this.cerereEnergie;
    }
    public abstract String afisareDetalii();

}

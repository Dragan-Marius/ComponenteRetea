package main.java.grid;

public abstract class ProducatorEnergie extends ComponenteRetea{
    public abstract double calculeazaProductie(double procentajSoare, double vitezaVant);
    ProducatorEnergie(String id) {
        super(id);
    }
    public abstract String afisareDetalii();

}

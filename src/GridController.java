package main.java.Tema1;

import java.util.ArrayList;

public class GridController {
    private ArrayList<ConsumatorEnergie> consumatoriEnergie = new ArrayList();
    private ArrayList<ProducatorEnergie> producatoriEnergie = new ArrayList();
    private ArrayList<Baterie> baterii = new ArrayList();
    private ArrayList<String> mesaje = new ArrayList();
    private int nr_tickuri = 0;
    private boolean esteInBlackout = false;
    public boolean getEsteInBlackout() {
        return esteInBlackout;
    }
    public String simuleazaTick(double factorSoare,double factorVant) {
        if (getEsteInBlackout() == true) {
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita";
        }
        nr_tickuri++;
        for (ConsumatorEnergie consumatorEnergie : consumatoriEnergie) {
            consumatorEnergie.cupleazaLaRetea();
        }
        double productieTotala = 0.0;
        double bTotal = 0.0;
        String listaDecuplati = "";
        //calcul productie de energie
        for (ProducatorEnergie producatorEnergie : producatoriEnergie) {
            if (producatorEnergie.statusOperational) {
                productieTotala = productieTotala + producatorEnergie.calculeazaProductie(factorSoare, factorVant);
            }
        }
        double cerereTotala = 0.0;
        double deficit = 0.0;
        //calcul cerere energie
        for (ConsumatorEnergie consumatorEnergie : consumatoriEnergie) {
            //cerereTotala = cerereTotala + consumatorEnergie.getCerereCurenta();
            if (consumatorEnergie.statusOperational) {
                cerereTotala = cerereTotala + consumatorEnergie.getCerereCurenta();
            }
        }
        double delta = productieTotala - cerereTotala;
        if (delta > 0) {
            for (Baterie baterie : baterii) {
                if (baterie.statusOperational) {
                    delta = baterie.incarca(delta);
                    if (delta <= 0.0) {
                        break;
                    }
                }
            }
        } else if (delta < 0) {
            deficit = -delta;
            for (Baterie baterie : baterii) {
                if (baterie.statusOperational) {
                    deficit = deficit - baterie.descarca(deficit);
                }
            }
        }
        if (deficit > 0) {
            //triage
            //decuplare in ordinea prioritatii
            for (ConsumatorEnergie consumatorEnergie : consumatoriEnergie) {
                if (consumatorEnergie.getPrioritate() == 3 && deficit > 0) {
                    consumatorEnergie.decupleazaDeLaRetea();
                    deficit = deficit - consumatorEnergie.getCerereEnergie();
                    if (listaDecuplati.length() != 0) {
                        listaDecuplati = listaDecuplati + ", ";
                    }
                    listaDecuplati = listaDecuplati + consumatorEnergie.id;
                }
            }
            for (ConsumatorEnergie consumatorEnergie : consumatoriEnergie) {
                if (consumatorEnergie.getPrioritate() == 2 && deficit > 0) {
                    consumatorEnergie.decupleazaDeLaRetea();
                    deficit = deficit - consumatorEnergie.getCerereEnergie();
                    if (listaDecuplati.length() != 0) {
                        listaDecuplati = listaDecuplati + ", ";
                    }
                    listaDecuplati = listaDecuplati + consumatorEnergie.id;
                }
            }
        }
        if (deficit > 0) {
            esteInBlackout = true;
            mesaje.add("Tick " + nr_tickuri + ": BLACKOUT! SIMULARE OPRITA.");
            // nr_tickuri++;
            return "BLACKOUT! SIMULARE OPRITA.";
        }
        for (Baterie baterie : baterii) {
            bTotal = bTotal + baterie.getEnergieStocata();
        }
        String productieTotalaFormat = String.format("%.2f", productieTotala);
        String bTotalFormat = String.format("%.2f", bTotal);
        String cerereTotalaFormat = String.format("%.2f", cerereTotala);
        return "TICK: Productie " + productieTotalaFormat + ", Cerere " + cerereTotalaFormat + ". Baterii: " + bTotalFormat + " MW. Decuplati: [" + listaDecuplati + "]";
    }

    public int verificare(String id){
        //verificare unicitate id
        for(ProducatorEnergie producatorEnergie:producatoriEnergie){
            if(producatorEnergie.id.equals(id)){
                return 0;
            }
        }
        for(ConsumatorEnergie consumatorEnergie:consumatoriEnergie){
            if(consumatorEnergie.id.equals(id)){
                return 0;
            }
        }
        for(Baterie baterie:baterii){
            if(baterie.id.equals(id)){
                return 0;
            }
        }
        return 1;
    }

    public String adaugareProducator(String tip_producator,String id, double putere){
        //verificare stare retea
        if(esteInBlackout==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok=verificare(id);
        if(ok==0){
            return "EROARE: Exista deja o componenta cu id-ul "+id+"\n";
        }
        //verificare tip producator
        if(tip_producator.equals("solar")){
            PanouSolar producator_solar = new PanouSolar(putere,id);
            producatoriEnergie.add(producator_solar);
        }
        if(tip_producator.equals("reactor")){
            ReactorNuclear producator_reactor = new ReactorNuclear(putere,id);
            producatoriEnergie.add(producator_reactor);
        }
        if(tip_producator.equals("turbina")){
            TurbinaEoliana producator_turbina = new TurbinaEoliana(putere,id);
            producatoriEnergie.add(producator_turbina);
        }
        return "S-a adaugat producatorul "+id+" de tip " + tip_producator+"\n";
    }

    public String adaugareConsumator(String tip_consumator, String id, double putere){
        //verificare stare retea
        if(esteInBlackout==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok = verificare(id);
        if(ok == 0){
            return "EROARE: Exista deja o componenta cu id-ul " + id + "\n";
        }
        //verificare tip consumator
        if(tip_consumator.equals("suport_viata")){
            SistemSuportViata sistemSuportViata = new SistemSuportViata(id, putere);
            consumatoriEnergie.add(sistemSuportViata);
        }
        if(tip_consumator.equals("laborator")){
            LaboratorStiintific lab = new LaboratorStiintific(id,putere);
            consumatoriEnergie.add(lab);
        }
        if(tip_consumator.equals("iluminat")){
            SistemIluminat sistem = new SistemIluminat(id,putere);
            consumatoriEnergie.add(sistem);
        }
        return "S-a adaugat consumatorul " + id + " de tip " + tip_consumator+"\n";
    }

    public String adaugareBaterie(String id, double capacitate_maxima){
        //verificare stare retea
        if(esteInBlackout==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok = verificare(id);
        if(ok==0){
            return "EROARE: Exista deja o componenta cu id-ul " + id + "\n";
        }
        Baterie baterieNoua = new Baterie(id, capacitate_maxima);
        baterii.add(baterieNoua);
        return "S-a adaugat bateria " + id + " cu capacitatea " + capacitate_maxima + "\n";
    }

    public String verificareStatus(String id,boolean status_operational){
        //verificare id
        int ok=verificare(id);
        if (ok == 1)
            return "EROARE: Nu exista componenta cu id-ul " + id + "\n";
        //schimbare status operational
        for(ConsumatorEnergie consumatorEnergie:consumatoriEnergie){
            if(consumatorEnergie.id.equals(id)){
                consumatorEnergie.statusOperational=status_operational;
                if(consumatorEnergie.statusOperational==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        for(ProducatorEnergie producatorEnergie:producatoriEnergie){
            if(producatorEnergie.id.equals(id)){
                producatorEnergie.statusOperational=status_operational;
                if(producatorEnergie.statusOperational==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        for(Baterie baterie:baterii){
            if(baterie.id.equals(id)){
                baterie.statusOperational=status_operational;
                if(baterie.statusOperational==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        return "";
    }

    public String stareRetea(){
        //tipul retelei
        if(baterii.size()==0 && producatoriEnergie.size()==0  && consumatoriEnergie.size()==0){
            return "Reteaua este goala\n";
        }
        String raspuns="";
        if(esteInBlackout==true) {
            raspuns = raspuns + "Stare Retea: BLACKOUT\n";
        } else {
            raspuns = raspuns+"Stare Retea: STABILA" + "\n";
        }
        for (ProducatorEnergie producatorEnergie : producatoriEnergie) {
            raspuns = raspuns + producatorEnergie.afisareDetalii();
        }
        for (ConsumatorEnergie consumatorEnergie : consumatoriEnergie) {
            raspuns = raspuns + consumatorEnergie.afisareDetalii();
        }
        for (Baterie baterie : baterii) {
            raspuns = raspuns + baterie.afisareDetalii();
        }
        return raspuns;
    }
    public String afisareIstoriTick(){
        String istoric="";
        for(String s:mesaje){
            istoric=istoric+s+"\n";
        }
        return istoric;
    }
}

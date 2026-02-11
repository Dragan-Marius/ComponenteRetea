package main.java.grid;

import java.util.ArrayList;

public class GridController {
    private ArrayList<EnergyConsumer> energyConsumer = new ArrayList();
    private ArrayList<EnergyProducer> energyProducer = new ArrayList();
    private ArrayList<Battery> batteries = new ArrayList();
    private ArrayList<String> messages = new ArrayList();
    private int tickNumber = 0;
    private boolean blackout = false;
    public boolean getBlackout() {
        return blackout;
    }
    public String simulateTick(double sunFactor, double windFactor) {
        if (getBlackout() == true) {
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita";
        }
        tickNumber++;
        for (EnergyConsumer energyConsumer : energyConsumer) {
            energyConsumer.connectToNetwork();
        }
        double totalProduction = 0.0;
        double bTotal = 0.0;
        String disconnectList = "";
        //calcul productie de energie
        for (EnergyProducer energyProducer : energyProducer) {
            if (energyProducer.operationalStatus) {
                totalProduction = totalProduction + energyProducer.calculateProduction(sunFactor, windFactor);
            }
        }
        double demandTotal = 0.0;
        double deficit = 0.0;
        //calcul cerere energie
        for (EnergyConsumer energyConsumer : energyConsumer) {
            //cerereTotala = cerereTotala + consumatorEnergie.getCerereCurenta();
            if (energyConsumer.operationalStatus) {
                demandTotal = demandTotal + energyConsumer.getCurrentRequest();
            }
        }
        double delta = totalProduction - demandTotal;
        if (delta > 0) {
            for (Battery battery : batteries) {
                if (battery.operationalStatus) {
                    delta = battery.unload(delta);
                    if (delta <= 0.0) {
                        break;
                    }
                }
            }
        } else if (delta < 0) {
            deficit = -delta;
            for (Battery battery : batteries) {
                if (battery.operationalStatus) {
                    deficit = deficit - battery.download(deficit);
                }
            }
        }
        if (deficit > 0) {
            //triage
            //decuplare in ordinea prioritatii
            for (EnergyConsumer energyConsumer : energyConsumer) {
                if (energyConsumer.getPriority() == 3 && deficit > 0) {
                    energyConsumer.disconnectFromNetwork();
                    deficit = deficit - energyConsumer.getEnergyDemand();
                    if (disconnectList.length() != 0) {
                        disconnectList = disconnectList + ", ";
                    }
                    disconnectList = disconnectList + energyConsumer.id;
                }
            }
            for (EnergyConsumer energyConsumer : energyConsumer) {
                if (energyConsumer.getPriority() == 2 && deficit > 0) {
                    energyConsumer.disconnectFromNetwork();
                    deficit = deficit - energyConsumer.getEnergyDemand();
                    if (disconnectList.length() != 0) {
                        disconnectList = disconnectList + ", ";
                    }
                    disconnectList = disconnectList + energyConsumer.id;
                }
            }
        }
        if (deficit > 0) {
            blackout = true;
            messages.add("Tick " + tickNumber + ": BLACKOUT! SIMULARE OPRITA.");
            // nr_tickuri++;
            return "BLACKOUT! SIMULARE OPRITA.";
        }
        for (Battery battery : batteries) {
            bTotal = bTotal + battery.getStoredEenergy();
        }
        String productieTotalaFormat = String.format("%.2f", totalProduction);
        String bTotalFormat = String.format("%.2f", bTotal);
        String cerereTotalaFormat = String.format("%.2f", demandTotal);
        return "TICK: Productie " + productieTotalaFormat + ", Cerere " + cerereTotalaFormat + ". Baterii: " + bTotalFormat + " MW. Decuplati: [" + disconnectList + "]";
    }

    public int verification(String id){
        //verificare unicitate id
        for(EnergyProducer energyProducer : energyProducer){
            if(energyProducer.id.equals(id)){
                return 0;
            }
        }
        for(EnergyConsumer energyConsumer : energyConsumer){
            if(energyConsumer.id.equals(id)){
                return 0;
            }
        }
        for(Battery battery : batteries){
            if(battery.id.equals(id)){
                return 0;
            }
        }
        return 1;
    }

    public String addProducer(String producerType, String id, double power){
        //verificare stare retea
        if(blackout ==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok= verification(id);
        if(ok==0){
            return "EROARE: Exista deja o componenta cu id-ul "+id+"\n";
        }
        //verificare tip producator
        if(producerType.equals("solar")){
            SolarPanel solarProducer = new SolarPanel(power,id);
            energyProducer.add(solarProducer);
        }
        if(producerType.equals("reactor")){
            NuclearReactor reactorProducer = new NuclearReactor(power,id);
            energyProducer.add(reactorProducer);
        }
        if(producerType.equals("turbina")){
            WindTurbine turbineProducer = new WindTurbine(power,id);
            energyProducer.add(turbineProducer);
        }
        return "S-a adaugat producatorul "+id+" de tip " + producerType +"\n";
    }

    public String addConsumer(String ConsumatorType, String id, double power){
        //verificare stare retea
        if(blackout ==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok = verification(id);
        if(ok == 0){
            return "EROARE: Exista deja o componenta cu id-ul " + id + "\n";
        }
        //verificare tip consumator
        if(ConsumatorType.equals("suport_viata")){
            LifeSupportSystem lifeSupportSystem = new LifeSupportSystem(id, power);
            energyConsumer.add(lifeSupportSystem);
        }
        if(ConsumatorType.equals("laborator")){
            ScientificLaboratory lab = new ScientificLaboratory(id, power);
            energyConsumer.add(lab);
        }
        if(ConsumatorType.equals("iluminat")){
            LightingSystem system = new LightingSystem(id, power);
            energyConsumer.add(system);
        }
        return "S-a adaugat consumatorul " + id + " de tip " + ConsumatorType +"\n";
    }

    public String addBattery(String id, double maximumCapacity){
        //verificare stare retea
        if(blackout ==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok = verification(id);
        if(ok==0){
            return "EROARE: Exista deja o componenta cu id-ul " + id + "\n";
        }
        Battery batteryNew = new Battery(id, maximumCapacity);
        batteries.add(batteryNew);
        return "S-a adaugat bateria " + id + " cu capacitatea " + maximumCapacity + "\n";
    }

    public String statusVerification(String id, boolean OperationalStatus){
        //verificare id
        int ok= verification(id);
        if (ok == 1)
            return "EROARE: Nu exista componenta cu id-ul " + id + "\n";
        //schimbare status operational
        for(EnergyConsumer energyConsumer : energyConsumer){
            if(energyConsumer.id.equals(id)){
                energyConsumer.operationalStatus = OperationalStatus;
                if(energyConsumer.operationalStatus ==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        for(EnergyProducer energyProducer : energyProducer){
            if(energyProducer.id.equals(id)){
                energyProducer.operationalStatus = OperationalStatus;
                if(energyProducer.operationalStatus ==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        for(Battery battery : batteries){
            if(battery.id.equals(id)){
                battery.operationalStatus = OperationalStatus;
                if(battery.operationalStatus ==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        return "";
    }

    public String NetworkState(){
        //tipul retelei
        if(batteries.size()==0 && energyProducer.size()==0  && energyConsumer.size()==0){
            return "Reteaua este goala\n";
        }
        String answer="";
        if(blackout ==true) {
            answer = answer + "Stare Retea: BLACKOUT\n";
        } else {
            answer = answer+"Stare Retea: STABILA" + "\n";
        }
        for (EnergyProducer energyProducer : energyProducer) {
            answer = answer + energyProducer.displayDetails();
        }
        for (EnergyConsumer energyConsumer : energyConsumer) {
            answer = answer + energyConsumer.displayDetails();
        }
        for (Battery battery : batteries) {
            answer = answer + battery.showDetails();
        }
        return answer;
    }
    public String historyTick(){
        String history="";
        for(String s: messages){
            history=history+s+"\n";
        }
        return history;
    }
}

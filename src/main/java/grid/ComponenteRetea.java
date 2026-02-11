package main.java.grid;

public abstract class ComponenteRetea {
    protected String id;
    protected boolean statusOperational;
    ComponenteRetea(String id) {
        this.id = id;
        this.statusOperational=true;
    }
}

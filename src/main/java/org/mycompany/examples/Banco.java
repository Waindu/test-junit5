package org.mycompany.examples;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {
    private List<Cuenta> cuentas;

    private String nombre;

    public Banco() {
        cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
        cuenta.setBanco(this);
    }

}

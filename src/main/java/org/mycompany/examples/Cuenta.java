package org.mycompany.examples;

import java.math.BigDecimal;

public class Cuenta {
    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto) {
        BigDecimal nuevoSaldo = this.saldo.subtract(monto);
        //if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0){
        //    throw new DineroInsuficienteException("Dinero Insuficiente");
        //}
        this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto) {
        this.saldo = this.saldo.add(monto);
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cuenta)) {
            return false;
        }
        if (this.persona == null || this.saldo == null) {
            return false;
        }
        Cuenta c = (Cuenta) o;
        return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
    }
}

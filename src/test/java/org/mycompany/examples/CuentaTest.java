package org.mycompany.examples;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CuentaTest {
    Cuenta cuenta;

    @BeforeAll // Una vez al principio
    static void beforeAll() {
        System.out.println("Inicializando Test Cuenta (BeforeAll Super)");
    }

    @AfterAll // Una vez al final
    static void afterAll() {
        System.out.println("Finalizado Test Cuenta (AfterAll Super)");
    }

    @BeforeEach // antes de cada test
    void initMetodoTest() {
        this.cuenta = new Cuenta("Fede", new BigDecimal("1000.0001"));
        System.out.println("Iniciando método de test (BeforeEach Super)");
    }

    @AfterEach // despues de cada test
    void tearDown() {
        System.out.println("Finalizado método de test (AfterEach Super)");
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CuentaAtributosTest {
        @BeforeAll
        void beforeAllCuentaAtributosTest() {
            System.out.println("Inicializando Tests de Atributos (BeforeAll atr)");
        }

        @AfterAll
        void afterAllCuentaAtributosTest() {
            System.out.println("Finalizados Tests de Atributos (BeforeAll atr)");
        }
        @Tag("cuenta")
        @Test
        void testNombreCuenta() {
            // cuenta.setPersona("Fede");
            String esperado = "Fede"; // expected
            String real = cuenta.getPersona(); // actual
            Assertions.assertEquals(esperado, real, () -> "El nombre no es correcto");
            Assertions.assertTrue(esperado.equals(real),  "El nobre no es utilizado otro assert");
        }

        @Tag("cuenta")
        @Test
        void testSaldoCuenta() {
            //        Cuenta cuenta = new Cuenta("Fede", new BigDecimal("1000.0001")); // lo quitamos al agregar @BeforeEach
            //        cuenta.setSaldo(new BigDecimal("1000.0001")); // lo quitamos porque se setea en constructor

            Assertions.assertEquals(1000.000, cuenta.getSaldo().doubleValue()); // expected, actual
            Assertions.assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        }
    }

    @Tag("comparacion")
    @Test
    @DisplayName("Comparar cuentas utilizando implementación equals de la clase")
    void testCompararCuenta() {
        Cuenta cuenta2 = new Cuenta("Fede", new BigDecimal("1000.0001"));
        Cuenta cuenta3 = new Cuenta("Rodrigo", new BigDecimal("100.001"));

        Assertions.assertAll(() -> Assertions.assertEquals(cuenta, cuenta2),
                () -> Assertions.assertNotEquals(cuenta2, cuenta3),
                () -> Assertions.assertTrue(cuenta.equals(cuenta2)),
                () -> Assertions.assertFalse(cuenta2.equals(cuenta3)));
    }

    @Test
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.0001", cuenta.getSaldo().toPlainString());

    }
    @Test
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.0001", cuenta.getSaldo().toPlainString());
    }
}

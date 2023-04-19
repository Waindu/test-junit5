package org.mycompany.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mycompany.exeptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


public class CuentaTest {
    Cuenta cuenta;
    String DEV = "dev";

    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeAll // Una vez al principio
    static void beforeAll() {
        System.out.println("Inicializando Test Cuenta (BeforeAll Super)");
    }

    @AfterAll // Una vez al final
    static void afterAll() {
        System.out.println("Finalizado Test Cuenta (AfterAll Super)");
    }

    @BeforeEach // antes de cada test
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter) {
        this.cuenta = new Cuenta("Fede", new BigDecimal("1000.0001"));
        this.testInfo = testInfo;
        this.testReporter = testReporter;
//        System.out.println("ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().get().getName()
//                + " con las etiquetas " + testInfo.getTags());
        testReporter.publishEntry("ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().get().getName()
                + " con las etiquetas " + testInfo.getTags());
    }

    @AfterEach // despues de cada test
    void tearDown(TestInfo testInfo, TestReporter testReporter) {
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        System.out.println("Finalizado método de test "+ testInfo.getDisplayName() + "(AfterEach Super)");
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

            Assertions.assertEquals(1000.0001, cuenta.getSaldo().doubleValue()); // expected, actual
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
    @DisplayName("Test Credito Cuenta")
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.0001", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test Dinero Insuficiente Exception Cuenta")
    void testDineroInsuficienteExceptionCuenta() {
        Exception e = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = e.getMessage();
        String expected = "Dinero Insuficiente";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Relacion Banco Cuentas")
    @Disabled
    void testRelacionBancoCuentas() {
        //fail();
        Cuenta cuenta1 = new Cuenta("Lola", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Fede", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertAll(
                () -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                        () -> "el valor del saldo de la cuenta2 no es el esperado."),
                () -> assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                        () -> "el valor del saldo de la cuenta1 no es el esperado."),
                () -> assertEquals(2, banco.getCuentas().size(),
                        () -> "el banco no tienes las cuentas esperadas"),
                () -> assertEquals("Banco del Estad", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Fede", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Fede"))
                        .findFirst()
                        .get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("Pedro")), () -> "no encontré a Pedro" ));
    }

    @Nested
    @DisplayName("Test según Sistema Operativo")
    class SoTest {
        @Test
        @DisplayName("SOLO WIN")
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }
        @Test
        @DisplayName("NO EN WIN")
        @DisabledOnOs(OS.WINDOWS)
        void testNoEnWindows() {
        }

        @Test
        @DisplayName("SOLO MAC")
        @EnabledOnOs(OS.MAC)
        void testSoloMac() {
        }
    }

    @Nested
    @DisplayName("Test según Versión JRE")
    class JavaJreVersionTest {
        @Test
        @DisplayName("JRE 8")
        @EnabledOnJre(JRE.JAVA_8)
        void testJre8() {
        }
        @Test
        @DisplayName("JRE 11")
        @EnabledOnJre(JRE.JAVA_11)
        void testJre11() {
        }

        @Test
        @DisplayName("JRE NO ES 15")
        @DisabledOnJre(JRE.JAVA_15)
        void testSoloMac() {
        }
    }

    @Nested
    @DisplayName("Test según System Properties")
    class SystemPropTest {
        @Test
        void imprimirSystemProperties() {
            Properties p = System.getProperties();
            p.forEach((k, v) -> System.out.println(String.format("%s: %s", k, v)));
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "Fede")
        void testSoloFede() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "Marta")
        void testSoloMarta() {
        }

        @Test
        @DisabledIfSystemProperty(named = "java.version", matches = ".*8.*")
        void testDisabledJava8ConSystemProp() {
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*8.*")
        void testEnabledJava8ConSystemProp() {
        }

        @Test
        @DisplayName("test Env Var Con Sys Prop")
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testEnvVarConSysProp() {
        }
    }

    @Nested
    @DisplayName("Test según ENV VAR")
    class EnviromentVariableTest {
        @Test
        @DisplayName("TEST Match v11")
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-11.*")
        void testVariableEntornoConEnvironmentVariableJDK11() {
        }

        @Test
        @DisplayName("TEST Match v15")
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-15.*")
        void testVariableEntornoConEnvironmentVariableJDK15() {
        }
    }

    @Nested
    @DisplayName("ASSUMPTION TEST")
    class AssumptionsTest {

        @Tag("assumption")
        @Test
        @DisplayName("TEST ASSUMPTIONS CON ASSUME TRUE")
        void testNombreCuentaConAssumption() {
            String esperado = "Fede";
            String real = cuenta.getPersona();
            boolean isDev = DEV.equals(System.getProperty("ENV"));
            Assumptions.assumeTrue(isDev);
            Assertions.assertEquals(esperado, real);
            Assertions.assertTrue(esperado.equals(real));
        }
        @Tag("assumption")
        @Test
        @DisplayName("TEST ASSUMPTIONS CON ASSUMING THAT")
        void testNombreCuentaConAssumptions() {
            String esperado = "Fede";
            String real = cuenta.getPersona();
            boolean isDev = DEV.equals(System.getProperty("ENV"));

            assumingThat(isDev, () -> Assertions.assertTrue(esperado.equals(real), () -> "Fallo Assumption"));

            Assertions.assertEquals(esperado, real, () -> "Fallo Assert");
        }
    }

    @Nested
    @DisplayName("Timeout Test")
    @Tag("timeout")
    class TimeoutTest {
        @Test
        @Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
        void timeoutTest() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(1500);
        }

        @Test
        void assertTimeoutTest() {
            // No se verá afectado por el assertTimeout

            Assertions.assertTimeout(
                    Duration.ofSeconds(2), () -> TimeUnit.SECONDS.sleep(1)
            );
        }
    }
    @Tag("repe")
    @RepeatedTest(value=5, name="{displayName} - Repetición nº: {currentRepetition} de {totalRepetitions} ")
    @DisplayName("test Saldo Cuenta Repetido")
    void testSaldoCuentaRepetido() {
        Assertions.assertEquals(1000.0001, cuenta.getSaldo().doubleValue());
        Assertions.assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }


    @Tag("param")
    @ParameterizedTest
    @ValueSource(strings = {"100", "200", "300", "400", "500", "600"})
    @DisplayName("Test Debito Cuenta Parametrizado")
    void testDebitoCuentaParametrizado(String monto) {
        // cuenta Fede, 1000.0001

        cuenta.debito(new BigDecimal(monto));

        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Tag("param")
    @ParameterizedTest
    @CsvSource({"100, 1100.0001 ", "200, 1200.0001", "300, 1300.0001", "400, 1400.0001", "1000, 2000.0001"})
    @DisplayName("Test Credito Cuenta Parametrizado")
    void testCreditoCuentaParametrizado(String monto, String expected) {
        // cuenta Fede, 1000.0001

        cuenta.credito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertEquals(expected, cuenta.getSaldo().toPlainString());
    }


}

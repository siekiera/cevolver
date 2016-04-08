package pl.edu.pw.elka.mtoporow.cevolver.cli.export;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja służąca do oznaczenia pól do eksportu
 * Data utworzenia: 04.04.16, 14:21
 *
 * @author Michał Toporowski
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Cell {
    /**
     * Nazwa pola
     *
     * @return nazwa
     */
    String value();
}

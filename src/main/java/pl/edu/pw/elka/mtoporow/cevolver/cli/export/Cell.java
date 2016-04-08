package pl.edu.pw.elka.mtoporow.cevolver.cli.export;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Klasa X
 * Data utworzenia: 04.04.16, 14:21
 *
 * @author Michał Toporowski
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Cell {
    String value();
    int size() default 1;
}

package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;

/**
 * Klasa bazowa dla fabryk osobników
 * Data utworzenia: 08.03.16, 15:37
 *
 * @author Michał Toporowski
 */
public abstract class BaseCandidateFactory extends AbstractCandidateFactory<AbstractCanalModel> {

    /**
     * Zwraca długość wektora cech osobnika
     *
     * @return długość wektora cech
     */
    public abstract int traitCount();
}

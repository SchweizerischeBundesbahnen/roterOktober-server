package ch.sbb.roteroktober.server.service;

import ch.sbb.roteroktober.server.model.PensumEntity;
import ch.sbb.roteroktober.server.repo.PensumRepository;
import ch.sbb.roteroktober.server.rest.model.AuslastungResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Berechnet die Auslastung eines Mitarbeiters
 */
@Component
public class AuslastungService {

    @Autowired
    private PensumRepository pensumRepository;

    public List<AuslastungResource> berechneAuslastung(String uid) {
        // Alle Pensen für diesen Benutzer laden
        List<PensumEntity> pensen = pensumRepository.findByMitarbeiter(uid);
        return berechneAuslastung(pensen);
    }

    public List<AuslastungResource> berechneAuslastung(List<PensumEntity> pensumEntities) {
        // Alle Daten ermitteln, an welchen ein Pensum entweder beginnt oder aufhört. Denn immer dort
        // müssen wir bei der Berechnung der Auslastung hinschauen, weil sich etwas ändert.
        SortedSet<Date> pensumAenderungen = ermittlePensumAenderung(pensumEntities);

        // Auslastungen einmal sehr detailliert berechnen
        List<AuslastungResource> auslastungRaw = berechneAulastungRaw(pensumEntities, pensumAenderungen);

        // Mal alle Auslastungen ausgeben
        printAuslastungen(auslastungRaw, "Auslastung Raw");

        // Auslastungen bereinigen und glätten.
        List<AuslastungResource> result = glaetteAuslastung(auslastungRaw);
        printAuslastungen(result, "Resultat");
        return result;
    }

    private void printAuslastungen(List<AuslastungResource> auslastungRaw, String title) {
        System.out.println();
        System.out.println(title);
        System.out.println(StringUtils.repeat("=", title.length()));
        for (AuslastungResource auslastung : auslastungRaw) {
            System.out.println(auslastung);
        }
    }

    /**
     * Berechnet die Auslastung aufgrund jeder Änderung des Pensums. Es wird noch nicht berücksichtigt, ob zwei
     * auseinander folgende Pensen den gleichen Wert haben.
     * @param pensen Liste mit den Pensen
     * @param pensumAenderungen Liste mit den Daten, an welchen es Änderungen gibt
     * @return Liste mit den Auslastungen
     */
    private List<AuslastungResource> berechneAulastungRaw(List<PensumEntity> pensen, SortedSet<Date> pensumAenderungen) {
        List<AuslastungResource> auslastungRaw = new ArrayList<>();
        Date lastAenderung = null;
        for (Date pensumAenderung : pensumAenderungen) {
            // Uns interessiert immer das Paar der letzten und aktuellen Änderung
            if(lastAenderung == null){
                lastAenderung = pensumAenderung;
                continue;
            }

            // Auslastung zu diesem Zeitpunkt ermitteln
            int auslastung = ermittleAuslastung(pensen, lastAenderung);

            // Auslastung erstellen
            AuslastungResource auslastungResource = new AuslastungResource(auslastung, lastAenderung, pensumAenderung);
            auslastungRaw.add(auslastungResource);

            // Das letzte Pensum überschreiben
            lastAenderung = pensumAenderung;
        }
        return auslastungRaw;
    }

    /**
     * Bereinigt eine Liste von Auslastungen. Dabei wir folgendes durchgeführt:
     * * Zu kurze Element (nur wenige Sekunden) werden entfernt
     * * Elemente mit dem gleichen Wert werden zusammengeführt
     * @param auslastungen Liste mit den zu glättenden Auslastungen
     * @return Geglättete Auslastungen
     */
    private List<AuslastungResource> glaetteAuslastung(List<AuslastungResource> auslastungen) {
        Stack<AuslastungResource> stack = new Stack<>();

        for (AuslastungResource auslastung : auslastungen) {
            // Wenn noch nichts auf dem Stack ist, dann legen wir die Auslastung einfach drauf
            if (stack.isEmpty()) {
                stack.push(auslastung);
                continue;
            }

            // Letztes Element vom Stack nehmen
            AuslastungResource last = stack.peek();

            // Dauer des Elementes überprüfen
            long duration = auslastung.getEnde().getTime() - auslastung.getAnfang().getTime();

            // Wenn die Dauer unter einer Minute ist, dann ignorieren wir dieses Element
            if (duration < 1000 * 60) {
                continue;
            }

            // Wenn beide Elemente das gleiche Pensum haben, fügen wir es zusammen
            if (last.getPensum() == auslastung.getPensum()) {
                AuslastungResource merged = new AuslastungResource(last.getPensum(), last.getAnfang(), auslastung.getEnde());
                stack.pop();
                stack.push(merged);
                continue;
            }

            // Element ohne Anpassungen drauflegen
            stack.push(auslastung);
        }

        return stack;
    }

    /**
     * Ermittelt die Auslastung an einem bestimmten Tag. Dazu wird festgestellt, welche Pensen an diesem Tag gültig
     * sind und anschliessend deren Prozentwerte zusammengezählt
     * @param pensumEntities Liste mit den Pensen, welche durchsucht werden sollen
     * @param datum Datum, an welchem die Auslastung ermittelt werden soll
     * @return Auslastung zum gegebenen Zeitpunkt. 0, wenn kein Pensum vorhanden ist
     */
    private int ermittleAuslastung(List<PensumEntity> pensumEntities, Date datum) {
        int result = 0;
        for (PensumEntity pensum : pensumEntities) {
            if ((pensum.getAnfang().equals(datum) || pensum.getAnfang().before(datum))
                    && (pensum.getEnde().equals(datum) || pensum.getEnde().after(datum))) {
                result += pensum.getPensum();
            }
        }
        return result;
    }

    /**
     * Ermittelt alle Daten, an welchen ein Pensum beginnt oder endet. Dies sind alle Orte, an welchen sich die
     * Auslastung potentiell ändern kann.
     * @param pensumEntities Liste mit den Entitäten
     * @return Sortiertes Set mit den ermittelten Daten, wen welchen sich das Pensum ändert
     */
    private SortedSet<Date> ermittlePensumAenderung(List<PensumEntity> pensumEntities) {
        SortedSet<Date> result = new TreeSet<>();

        for (PensumEntity pensum : pensumEntities) {
            result.add(pensum.getAnfang());

            // Beim Ende-Datum setzten wir die Zeit immer auf Mitternacht, da wir davon ausgehen, dass immer der
            // ganze Tag gemeint ist
            result.add(setEndOfDay(pensum.getEnde()));
        }

        return result;
    }

    /**
     * Der die Zeit eines Datums auf Ende des Tages, sprich 23:59:59:999
     * @param date Datum, dessen Zeit geändert werden soll
     * @return Datum mit Zeit zu Ende des Tages
     */
    private Date setEndOfDay(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}

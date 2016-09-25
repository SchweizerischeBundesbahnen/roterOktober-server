package ch.sbb.roteroktober.server.service;

import ch.sbb.roteroktober.server.model.PensumEntity;
import ch.sbb.roteroktober.server.rest.model.AuslastungResource;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Unittest für die Klasse {@link AuslastungService}
 */
@RunWith(MockitoJUnitRunner.class)
public class AuslastungServiceTest {

    @InjectMocks
    private AuslastungService auslastungService;

    @Test
    public void berechneAuslastung() throws Exception {
        // Pensen erstellen
        List<PensumEntity> pensen = new ArrayList<>();
        pensen.add(new PensumEntity(60, cd(2015, 1, 1), cd(2015, 7, 31)));
        pensen.add(new PensumEntity(80, cd(2015, 8, 1), cd(2016, 7, 31)));
        pensen.add(new PensumEntity(80, cd(2016, 8, 1), cd(2016, 10, 31)));
        pensen.add(new PensumEntity(30, cd(2015, 6, 1), cd(2015, 12, 31)));
        pensen.add(new PensumEntity(20, cd(2016, 3, 1), cd(2016, 3, 31)));
        pensen.add(new PensumEntity(20, cd(2016, 4, 1), cd(2016, 4, 30)));

        // Test ausführen
        List<AuslastungResource> result = auslastungService.berechneAuslastung(pensen);

        // Resultat überprüfen
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.size());
        checkAuslastung(result.get(0), 60, cd(2015, 1, 1), cd(2015, 5, 31));
        checkAuslastung(result.get(1), 90, cd(2015, 6, 1), cd(2015, 7, 31));
        checkAuslastung(result.get(2), 110, cd(2015, 8, 1), cd(2015, 12, 31));
        checkAuslastung(result.get(3), 80, cd(2016, 1, 1), cd(2016, 2, 29));
        checkAuslastung(result.get(4), 100, cd(2016, 3, 1), cd(2016, 4, 30));
        checkAuslastung(result.get(5), 80, cd(2016, 5, 1), cd(2016, 10, 31));
    }

    private void checkAuslastung(AuslastungResource result, int pensum, Date start, Date ende) {
        Assert.assertNotNull(result);
        Assert.assertEquals(pensum, result.getPensum());

        Assert.assertEquals(start.getYear(), result.getAnfang().getYear());
        Assert.assertEquals(start.getMonth(), result.getAnfang().getMonth());
        Assert.assertEquals(start.getDate(), result.getAnfang().getDate());

        Assert.assertEquals(ende.getYear(), result.getEnde().getYear());
        Assert.assertEquals(ende.getMonth(), result.getEnde().getMonth());
        Assert.assertEquals(ende.getDate(), result.getEnde().getDate());
    }

    private Date cd(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
package ch.sbb.roteroktober.server.repo;

import java.util.List;

import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import ch.sbb.roteroktober.server.model.QEinsatzEntity;
import ch.sbb.roteroktober.server.model.QMitarbeiterEntity;
import ch.sbb.roteroktober.server.model.QPensumEntity;
import ch.sbb.roteroktober.server.model.QProjektEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * Erweiterte DB-Zugriffe auf den Mitarbeiter, welche mit einem normalen Spring Data Repo nicht abgebildet werden können.
 */
@Repository
public class MitarbeiterRepositoryCustom extends QueryDslRepositorySupport {

    public MitarbeiterRepositoryCustom() {
        super(MitarbeiterEntity.class);
    }

    public List<MitarbeiterEntity> search(String oeName, String projektId) {
        QMitarbeiterEntity mitarbeiter = QMitarbeiterEntity.mitarbeiterEntity;
        QEinsatzEntity einsatz = QEinsatzEntity.einsatzEntity;
        QProjektEntity projekt = QProjektEntity.projektEntity;
        QPensumEntity pensum = QPensumEntity.pensumEntity;

        // Query erstellen
        BooleanBuilder builder = new BooleanBuilder();
        JPQLQuery<MitarbeiterEntity> root = from(mitarbeiter).distinct();

        // OE überprüfen
        if (StringUtils.isNotBlank(oeName)) {
            // Wildcards ersetzen
            String oeNameSearch = oeName.replace("*", "%");

            BooleanExpression mitarbeiterUid = mitarbeiter.oeName.like(oeNameSearch);
            builder.and(mitarbeiterUid);
        }

        // Projekt überprüfen
        if (StringUtils.isNotBlank(projektId)) {
            root.innerJoin(mitarbeiter.einsaetze, einsatz).innerJoin(einsatz.projekt, projekt).innerJoin(einsatz.pensen, pensum); // Wir wollen nur Projekte, wo es effektiv Einsätze gibt

            // Nur nicht gelöschte Einträge finden
            builder.and(einsatz.deleted.isFalse());
            builder.and(projekt.deleted.isFalse());

            // Und nun endlich, die Überprüfung auf die Projekt-ID
            builder.and(projekt.publicId.equalsIgnoreCase(projektId));
        }

        // Nur nicht gelöschte Mitarbeiter suchen
        BooleanExpression isNotDeleted = mitarbeiter.deleted.isFalse();
        builder.and(isNotDeleted);

        return root.where(builder).fetch();
    }
}

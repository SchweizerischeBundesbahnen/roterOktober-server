package ch.sbb.roteroktober.server.service;

import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import ch.sbb.roteroktober.server.model.QMitarbeiterEntity;
import ch.sbb.roteroktober.server.repo.MitarbeiterRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Verwaltet die Mitarbeiter
 */
@Component
public class MitarbeiterService {

    @Autowired
    private MitarbeiterRepository mitarbeiterRepository;

    public Iterable<MitarbeiterEntity> search(String oeName) {
        // Wildcards ersetzen
        String oeNameSearch = oeName.replace("*", "%");

        QMitarbeiterEntity mitarbeiter = QMitarbeiterEntity.mitarbeiterEntity;
        BooleanExpression mitarbeiterUid = mitarbeiter.oeName.like(oeNameSearch);
        BooleanExpression isNotDeleted = mitarbeiter.deleted.isFalse();

        return mitarbeiterRepository.findAll(mitarbeiterUid.and(isNotDeleted));
    }
}

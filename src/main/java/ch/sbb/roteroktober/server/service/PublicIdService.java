package ch.sbb.roteroktober.server.service;

import ch.sbb.roteroktober.server.exceptions.UniqueKeyGenerationFailedException;
import ch.sbb.roteroktober.server.model.PublicIdEntity;
import ch.sbb.roteroktober.server.repo.PublicIdRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Enthält die Logik für die öffentlichen Schlüssel
 */
@Component
public class PublicIdService {

    /** Anzahl Versuche, eine eindeutige ID zu erhalten */
    private final int DUPLICATE_RETRY = 10;

    /** Unser sicherer Zufallsgenerator */
    private final SecureRandom random = new SecureRandom();

    /** Liste der erlaubten Zeichen */
    private final char[] allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    @Autowired
    private PublicIdRepository publicIdRepository;

    /**
     * Erstellt einen neuen eindeutigen Schlüssel
     * @param prefix Kürzel, welches vor jedem Schlüssel steht
     * @param length Länge des generierten Schlüssels in Anzahl Zeichen, ohne Prefix
     * @return Erstellter Schlüssel
     * @throws UniqueKeyGenerationFailedException Wird ausgelöst, wenn kein eindeutiger Schlüssel erstellt werden konnte
     */
    public PublicIdEntity createNewPublicId(String prefix, int length) throws UniqueKeyGenerationFailedException {
        PublicIdEntity result = null;

        // Anzahl Versuche zählen
        int attemptCount = 0;

        do {
            // Schlüssel erstellen
            String key = generatePublicKey(prefix, length);

            // Schlüssel erstellen
            result = createNewPublicId(key);
        } while (result == null && attemptCount++ < DUPLICATE_RETRY);

        // Wenn wir jetzt immer noch keinen Schlüssel speichern konnte, geben wir eine Exception zurück
        if (result == null) {
            throw new UniqueKeyGenerationFailedException();
        }

        // Schlüssel zurückgeben
        return result;
    }

    /**
     * Speichert einen bestimmten eindeutigen Schlüssel
     * @param key Eindeutiger Schlüssel
     * @return Erstellte Entität. <code>null</code>, wenn der Schlüssel in der Datenbank bereits existiert.
     */
    protected PublicIdEntity createNewPublicId(String key) {
        // Entität erstellen
        PublicIdEntity entity = new PublicIdEntity();
        entity.setPublicId(key);

        try {
            // Schlüssel speichern
            PublicIdEntity result = publicIdRepository.save(entity);
            return result;
        } catch (DataIntegrityViolationException e) {
            // ID scheint bereits zu existieren
            return null;
        }
    }

    protected String generatePublicKey(String prefix, int length) {
        // Zufälliger String in der gewünschten Länge erstellen
        String randomKey = RandomStringUtils.random(length, 0, allowedChars.length, true, true, allowedChars, random);

        // Wenn vorhanden das Prefix hinzufügen
        if(prefix != null){
            randomKey = prefix.toUpperCase() + randomKey;
        }
        return randomKey;
    }
}

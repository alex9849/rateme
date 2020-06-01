package de.hskl.rateme.service;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class AccessService {
    private Map<UUID, Integer> logins = new HashMap<>();

    public boolean isLoggedIn(String uuidString) {
        if(uuidString == null) {
            return false;
        }
        UUID uuid = UUID.fromString(uuidString);
        return isLoggedIn(uuid);
    }

    public boolean isLoggedIn(UUID loginId) {
        return this.logins.containsKey(loginId);
    }

    public Integer getUserId(UUID loginID) {
        return this.logins.get(loginID);
    }

    public UUID login(int userId) {
        for(UUID uuid : this.logins.keySet()) {
            if(this.logins.get(uuid) == userId) {
                this.logins.remove(uuid);
                break;
            }
        }
        UUID uuid = UUID.randomUUID();
        this.logins.put(uuid, userId);
        return uuid;
    }

    public boolean logout(UUID loginId) {
        return this.logins.remove(loginId) != null;
    }


}

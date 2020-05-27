package de.hskl.rateme.controller;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class AccessController {
    private Map<UUID, Integer> logins = new HashMap<>();

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

    public void logout(UUID loginId) {
        this.logins.remove(loginId);
    }


}

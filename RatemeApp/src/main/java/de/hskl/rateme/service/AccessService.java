package de.hskl.rateme.service;

import de.hskl.rateme.model.LoginData;
import de.hskl.rateme.model.User;
import de.hskl.rateme.util.Password;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class AccessService {
    @Inject
    UserService userService;

    private Map<UUID, Integer> logins = new HashMap<>();

    public Integer getUserIdIfLoggedIn(UUID uuid) {
        if(uuid == null)
            return null;
        return this.logins.get(uuid);
    }

    public User getUserIfLoggedIn(UUID loginId) {
        Integer userId = getUserIdIfLoggedIn(loginId);
        if(userId == null)
            return null;
        return userService.loadUser(userId);
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

    public UUID login(LoginData loginData) throws IllegalAccessException {
        User user = userService.loadUser(loginData.getUsername());
        if(user == null) {
            throw new IllegalAccessException("User does not exist!");
        }
        if(!Password.checkPassword(loginData.getPassword(), user.getPassword())) {
            throw new IllegalAccessException("Password invalid!");
        }
        return this.login(user.getId());
    }


}

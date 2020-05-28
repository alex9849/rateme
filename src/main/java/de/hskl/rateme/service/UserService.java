package de.hskl.rateme.service;

import de.hskl.rateme.db.UserDB;
import de.hskl.rateme.model.LoginData;
import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.User;
import de.hskl.rateme.util.Password;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Singleton
public class UserService {
    @Inject
    UserDB userDB;
    @Inject
    AccessService accessService;

    public User createUser(User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(userDB.loadUser(user.getUsername()) != null) {
            throw new RatemeDbException("A user with this username already exists!");
        }
        user.setPassword(Password.getSaltedHash(user.getPassword()));
        userDB.createUser(user);
        return user;
    }

    public User loadUser(int id) {
        return userDB.loadUser(id);
    }

    public UUID loginUser(LoginData loginData) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = userDB.loadUser(loginData.getUsername());
        if(user == null) {
            throw new RatemeDbException("User does not exist!");
        }
        if(!Password.checkPassword(loginData.getPassword(), user.getPassword())) {
            throw new RatemeDbException("Password invalid!");
        }
        return accessService.login(user.getId());
    }

    public boolean logout(UUID loginId) {
        return accessService.logout(loginId);
    }

}

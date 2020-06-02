package de.hskl.rateme.service;

import de.hskl.rateme.db.UserDB;
import de.hskl.rateme.model.User;
import de.hskl.rateme.util.Password;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserService {
    @Inject
    UserDB userDB;

    public User createUser(User user) {
        user.setPassword(Password.hashPassword(user.getPassword(), Password.genSalt()));
        userDB.createUser(user);
        return user;
    }

    public User loadUser(int id) {
        return userDB.loadUser(id);
    }

    public User loadUser(String username) {
        return userDB.loadUser(username);
    }

}

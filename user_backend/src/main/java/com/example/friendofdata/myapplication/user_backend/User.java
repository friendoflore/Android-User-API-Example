package com.example.friendofdata.myapplication.user_backend;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * Created by friendofdata on 3/10/16.
 */

public class User {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity newUser = new Entity("User");
    newUser.setProperty("username", "testuser");

}

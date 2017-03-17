package pt.code5.micro.auth.users.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by eduardo on 17/03/2017.
 */
@Entity("users")
public class User {

    @Id
    private ObjectId id = new ObjectId();

    private String username;
    private String name;
    private String email;
}

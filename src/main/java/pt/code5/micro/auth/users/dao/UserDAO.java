package pt.code5.micro.auth.users.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import pt.code5.micro.auth.users.entities.User;
import pt.code5.micro.utils.database.MorphiaManager;

/**
 * Created by eduardo on 17/03/2017.
 */
public class UserDAO extends BasicDAO<User, ObjectId> {
    private static UserDAO ourInstance = new UserDAO(MorphiaManager.getInstance().getDataStore());

    private UserDAO(Datastore ds) {
        super(ds);
    }

    public static UserDAO getInstance() {
        return ourInstance;
    }


}

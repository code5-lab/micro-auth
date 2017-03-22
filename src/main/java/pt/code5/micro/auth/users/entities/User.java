package pt.code5.micro.auth.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import pt.code5.micro.auth.users.views.Views;

import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

/**
 * Created by eduardo on 17/03/2017.
 */
@Entity("users")
public class User {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id = new ObjectId();

    private String username;
    private String name;
    private String email;

    private boolean active;
    @JsonIgnore
    private String activationToken = UUID.randomUUID().toString();
    @JsonIgnore
    private LocalTime activationRequestTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonView(Views.Public.class)
    private Map properties;

    @JsonView(Views.Private.class)
    private Map privateProperties;

    @JsonView(Views.Secret.class)
    private Map secretProperties;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public Map getPrivateProperties() {
        return privateProperties;
    }

    public void setPrivateProperties(Map privateProperties) {
        this.privateProperties = privateProperties;
    }

    public Map getSecretProperties() {
        return secretProperties;
    }

    public void setSecretProperties(Map secretProperties) {
        this.secretProperties = secretProperties;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public LocalTime getActivationRequestTime() {
        return activationRequestTime;
    }

    public void setActivationRequestTime(LocalTime activationRequestTime) {
        this.activationRequestTime = activationRequestTime;
    }

    public void merge(User other) {
        this.username = other.username;
        this.email = other.email;
        this.name = other.name;

        if (other.password != null && !other.password.equals("")) this.password = other.password;
    }

    public void mergePrivate(User other) {
        this.merge(other);
        this.privateProperties = other.privateProperties;
    }

    public void mergeSecret(User other) {
        this.mergePrivate(other);
        this.secretProperties = other.secretProperties;
    }
}

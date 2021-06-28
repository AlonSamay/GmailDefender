package gmail.data;


import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UsersFilters")
public class UserFilterEntity {

    @Id
    private String email;
    private String userUid;
    private BigInteger historyId;
    private String token;
    @DBRef
    private Set<FilterEntity> filters;


    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    public UserFilterEntity() {

    }

    public BigInteger getHistoryId() {
        return historyId;
    }

    public void setHistoryId(BigInteger historyId) {
        this.historyId = historyId;
    }


    public String getUserUid() {
        return userUid;
    }


    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String userEmail) {
        this.email = userEmail;
    }

    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public UserFilterEntity(String userEmail, String userUid, BigInteger historyId, String token, Set<FilterEntity> filters) {
        super();
        this.email = userEmail;
        this.userUid = userUid;
        this.historyId = historyId;
        this.token = token;
        setFilters(filters);
    }


    public Set<FilterEntity> getFilters() {
        return filters;
    }

    public void setFilters(Set<FilterEntity> filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "UserFilterEntity [userEntityId=" + ", userEmail=" + email + "]";
    }

}

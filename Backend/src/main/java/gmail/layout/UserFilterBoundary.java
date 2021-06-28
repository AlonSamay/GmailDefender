package gmail.layout;


import com.fasterxml.jackson.databind.ObjectMapper;

import gmail.data.UserFilterEntity;

public class UserFilterBoundary {
    private String userEmail;
    private String userUid;

    public UserFilterBoundary() {

    }


    public UserFilterBoundary(UserFilterEntity userFilterEntity) {
        this.userUid = userFilterEntity.getUserUid();
        this.userEmail = userFilterEntity.getEmail();
    }


    public String getUserUid() {
        return userUid;
    }


    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }


    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public UserFilterEntity convertToEntity() {
        UserFilterEntity entity = new UserFilterEntity();


        entity.setEmail(userEmail);
        return entity;

    }


    @Override
    public String toString() {

        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}




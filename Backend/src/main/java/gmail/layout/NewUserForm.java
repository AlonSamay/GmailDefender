package gmail.layout;

import com.fasterxml.jackson.databind.ObjectMapper;

import gmail.data.UserFilterEntity;

public class NewUserForm {
    private String userEmail;

    public NewUserForm() {

    }

    public NewUserForm(String userEmail) {
        this.userEmail = userEmail;
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

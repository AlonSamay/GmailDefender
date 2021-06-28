package gmail.infra;

import java.util.List;

import gmail.data.FilterEntity;
import gmail.data.UserFilterEntity;


public interface UserFilterService {

    void updateUserFilter(String id, UserFilterEntity userFilterEntity);

    void newUserFilter(UserFilterEntity userFilterEntity);

    UserFilterEntity getUserFilter(String userId);

    void addFilter(FilterEntity filter, String userId);

    UserFilterEntity login(String email);

    void deleteAll();


    List<FilterEntity> readAllFilters(String userId, int page, int size);

}

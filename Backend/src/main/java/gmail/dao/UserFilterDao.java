package gmail.dao;

import java.util.List;
import java.util.Optional;

import gmail.data.FilterEntity;
import gmail.data.UserFilterEntity;

public interface UserFilterDao {

    void create(UserFilterEntity userFilterEntity);

    Optional<UserFilterEntity> readById(String key);

    void addFilter(String id, FilterEntity FilterEntity);

    void update(String id, UserFilterEntity userFilterEntity);

    void deleteAll();


}

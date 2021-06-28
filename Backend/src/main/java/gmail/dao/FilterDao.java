package gmail.dao;

import java.util.Optional;

import gmail.data.FilterEntity;

public interface FilterDao {
    void create(FilterEntity filterEntity);

    Optional<FilterEntity> readById(long key);


    void update(long id, FilterEntity filterEntity);

    void deleteAll();


    void delete(FilterEntity filterEntity);

}

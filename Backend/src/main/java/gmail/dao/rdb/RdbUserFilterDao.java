package gmail.dao.rdb;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import gmail.aop.UserNotFoundException;
import gmail.dao.UserFilterDao;
import gmail.data.FilterEntity;
import gmail.data.UserFilterEntity;

@Repository
public class RdbUserFilterDao implements UserFilterDao {
    private UserFilterCrud userFilterCrud;


    @Autowired
    public RdbUserFilterDao(UserFilterCrud userFilterCrud) {
        super();
        this.userFilterCrud = userFilterCrud;
    }


    @Override
    public void create(UserFilterEntity userFilterEntity) {
        // TODO Auto-generated method stub
        this.userFilterCrud.save(userFilterEntity);
    }


    @Override
    public Optional<UserFilterEntity> readById(String key) {

        return this.userFilterCrud.findByEmail(key);
    }


    @Override
    public void update(String id, UserFilterEntity userFilterEntity) {

        UserFilterEntity u = this.userFilterCrud.findByEmail(id).orElseThrow(UserNotFoundException::new);

        if (userFilterEntity.getHistoryId() != null)
            u.setHistoryId(userFilterEntity.getHistoryId());

        if (userFilterEntity.getToken() != null)
            u.setToken(userFilterEntity.getToken());

        if (userFilterEntity.getUserUid() != null)
            u.setUserUid(userFilterEntity.getUserUid());

        this.userFilterCrud.save(u);
    }


    @Override
    public void deleteAll() {
        this.userFilterCrud.deleteAll();
    }


    @Override
    public void addFilter(String id, FilterEntity filterEntity) {
        UserFilterEntity u = this.userFilterCrud.findByEmail(id).orElseThrow(RuntimeException::new);

        Set<FilterEntity> f;

        if (u.getFilters() == null)
            f = new HashSet<>();
        else
            f = u.getFilters();

        f.add(filterEntity);
        u.setFilters(f);
        this.userFilterCrud.save(u);
    }


}

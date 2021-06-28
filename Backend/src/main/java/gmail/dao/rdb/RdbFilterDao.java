package gmail.dao.rdb;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import gmail.aop.FilterNotFoundException;
import gmail.aop.UserAlreadyExistsException;
import gmail.dao.FilterDao;
import gmail.data.FilterEntity;


@Repository
public class RdbFilterDao implements FilterDao {

    private FilterCrud filterCrud;

    @Autowired
    public RdbFilterDao(FilterCrud filterCrud) {
        super();
        this.filterCrud = filterCrud;
    }


    @Override
    public Optional<FilterEntity> readById(long key) {
        return this.filterCrud.findById(key);
    }

    @Override
    public void update(long id, FilterEntity filterEntity) {

        FilterEntity f = this.filterCrud.findById(id).orElseThrow(FilterNotFoundException::new);
        if (!filterEntity.getFilterName().trim().isEmpty())
            f.setFilterName(filterEntity.getFilterName());
        if (!filterEntity.getFrom().trim().isEmpty())
            f.setFrom(filterEntity.getFrom());

        if (!filterEntity.getHasTheWords().trim().isEmpty())
            f.setHasTheWords(filterEntity.getHasTheWords());
        if (!filterEntity.getSubject().trim().isEmpty())
            f.setSubject(filterEntity.getSubject());

        this.filterCrud.save(f);


    }

    @Override
    public void deleteAll() {
        this.filterCrud.deleteAll();
    }

    @Override
    public void delete(FilterEntity filterEntity) {
        this.filterCrud.delete(filterEntity);
    }


    @Override
    public void create(FilterEntity filterEntity) {

        if (!this.filterCrud.existsById(filterEntity.getFilterId()))
            this.filterCrud.save(filterEntity);

        else throw new UserAlreadyExistsException();
    }


}

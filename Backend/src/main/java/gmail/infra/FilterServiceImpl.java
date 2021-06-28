package gmail.infra;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmail.aop.FilterAlreadyExistsException;
import gmail.aop.FilterNotFoundException;
import gmail.aop.IncompatibleFilterDetailsException;
import gmail.dao.FilterDao;
import gmail.dao.UserFilterDao;
import gmail.data.FilterEntity;

@Service
public class FilterServiceImpl implements FilterService {
    private FilterDao filterDao;

    @Autowired
    public FilterServiceImpl(FilterDao filterDao) {
        super();
        this.filterDao = filterDao;
    }


    private boolean validateNewFilter(FilterEntity filterEntity) {
        return filterEntity.getFilterName() != null
                && !filterEntity.getFilterName().trim().isEmpty()
                && filterEntity.getSubject() != null
                && !filterEntity.getSubject().trim().isEmpty();
    }


    @Override
    public void updateFilter(long id, FilterEntity filterEntity) {
        this.filterDao.update(id, filterEntity);
    }


    @Override
    public void newFilter(FilterEntity entity) {

        if (this.filterDao.readById(entity.getFilterId()).isPresent())
            throw new FilterAlreadyExistsException();

        if (!validateNewFilter(entity))
            throw new IncompatibleFilterDetailsException();

        this.filterDao.create(entity);
    }


    @Override
    public FilterEntity getFilter(long filterId) {
        return this.filterDao.readById(filterId).orElseThrow(FilterNotFoundException::new);

    }


    @Override
    public void deleteFilter(long filterId) {
        FilterEntity f = this.filterDao.readById(filterId).orElseThrow(FilterNotFoundException::new);
        this.filterDao.delete(f);
    }

}

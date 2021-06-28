package gmail.infra;

import java.util.List;

import gmail.data.FilterEntity;

public interface FilterService {

    void updateFilter(long id, FilterEntity filterEntity);

    void newFilter(FilterEntity entity);

    FilterEntity getFilter(long filterId);

    void deleteFilter(long filterId);
}


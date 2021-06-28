package gmail.dao.rdb;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import gmail.data.FilterEntity;
import gmail.data.UserFilterEntity;

public interface UserFilterCrud extends PagingAndSortingRepository<UserFilterEntity, String> {
    Optional<UserFilterEntity> findByEmail(@Param("email") String email);
//public List<FilterEntity> findByFilters (@Param("filters") FilterEntity filters, Pageable page);

}

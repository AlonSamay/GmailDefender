package gmail.dao.rdb;


import org.springframework.data.repository.PagingAndSortingRepository;


import gmail.data.FilterEntity;

public interface FilterCrud extends PagingAndSortingRepository<FilterEntity, Long> {


}

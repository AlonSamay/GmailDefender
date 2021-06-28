package gmail.infra;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import gmail.aop.FilterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmail.aop.UserAlreadyExistsException;
import gmail.aop.UserNotFoundException;
import gmail.dao.FilterDao;
import gmail.dao.UserFilterDao;
import gmail.data.FilterEntity;
import gmail.data.UserFilterEntity;

@Service
public class UserFilterServiceImpl implements UserFilterService {

    private final UserFilterDao userDao;
    private final FilterDao filterDao;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public UserFilterServiceImpl(UserFilterDao userDao, FilterDao filterDao,
                                 SequenceGeneratorService sequenceGeneratorService) {
        super();
        this.userDao = userDao;
        this.filterDao = filterDao;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }


    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{1,}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }


    @Override
    public void updateUserFilter(String id, UserFilterEntity userFilterEntity) {
        if (!validateEmail(userFilterEntity.getEmail()))
            throw new RuntimeException();
        this.userDao.readById(id).orElseThrow(UserNotFoundException::new);

        this.userDao.update(id, userFilterEntity);
    }


    @Override
    public void newUserFilter(UserFilterEntity userFilterEntity) {
        if (this.userDao.readById(userFilterEntity.getEmail()).isPresent())
            throw new UserAlreadyExistsException();

        this.userDao.create(userFilterEntity);
    }


    @Override
    public UserFilterEntity getUserFilter(String userId) {
        return this.userDao.readById(userId).orElseThrow(UserNotFoundException::new);
    }


    @Override
    public UserFilterEntity login(String email) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void addFilter(FilterEntity filter, String userId) {
        this.userDao.readById(userId).orElseThrow(UserNotFoundException::new);
        filter.setFilterId(sequenceGeneratorService.generateSequence("filters_sequence"));
        this.filterDao.create(filter);
        this.userDao.addFilter(userId, filter);
    }

    @Override
    public void deleteAll() {
        this.filterDao.deleteAll();
        this.sequenceGeneratorService.delete();
        this.userDao.deleteAll();
    }

    @Override
    public List<FilterEntity> readAllFilters(String userId, int page, int size) {

        UserFilterEntity u = this.userDao.readById(userId).orElseThrow(UserNotFoundException::new);

        if (u.getFilters() == null || u.getFilters().stream() == null) {
            throw new FilterNotFoundException();
        }
        return u.getFilters().stream().skip(page * size)
                .limit(size).collect(Collectors.toList());

    }
}

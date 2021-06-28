package gmail.layout;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gmail.data.FilterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.http.HttpStatus;

import gmail.aop.FilterAlreadyExistsException;
import gmail.aop.FilterNotFoundException;
import gmail.aop.IncompatibleFilterDetailsException;
import gmail.aop.InvalidPaginationDataException;
import gmail.aop.UserAlreadyExistsException;
import gmail.aop.UserNotFoundException;
import gmail.data.UserFilterEntity;
import gmail.infra.FilterService;
import gmail.infra.UserFilterService;

@RestController
public class UserFilterController {

    private final UserFilterService userFilterService;
    private final FilterService filterService;

    private final String baseUrl = "/gmailDefender";
    private final String filters = "/filters";
    private final String users = "/users";


    @Autowired
    public UserFilterController(UserFilterService userFilterService, FilterService filterService) {
        super();
        this.filterService = filterService;
        this.userFilterService = userFilterService;
    }


    @GetMapping(
            path = baseUrl + filters + "/{filterId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public FilterBoundary getFilter(
            @PathVariable("filterId") long filterId) {
        return new FilterBoundary(this.filterService.getFilter(filterId));
    }

    @PutMapping(
            path = baseUrl + filters + "/{filterId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateFilter(
            @PathVariable("filterId") long filterId,
            @RequestBody FilterBoundary filterBoundary) {
        filterBoundary.setFilterId(filterId);
        this.filterService.updateFilter(filterId, filterBoundary.convertToEntity());
    }


    @PostMapping(path = baseUrl + users + "/addFilter/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addFilter(@PathVariable("userId") String userId,
                          @RequestBody FilterBoundary filterBoundary) {

        this.userFilterService.addFilter(filterBoundary.convertToEntity(), userId);
    }

    @GetMapping(path = baseUrl + users + "/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserFilterEntity getUser(@PathVariable("userId") String userId) {
        return this.userFilterService.getUserFilter(userId);

    }

    @DeleteMapping(path = baseUrl + "/{userId}" + "/delete/" + "{filterId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteFilterByUser(@PathVariable("userId") String userId, @PathVariable("filterId") long filterId) {
        this.filterService.deleteFilter(filterId);
    }

    @DeleteMapping(path = baseUrl + "deleteAll")
    public void deleteAll() {
        this.userFilterService.deleteAll();
    }

    @GetMapping(
            path = baseUrl + "/getAllFiltersByUsers",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public FilterBoundary[] getFilterBy(@RequestParam(name = "userEmail") String userEmail,
                                        @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        List<FilterEntity> filtersList = this.userFilterService.readAllFilters(userEmail, page, size);
        return !filtersList.isEmpty() ? filtersList.stream()
                .map(FilterBoundary::new).toArray(FilterBoundary[]::new) : null;

    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, Object> handleError(FilterNotFoundException e) {
        String message = e.getMessage();
        if (message == null || message.trim().length() == 0) {
            message = "Filter not found";
        }
        return Collections.singletonMap("error", message);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, Object> handleError(UserNotFoundException e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "User not found";
        }
        return Collections.singletonMap("error", message);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleError(FilterAlreadyExistsException e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "Filter already exists";
        }
        return Collections.singletonMap("error", message);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleError(UserAlreadyExistsException e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "User already exists";
        }
        return Collections.singletonMap("error", message);
    }


    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleError(IncompatibleFilterDetailsException e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "Filter details supplied are incorrect";
        }
        return Collections.singletonMap("error", message);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleError(InvalidPaginationDataException e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "Invalid pagination data. Please supply page size between 0 and 100";
        }
        return Collections.singletonMap("error", message);
    }
}

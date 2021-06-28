package gmail.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import gmail.data.FilterEntity;

public class NewFilterForm {
    private String filterName;
    private String subject;
    private String from;
    private String hasTheWords;

    public NewFilterForm() {
    }

    public NewFilterForm(String filterName, String subject, String from, String hasTheWords) {
        this.filterName = filterName;
        this.subject = subject;
        this.from = from;
        this.hasTheWords = hasTheWords;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHasTheWords() {
        return hasTheWords;
    }

    public void setHasTheWords(String hasTheWords) {
        this.hasTheWords = hasTheWords;
    }

    public FilterEntity convertToEntity() {
        FilterEntity entity = new FilterEntity();

        entity.setFilterName(filterName);
        entity.setFrom(from);
        entity.setSubject(subject);
        entity.setHasTheWords(hasTheWords);

        return entity;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

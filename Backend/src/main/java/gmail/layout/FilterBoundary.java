package gmail.layout;


import com.fasterxml.jackson.databind.ObjectMapper;

import gmail.data.FilterEntity;

public class FilterBoundary {

    private long filterId;
    private String filterName;
    private String subject;
    private String from;
    private String hasTheWords;

    public FilterBoundary() {

    }

    public FilterBoundary(FilterEntity filterEntity) {

        if(filterEntity != null) {
            this.filterId = filterEntity.getFilterId();
            this.filterName = filterEntity.getFilterName();
            this.from = filterEntity.getFrom();
            this.hasTheWords = filterEntity.getHasTheWords();
            this.subject = filterEntity.getSubject();
        }
    }


    public long getFilterId() {
        return filterId;
    }


    public void setFilterId(long filterId) {
        this.filterId = filterId;
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
        entity.setFilterId(filterId);

        entity.setFilterName(filterName);
        entity.setHasTheWords(hasTheWords);
        entity.setFrom(from);
        entity.setSubject(subject);
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

package gmail.data;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Filters")
public class FilterEntity {
    @Id
    private long filterId;
    private String filterName;
    private String subject;
    private String from;
    private String hasTheWords;

    public FilterEntity() {

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


    public long getFilterId() {
        return filterId;
    }


    public void setFilterId(long filterId) {
        this.filterId = filterId;
    }


    public FilterEntity(long filterId, String filterName, String subject, String from, String hasTheWords) {
        super();
        this.filterId = filterId;
        this.filterName = filterName;
        this.subject = subject;
        this.from = from;
        this.hasTheWords = hasTheWords;
    }

    @Override
    public String toString() {
        return "FilterEntity [filterName=" + filterName + ", filterId=" + filterId + "]";
    }


}

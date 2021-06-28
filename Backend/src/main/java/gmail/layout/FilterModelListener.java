package gmail.layout;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import gmail.data.FilterEntity;
import gmail.infra.SequenceGeneratorService;


@Component
public class FilterModelListener extends AbstractMongoEventListener<FilterEntity> {

    private final SequenceGeneratorService sequenceGenerator;

    @Autowired
    public FilterModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<FilterEntity> event) {
        if (event.getSource().getFilterId() < 1) {
            event.getSource().setFilterId(sequenceGenerator.generateSequence("filters_sequence"));
        }
    }


}

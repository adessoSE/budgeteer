package de.adesso.budgeteer.service.statistics;

import de.adesso.budgeteer.persistence.record.ShareBean;
import de.adesso.budgeteer.service.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class ShareBeanToShareMapper extends AbstractMapper<ShareBean, Share> {

    @Override
    public Share map(ShareBean sourceObject) {
        return new Share(sourceObject.getValue(), sourceObject.getName());
    }
}

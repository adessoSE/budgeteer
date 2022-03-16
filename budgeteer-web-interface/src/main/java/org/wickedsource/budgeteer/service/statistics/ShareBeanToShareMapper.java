package org.wickedsource.budgeteer.service.statistics;

import de.adesso.budgeteer.persistence.record.ShareBean;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class ShareBeanToShareMapper extends AbstractMapper<ShareBean, Share> {

  @Override
  public Share map(ShareBean sourceObject) {
    return new Share(sourceObject.getValue(), sourceObject.getName());
  }
}

package org.wickedsource.budgeteer.service.statistics;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.record.ShareBean;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class ShareBeanToShareMapper extends AbstractMapper<ShareBean, Share> {

	@Override
	public Share map(ShareBean sourceObject) {
		return new Share(sourceObject.getValue(), sourceObject.getName());
	}
}

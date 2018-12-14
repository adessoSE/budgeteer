package de.adesso.budgeteer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;


public class LoggerFilter extends Filter {

    @Override
    public FilterReply decide(Object event) {
        ILoggingEvent event1 = (ILoggingEvent) event;
        if(event1.getMessage().contains("HHH90000014")){
            return FilterReply.DENY;
        }else{
            return FilterReply.NEUTRAL;
        }
    }
}

package de.adesso.budgeteer.core.user;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
@EqualsAndHashCode(callSuper = false)
public class OnEmailChangedEvent extends ApplicationEvent {
    long userId;
    String name;
    String email;

    public OnEmailChangedEvent(long userId, String name, String email) {
        super(userId);
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}

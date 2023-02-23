package org.wickedsource.budgeteer;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class JavaMailMock extends JavaMailSenderImpl {}

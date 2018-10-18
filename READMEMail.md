# Set up local mail server for development

[PR #276](https://github.com/adessoAG/budgeteer/pull/276) adds new features that require a mail server.
Within this manual a local mail server under Windows 10 is set up,
to use all functions within Budgeteer.

## Download and install hMailServer

hMailServer can be downloaded from https://www.hmailserver.com/download.

For server types, _Microsoft SQL Compact_ can be selected.
An administrator password must be specified during installation. 
This must be entered when connecting to the server (IMPORTANT!).

## hMailServer Administrator

After installation, _hMailServer Administrator_ can be started.

A server with the host name _localhost_ is created automatically, which we also use.

A double click connects to the server, but the password specified during installation must be entered.

## Create domain

Under _Domains_ and then on _Add_ we create a new domain. As domain we use **budgeteer.local**.

Under the tab _Advanced_ we set a catch-all address, namely **all@budgeteer.local**.

Click on _Save_ to save the new  domain.

## Create mail account

On the newly created domain, then _Accounts_ and then on _Add_ we create a mail account.

We use the same name for the address as for the catch-all address, namely **all@budgeteer.local**.

A password must be selected.

Click on _Save_ to save the mail account.

A second mail account with the name **noreply@budgeteer.local** should be created additionally.

## Configure SMTP

Under _Settings -> Protocols -> SMTP -> Delivery of e-mail_, **localhost** must be entered in _Local host name_.

Click on _Save_ to save the configuration.

The mail server can now be used.

## Configure Outlook 2013

Under _File -> Account Settings -> Account Settings... -> E-Mail -> New_ the address _all@budgeteer.local_ can be added to Outlook.

Select _Manual configuration or additional server types_ and then _POP or IMAP_.

Then configure as follows:

- **Your name:** Max Mustermann
- **E-mail address:** all@budgeteer.local
- **Account type:** POP3
- **Incoming mail server:** localhost
- **Outgoing mail server (SMTP):** localhost
- **Username:** all@budgeteer.local
- **Password:** PASSWORD FROM all@budgeteer.local

Now all messages to the mail server can be received via Outlook 2013.

## Configure Budgeteer

Configure the _application.properties_ in _budgeteer-web-interface_ as follows:

- budgeteer.mail.activate=true
- spring.mail.host=localhost
- spring.mail.port=25
- spring.mail.username=noreply@budgeteer.local
- spring.mail.password=PASSWORD FROM noreply@budgeteer.local
- spring.mail.properties.mail.smtp.auth=true
- spring.mail.properties.mail.smtp.starttls.enable=true

## Deactivating mail functions

To deactivate the mail functions, **budgeteer.mail.activate=false** can be set. Then no mail server is needed anymore, because no more mails are sent, but you still have to, for example, enter a mail address when registering.
How to configure Keycloak for Budgeteer.
1. Download Keycloak [here](https://www.keycloak.org/downloads.html). 

2. Go to the bin/ directory of the server distribution.
To boot the server:

Linux/Unix

`$ ./standalone.sh`

Windows

`> .\standalone.bat`

3. After the server boots, open your browser and go the
[http://localhost:8080/auth](http://localhost:8080/auth) URL.

4. Keycloak has no admin account by default. Therefor you have
to create an admin account on the welcome page.

5. Once you've created an admin account. You can log in to the Admin Console by clicking on the Administration Console link or by using the [http://localhost:8080/auth/admin/](http://localhost:8080/auth/admin/) URL.
Afterwards the Admin Page opens.

6. Now you're in the Master realm. You can now create a new realm. Simply hover over Master and a button "Add Realm" should appear.
Budgeteer has the default realm demo.

7. To use Keycloak for Budgeteer, you have to create a Budgeteer client. The sidebar has the button "Clients", simply click on "Clients" and then "Create". The default "Client ID" for Budgeteer is budgeteer.
Set "Access Type" to "confidential" and type in a "Valid Redirect URL". Budgeteer has the default http://localhost:8080/* URL.
Click on "Save" at the bottom of the site.

8. Once you've completed these steps there should be a new tab called "Credentials" at the top of the site. Open the tab "Credentials" and copy the "Secret" key that is displayed next to the "Regenerate Secret" button.
Go to the application.properties of Budgeteer and replace the default "keycloak.credentials.secret" with the new key. Now check the defaults for Keycloak.
The defaults should be: 
                        
                        //to activate Keycloak for Budgeteer, set adapter.keycloak.activated true
                        adapter.keycloak.activated=false
                        keycloak.realm=demo
                        keycloak.auth-server-url=http://localhost:8180/auth/
                        keycloak.ssl-required=external
                        keycloak.resource=budgeteer
                        //here should be the current secret key
                        keycloak.credentials.secret=eb803c5d-7377-454b-8797-95ce6d02c7cd
                        keycloak.use-resource-role-mappings=false
                        # Exclude autoconfiguration for keycloak
                        #spring.autoconfigure.exclude=org.keycloak.adapters.springboot.KeycloakSpringBootConfiguration
                        
If these defaults are not given, change them. If in doubt use the given defaults in the READMEKeycloak, but don't forget to set "adapter.keycloak.activated" true and to replace the old "secret" key with the current key.

9. Afterwards go to "Realm Settings" open the "Login" tab and enable "User registration". Now you can either create a new User on the "Login Page" or create one as an admin with the admin console.

10. Now we head to "Roles" , add a new role called "user" and add it to the "Realm Default Roles". Now every user created on the Login Page or created by an admin has the default role "user".

11. At the end we close Keycloak and launch it again:

Linux / Unix

`$ .../bin/standalone.sh -Djboss.socket.binding.port-offset=100`

Windows

`> ...\bin\standalone.bat -Djboss.socket.binding.port-offset=100`

Now Keycloak has the [http://localhost:8180/](http://localhost:8180/) address.
Launch your Database and afterwards Budgeteer.

And don't forget to have FUN!
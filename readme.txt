This sample shows how to remote lookup a Stateless Session Bean in jboss-5.1.0.GA
(Assume clint is in a differnet IP-192.168.125.9)

1. jboss-ejb-client.properties:

   #wildfly-8
   java.naming.factory.initial=org.jboss.naming.remote.client.InitialContextFactory
   java.naming.provider.url=http-remoting://192.168.12.2:8080
   jboss.naming.client.ejb.context=true
   java.naming.security.principal=app1
   java.naming.security.credentials=pass123!

	#JBOSS
   java.naming.factory.initial=org.jnp.interfaces.NamingContextFactory
   java.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces
   java.naming.provider.url=localhost

	#Weblogic
    weblogic.jndi.Environment environment = new weblogic.jndi.Environment();
    environment.setInitialContextFactory(
    weblogic.jndi.Environment.DEFAULT_INITIAL_CONTEXT_FACTORY);
    environment.setProviderURL("t3://bross:4441");
    environment.setSecurityPrincipal("guest");
    environment.setSecurityCrendentials("guest");
    InitialContext ctx = environment.getInitialContext();
         
2. jboss-5.1.0.GA (IP is 192.168.12.2)
   Deploy jar to JBoss8
   HelloWorldSessionBean-0.0.1-SNAPSHOT.jar
   
3. Create App User in JBoss
   User: app1
   Password: pass123!

4. jboss-5.1.0.GA, for remote access to EJBs, use the ejb: namespace with the following syntax:

   For stateless beans:
      ejb:<app-name>/<module-name>/<distinct-name>/<bean-name>!<fully-qualified-classname-of-the-remote-interface>

   For stateful beans:
      ejb:<app-name(Ear)>/<module-name(Jar)>/<distinct-name>/<bean-name(Concert)>!<fully-qualified-classname-of-the-remote-interface(fullpath of remote interface)>?stateful

5. Home interface and remote interface:
   5.1. Home interface:
        (1). Client is in same WebApp(Ear or War ) in a JVM
        (2). Direct method call(Not RMI)
        (3). Parameter pass by reference

   5.2. Remote interface:
        (1). Client is in different JVM or different webapp (even in same JBM, different web app can'e make local call)
        (2). Remote component interface will be handled via remote method invocation (RMI)
        (3). Parameter pass by value   

package com.york.client;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.york.businesslogic.*;
import com.york.entity.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

public class Client {
	public static void main(String[] args) {
		TestManageEmployeeBean.testManageEmployeeBean();

		MDBTester jmsTester = new MDBTester();
		jmsTester.testMessageBeanEjb();
	}
}

class TestManageEmployeeBean {
	public static void main(String[] args) {
		testManageEmployeeBean();

		MDBTester jmsTester = new MDBTester();
		jmsTester.testMessageBeanEjb();
		
	}
	
	public static void testManageEmployeeBean() {
		EmployeeRemote remote = doLookupManageEmployeeBean();
		Employee employee = new Employee();
		employee.setFirstName("Mark");
		employee.setLastName("King");
		employee.setEmail("mking@gmail.com");

		Employee employee1 = new Employee();
		employee1.setFirstName("Mathew");
		employee1.setLastName("king");
		employee1.setEmail("mtking@gmail.com");
		remote.addEmployee(employee);
		remote.addEmployee(employee1);
	}

	private static EmployeeRemote doLookupManageEmployeeBean() {
		Context context = null;
		EmployeeRemote bean = null;
		try {
			// 1. Obtaining Context
			context = getInitialContext();
			// 2. Lookup and cast
			bean = (EmployeeRemote) context.lookup(LOOKUP_STRING);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return bean;
	}

	private static final String LOOKUP_STRING = "ManageEmployeeBean/remote";
	/*
	 *  * location of JBoss JNDI Service provider the client will use. It should
	 * be * URL string.
	 */
	//private static final String PROVIDER_URL = "http-remoting://localhost:8080";
	private static final String PROVIDER_URL = "jnp://localhost:1099";
	/*
	 *  * specifying the list of package prefixes to use when loading in URL *
	 * context factories. colon separated
	 */

	private static final String JNP_INTERFACES = "org.jboss.naming:org.jnp.interfaces";
	/*
	 *  * Factory that creates initial context objects. fully qualified class
	 * name.
	 */
	private static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
	private static Context initialContext;

	public static Context getInitialContext() throws NamingException {
		if (initialContext == null) {
			// Properties extends HashTable
			Properties prop = new Properties();
			prop.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			prop.put(Context.URL_PKG_PREFIXES, JNP_INTERFACES);
			prop.put(Context.PROVIDER_URL, PROVIDER_URL);
			initialContext = new InitialContext(prop);
		}
		return initialContext;
	}
}



class MDBTester {

	BufferedReader brConsoleReader = null;
	Properties props;
	InitialContext ctx;
	{
		props = new Properties();
		try {
			props.load(new FileInputStream("jndi.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			ctx = new InitialContext(props);
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
		brConsoleReader = new BufferedReader(new InputStreamReader(System.in));
	}
/*
	public static void main(String[] args) {

		MDBTester jmsTester = new MDBTester();

		jmsTester.testMessageBeanEjb();
	}
*/
	
	private void showGUI() {
		System.out.println("**********************");
		System.out.println("Welcome to Book Store");
		System.out.println("**********************");
		System.out.print("Options \n1. Add Book\n2. Exit \nEnter Choice: ");
	}

	public void testMessageBeanEjb() {

		try {
			int choice = 1;
			Queue queue = (Queue) ctx.lookup("/queue/BookQueue");
			QueueConnectionFactory factory = (QueueConnectionFactory) ctx
					.lookup("ConnectionFactory");
			QueueConnection connection = factory.createQueueConnection();
			QueueSession session = connection.createQueueSession(false,
					QueueSession.CLIENT_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);

			while (choice != 2) {
				String bookName;
				showGUI();
				String strChoice = brConsoleReader.readLine();
				choice = Integer.parseInt(strChoice);
				if (choice == 1) {
					System.out.print("Enter book name: ");
					bookName = brConsoleReader.readLine();
					Book book = new Book();
					book.setName(bookName);
					ObjectMessage objectMessage = session
							.createObjectMessage(book);
					sender.send(objectMessage);
				} else if (choice == 2) {
					break;
				}
			}

			LibraryPersistentBeanRemote libraryBean = (LibraryPersistentBeanRemote) ctx
					.lookup("LibraryPersistentBean/remote");

			List<Book> booksList = libraryBean.getBooks();

			System.out.println("Book(s) entered so far: " + booksList.size());
			int i = 0;
			for (Book book : booksList) {
				System.out.println((i + 1) + ". " + book.getName());
				i++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (brConsoleReader != null) {
					brConsoleReader.close();
				}
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}

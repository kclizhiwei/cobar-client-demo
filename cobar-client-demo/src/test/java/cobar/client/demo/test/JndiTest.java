package cobar.client.demo.test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import cobar.client.demo.model.User;
import cobar.client.demo.service.UserService;

@ContextConfiguration(locations={"classpath:applicationContext-jndi.xml"})
public class JndiTest extends BaseTest{
	@Resource
	private UserService userService;
	
	@BeforeClass
	public static void initDataSources() throws RemoteException, NamingException {
		LocateRegistry.createRegistry(1099);
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
		System.setProperty(Context.PROVIDER_URL, "rmi://localhost:1099");
		
		InitialContext context = new InitialContext();
		
		Reference ref0 = new Reference("javax.sql.DataSource", "org.apache.commons.dbcp.BasicDataSourceFactory", null);
		ref0.add(new StringRefAddr("driverClassName", "oracle.jdbc.driver.OracleDriver"));
		ref0.add(new StringRefAddr("url", "jdbc:oracle:thin:@localhost:1521/xe"));
		ref0.add(new StringRefAddr("username", "dbtest1"));
		ref0.add(new StringRefAddr("password", "root"));
		
		Reference ref1 = new Reference("javax.sql.DataSource", "org.apache.commons.dbcp.BasicDataSourceFactory", null);
		ref1.add(new StringRefAddr("driverClassName", "oracle.jdbc.driver.OracleDriver"));
		ref1.add(new StringRefAddr("url", "jdbc:oracle:thin:@localhost:1521/xe"));
		ref1.add(new StringRefAddr("username", "dbtest2"));
		ref1.add(new StringRefAddr("password", "root"));
		
		Reference ref2 = new Reference("javax.sql.DataSource", "org.apache.commons.dbcp.BasicDataSourceFactory", null);
		ref2.add(new StringRefAddr("driverClassName", "oracle.jdbc.driver.OracleDriver"));
		ref2.add(new StringRefAddr("url", "jdbc:oracle:thin:@localhost:1521/xe"));
		ref2.add(new StringRefAddr("username", "dbtest3"));
		ref2.add(new StringRefAddr("password", "root"));
		
		context.rebind("jdbc/dbtest1", ref0);
		context.rebind("jdbc/dbtest2", ref1);
		context.rebind("jdbc/dbtest3", ref2);
		
		context.close();
	}
	@Test
	public void testAdd() throws Exception {
		User user = new User();
		Long id = new Long(new Random().nextInt(10000));
		user.setId(id);
		logger.info("id : {}",id);
		user.setName("Tom");
		Long taobaoId = id;
		user.setTaobaoId(taobaoId);
		userService.addUser(user);
	}
	@Test
	public void testGetUserBykey() throws Exception{
		User user = new User();
		user.setId(7658L);
		User user2 = userService.getUserByKey(user);
		logger.info(user2.toString());
	}
}

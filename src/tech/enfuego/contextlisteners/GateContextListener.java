package tech.enfuego.contextlisteners;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import gate.Gate;
import gate.util.GateException;

@WebListener
public class GateContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Initialising A NEW INFORMATAION EXTRACTION ENGINE(ANNIE)....");
		String GATE_HOME = this.getClass().getResource("/gate-service").getPath();
		System.out.println("GATE HOME IS..."+GATE_HOME);
		File f = new File(GATE_HOME);
		System.out.println(f.getAbsolutePath());
		Gate.setGateHome(f);
		 try {
			Gate.init();
		} catch (GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}

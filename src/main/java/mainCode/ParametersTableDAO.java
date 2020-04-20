package mainCode;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

public class ParametersTableDAO {
	private static List<ParametersTable> ParameterList = new ArrayList<ParametersTable>();

	public static List<ParametersTable> getParmaeters() {
		List<ParametersTable> list = new ArrayList<ParametersTable>();
		list.addAll(ParameterList);
		return list;
	}
	// returns specific Parameter value
	public static String returnValue(String Parameter, String Arribute) {
		getParametersFromDatabase();
		String Value="";
		for(ParametersTable parameter : ParameterList) {
			if(parameter.getAttribute().equals(Arribute) && parameter.getParameter().equals(Parameter)) {
				Value=parameter.getValue();
				break;
			}
		}
		return Value;
		
	}
	
	// This method gets parameters data from database. 
		public static List<ParametersTable> getParametersFromDatabase() {
			Session session = DataSource.getSessionFactory().openSession();
			@SuppressWarnings("unchecked")
			List<ParametersTable> parameters = session.createQuery("FROM ParametersTable").list();
			// Closing The Session Object
			session.close();
			ParameterList=parameters;
			return parameters;
		}
}

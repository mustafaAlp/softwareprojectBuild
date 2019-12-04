
public class Driver {

	private static String pJson;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		pJson = 
				"{\n" + 
				"        \"object_type\": \"ot\" ,\n" + 
				"        \"operation\": \"op\",\n" + 
				"        \"status\": \"st\",\n" + 
				"        \"description\": \"de\",\n" + 
				"        \"method\": \"change me\",\n" + 
				"        \"card_id\": \"change me\",\n" + 
				"        \"project_name\": \"my-app\",\n" +//todo directory alanı eklenecek
				"        \"repository_url\": \"https://github.com/gokhanhas/my-app.git\",\n" + 
				"        \"github_login\": \"gokhanhas\",\n" + 
				"        \"github_password\": \"hasGTUİtiraf\"\n" +  //ToDo Şİfreyi doğru gir    
				"}\n";
				
		System.out.println(pJson);
		
		Main.build(pJson);
	}

}
//url username password eklenicek
 

import java.io.IOException;

public class Driver {

	private static String pJson;
	
	public static void main(String[] args) throws IOException {
		testPostJson();
		
	}
	
	
	/**
	 * test for Main.build()  method
	 */
	public static void testMainBuild(){
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
	
	public static void testPostJson() throws IOException{
		
		String url  = "http://dummy.restapiexample.com/api/v1/create";
		String json = "{\"name\":\"Mustafa Alp\",\"salary\":\"10000\",\"age\":\"29\"}";
		
		String responce = (HttpRequest.PostJson(url, json));
		System.out.print(responce);
	}
	
	

}
//url username password eklenicek
 

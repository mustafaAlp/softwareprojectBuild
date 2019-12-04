
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;

import org.json.JSONObject;

public class Main {

    private static String login;
    private static String password;
    private static String  url;
    private static String  projectName;
    private static String JsonStr;
    private static String object_type;
    private static String card_id;
    private static String method;
    private static String destination;
    

    public static void build(String pJson) {

        try {

     
            JsonStr = null;

            JsonStr = pJson;
            parse_Json();

            PrintStream out = new PrintStream(new FileOutputStream("report.txt"));
            System.setOut(out);
            
         
            Maven_Builder project = new Maven_Builder(url , projectName , login , password);

            project.build(); //build project
            

            Boolean signal  = null; //to send request
            BufferedReader br = new BufferedReader(new FileReader("report.txt")); //read report from the file

            String sCurrentLine;
            String report = new String();

            while ((sCurrentLine = br.readLine()) != null) {
                report += sCurrentLine;
            }
            
            if(report.contains("BUILD SUCCESS")){
                signal = true; // request signal to send
            }
            else {
                signal = false; // request to send
            }
            br.close();

            project.push(signal);

            if(signal) {
            	destination = "test";
            }
            else {
            	destination = "plan";
            }

            String json_res = new String();
			json_res += "{"+"\n"+
					"\"destination\" : " + destination +"\","+"\n"+
                    "\"object_type\" : \"response\" ,"+"\n"+
                    "\"status\" :  \"" + signal.toString()+ "\","+"\n"+
                    "\"description\" : \""+report+"\","+"\n"+
//                    "\"commit_ID\" : \"" +project.getCommitID()+"\","+"\n"+
                    "\"github_login\" : \" "+login.toString()+"\","+"\n"+
                    "\"github_password\" : \""+password.toString()+"\","+"\n"+
                    "\"repository_url\" : \""+url.toString()+"\","+"\n"+
                    "\"project_name\" : \""+projectName.toString()+"\" ,"+"\n"+
                    "\"method\" : \"check-build-status\""+"\n"+
                    "} ";
            File file = new File("output.json");

            // creates the file
            file.createNewFile();

            // creates a FileWriter Object
            FileWriter writer = new FileWriter(file);

            // Writes the content to the file
            writer.write(json_res);
            writer.flush();
            writer.close();

           	Runtime.getRuntime().exec("python3 CONNECTOR/Connector.py output.json" );
            	//dizinler değişicek , X



        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(1);
        }finally {
            File delete = new File("report.txt");
            delete.delete();
        }
    }
    private static void parse_Json() throws Exception {
    	

        JSONObject obj = new JSONObject(JsonStr);
        projectName = obj.getString("project_name");
        login = obj.getString("github_login");
        password = obj.getString("github_password");
        method = obj.getString("method");
        object_type = obj.getString("object_type");
        url = obj.getString("repository_url");
        
        //url = "https://" + url.substring(4) + ".git";
        
        card_id = obj.getString("card_id");


    }



}

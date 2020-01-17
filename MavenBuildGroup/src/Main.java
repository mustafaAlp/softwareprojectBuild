
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.json.JSONException;
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
    private static String fileName = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
    

    public static void build(String pJson) {

        try {

            JsonStr = pJson;
            parse_Json();

            PrintStream out = new PrintStream(new FileOutputStream(fileName));
            System.setOut(out);
            
         
            Maven_Builder project = new Maven_Builder(url , projectName , login , password);

            project.build(); //build project
            

            Boolean signal  = null; //to send request
            BufferedReader br = new BufferedReader(new FileReader(fileName)); //read report from the file

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
            	destination = "7";
            }
            else {
            	destination = "2";
            }

            String json_res = new String();
			json_res += "{\n"+
					"\"destination\" : " + destination + "\",\n"+
                    "\"object_type\" : \"response\" ,\n"+
                    "\"status\" :  \"" + signal.toString()+ "\",\n"+
                    "\"description\" : \"" + report + "\",\n"+
                    "\"commit_ID\" : \"" +project.getCommitID() + "\",\n"+
                    "\"github_login\" : \" " + login.toString() + "\",\n"+
                    "\"github_password\" : \"" + password.toString() + "\",\n"+
                    "\"repository_url\" : \"" + url.toString() + "\",\n"+
                    "\"project_name\" : \"" + projectName.toString() + "\" ,\n"+
                    "\"method\" : \"check-build-status\"\n"+
                    "} ";
			
			String postUrl = "http://localhost:8081";
			String url  = "http://dummy.restapiexample.com/api/v1/create";

			System.err.print("responce " + HttpRequest.PostJson(postUrl, json_res));
			

        } catch (NoFilepatternException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (WrongRepositoryStateException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (JGitInternalException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (ConcurrentRefUpdateException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (NoMessageException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (NoHeadException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (InvalidRemoteException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);        
        } catch (JSONException e) {
            System.err.println(e.toString());
            System.exit(1);        
        } catch (MavenInvocationException e) {
        	System.err.println("");
            System.err.println(e.toString());
            System.exit(1);     
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(1);      
        }finally {
            File delete = new File(fileName);
            delete.delete();
        }
    }
    
    /**
     * method that parses json string 
     * 
     *  
     * @throws JSONException 
     */
    private static void parse_Json() throws JSONException   {
    	

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

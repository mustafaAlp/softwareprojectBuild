package BuilSocketServer;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private static String commit;
    private static String method;
    private static String destination;
//    private static String fileName = new SimpleDateFormat("yyyyMMddHHmmSSS'.txt'").format(new Date());
    private static String forBuild;

    
    public static void build(String pJson) throws FileNotFoundException {
    	
    	PrintStream log = new PrintStream(new FileOutputStream("BuildLog.txt"));
        try {
            JsonStr = pJson;
            System.err.print(JsonStr);
            parse_Json();

            
            PrintStream out = new PrintStream(new FileOutputStream("report.txt"));
            
            //PrintStream out = new PrintStream(new FileOutputStream(fileName));
            System.setOut(out);
            log.println(log("Build Started "));

            TimeUnit.SECONDS.sleep(3);
            Maven_Builder project = new Maven_Builder(url , projectName , login , password, forBuild);
            project.build(); //build project
            log.println(log("Build Completed "));

            
            Boolean signal  = null; //to send request
            BufferedReader br = new BufferedReader(new FileReader("report.txt")); //read report from the file
            //BufferedReader br = new BufferedReader(new FileReader(fileName)); //read report from the file

            String sCurrentLine;
            String report = new String();
            while ((sCurrentLine = br.readLine()) != null) {
                report += sCurrentLine;
                 if(report.contains("BUILD SUCCESS")){
                    signal = true; // request signal to send
                    break;
                 }
            }
            
            log.println(log("Build Status Verified"));
            
            if(report.contains("BUILD SUCCESS")){
                signal = true; // request signal to send
                TimeUnit.SECONDS.sleep(2);                
                project.push(signal);
            }
            else {
                signal = false; // request to send
            }
            br.close();
            // push was here

            if(signal) {
            	String postUrl = "http://localhost:8081";
    			destination = "2";
    			// JSon objesi hazirlama ...
                JSONObject jsonPLan = new JSONObject();
                jsonPLan.put("destination", destination);
                jsonPLan.put("object_type", "response");
                jsonPLan.put("origin", "1");
                jsonPLan.put("operation", "1");
                jsonPLan.put("status", signal.toString());
                //   jsonPLan.put("description", report);
                //   jsonPLan.put("commit_ID", commit);
                jsonPLan.put("github_login", login.toString());
                jsonPLan.put("github_password", password.toString());
                jsonPLan.put("repository_url", url.toString());
                jsonPLan.put("project_name", projectName.toString());
                jsonPLan.put("buildresult", "success");
                jsonPLan.put("targetPswd", "gtu2017A");
                jsonPLan.put("forBuild", forBuild);
                
                
                log.println(log("JSon to PLAN (Success) "));
    			
    			HttpRequest.PostJson(postUrl, jsonPLan.toString());
                

            	
            	destination = "7";
            	//commit = project.getCommitID();
    			// JSon objesi hazirlama ...
                JSONObject jsonTest = new JSONObject();
                jsonTest.put("destination", destination);
                jsonTest.put("object_type", "response");
                jsonTest.put("origin", "1");
    			jsonTest.put("operation", "1");
    			jsonTest.put("status", signal.toString());
    			//	jsonTest.put("description", report);
    			//  jsonTest.put("commit_ID", commit);
    			jsonTest.put("github_login", login.toString());
    			jsonTest.put("github_password", password.toString());
    			jsonTest.put("repository_url", url.toString());
    			jsonTest.put("project_name", projectName.toString());
    			jsonTest.put("buildresult", "success");
    			jsonTest.put("targetPswd", "gtu2017A");
    			jsonTest.put("forBuild", forBuild);
    			
    			
                log.println(log("JSon to TEST (Success) "));
    			
    			HttpRequest.PostJson(postUrl, jsonTest.toString());
                
    			
            }
            else {
            	destination = "2";
                commit = "0";
                Runtime.getRuntime().exec("rm -rf /tmp/" + "GtuDevOps" ); 
    			// JSon objesi hazirlama ...
                JSONObject json = new JSONObject();
    			json.put("destination", destination);
    			json.put("object_type", "response");
    			json.put("origin", "1");
    			json.put("operation", "1");
    			json.put("status", signal.toString());
    			//json.put("description", report);
    			//json.put("commit_ID", commit);
    			json.put("github_login", login.toString());
    			json.put("github_password", password.toString());
    			json.put("repository_url", url.toString());
    			json.put("project_name", projectName.toString());
    			json.put("buildresult", "fail");
    			json.put("targetPswd", "gtu2017A");
    			
    			String postUrl = "http://localhost:8081";
    			
    			
    			log.println(log("JSon to TEST (Fail) "));
    			HttpRequest.PostJson(postUrl, json.toString());
            }

			
        } catch (NoFilepatternException e) {
        	log.println(log(e.toString()));
        } catch (WrongRepositoryStateException e) {
        	log.println(log(e.toString()));
            
        } catch (JGitInternalException e) {
        	log.println(log(e.toString()));
           
        } catch (ConcurrentRefUpdateException e) {
        	log.println(log(e.toString()));
            
        } catch (NoMessageException e) {
        	log.println(log(e.toString()));
            
        } catch (NoHeadException e) {
        	log.println(log(e.toString()));
            
        } catch (InvalidRemoteException e) {
        	log.println(log(e.toString()));
            
        } catch (IOException e) {
        	log.println(log(e.toString()));       
        } catch (JSONException e) {
        	log.println(log(e.toString()));
                  
        } catch (MavenInvocationException e) {
        	log.println(log(e.toString()));
                
        } catch (Exception e) {
        	log.println(log(e.toString()));
                
        }finally {
        	log.println(log("FINALLY"));
        	log.close();
//            File delete = new File("report.txt");
//            File delete = new File(fileName);
//            delete.delete();
        }
    }
    
    public static String log (String inp){
    	return ("STAGE : " + inp +", Time Stamp : "  + new SimpleDateFormat("yyyyMMddHHmmSSS").format(new Date()));
    }
    
    
    /**destination
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
        object_type = obj.getString("object_type");
        url = obj.getString("repository_url");
        forBuild = obj.getString("forBuild");
        
        /*
        System.err.print("\n" + url + "  ******\n");
        System.err.print("\n" + forBuild + "  ******\n");
        */
    }
}

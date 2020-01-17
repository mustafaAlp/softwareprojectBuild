package BuilSocketServer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private  String login;
    private  String password;
    private  String  url;
    private  String  projectName;
    private  String JsonStr;
    private  String object_type;
    private  String card_id;
    private  String method;
    private  String destination;
    private  String fileName ;
    

    public  void build(String pJson) throws IOException {

        try {

            JsonStr = pJson;
            parse_Json();
            
            fileName = "report-" + new SimpleDateFormat("yyyyMMddHHmmSSS'.txt'").format(new Date());
            System.err.println("\n\n"+ fileName + "\n\n");
            
            
            PrintStream sys = System.out; 
            FileOutputStream reportFile = new FileOutputStream("report.txt", false);
            reportFile.flush();
            
            PrintStream out = new PrintStream(reportFile, true, StandardCharsets.UTF_8.toString());
            System.setOut(out);            
//            File delete = new File("report.txt");        
//            delete.delete();
            

            
            Maven_Builder project = new Maven_Builder(url , projectName , login , password);

            System.err.print("BUILD STARTED0\n");
            project.build(); //build project
            System.err.print("BUILD STARTED1\n");
            
            System.setOut(sys);
//            reportFile.close();
    		System.out.println("\nhelloo world");


            Boolean signal  = null; //to send request
            BufferedReader br = new BufferedReader(new FileReader("report.txt")); //read report from the file

            String sCurrentLine;
            String report = new String();

            while ((sCurrentLine = br.readLine()) != null) {
//            	System.err.print(sCurrentLine+"\n");
//            	if(sCurrentLine.charAt(0) != '\u0000')
            		report += sCurrentLine;
            }
            
            if(report.contains("BUILD SUCCESS")){
            	System.err.print("BUILD SUCCESS\n");
                signal = true; // request signal to send
            }
            else {
                signal = false; // request to send
            }
            br.close();

            project.push(signal);
            String commit;

            if(signal) {
            	destination = "7";
            	commit = project.getCommitID();
    			// JSon objesi hazirlama ...
                JSONObject jsonTest = new JSONObject();
                jsonTest.put("destination", destination);
                jsonTest.put("object_type", "response");
                jsonTest.put("origin", "1");
    			jsonTest.put("operation", "1");
    			jsonTest.put("status", signal.toString());
    			jsonTest.put("description", report);
    			jsonTest.put("commit_ID", commit);
    			jsonTest.put("github_login", login.toString());
    			jsonTest.put("github_password", password.toString());
    			jsonTest.put("repository_url", url.toString());
    			jsonTest.put("project_name", projectName.toString());
    			jsonTest.put("method", "check-build-status");
    			
    			String postUrl = "http://localhost:8081";
    			
    			System.err.print("Test response " + HttpRequest.PostJson(postUrl, jsonTest.toString()));
    			
    			destination = "2";
    			// JSon objesi hazirlama ...
                JSONObject jsonPLan = new JSONObject();
                jsonPLan.put("destination", destination);
                jsonPLan.put("object_type", "response");
                jsonPLan.put("origin", "1");
                jsonPLan.put("operation", "1");
                jsonPLan.put("status", signal.toString());
                jsonPLan.put("description", report);
                jsonPLan.put("commit_ID", commit);
                jsonPLan.put("github_login", login.toString());
                jsonPLan.put("github_password", password.toString());
                jsonPLan.put("repository_url", url.toString());
                jsonPLan.put("project_name", projectName.toString());
                jsonPLan.put("method", "check-build-status");
    			
    			
    			System.err.print("PLan response " + HttpRequest.PostJson(postUrl, jsonPLan.toString()));
            }
            else {
            	destination = "2";
                commit = "0";
    			// JSon objesi hazirlama ...
                JSONObject json = new JSONObject();
    			json.put("destination", destination);
    			json.put("object_type", "response");
    			json.put("origin", "1");
    			json.put("operation", "1");
    			json.put("status", signal.toString());
    			json.put("description", report);
    			json.put("commit_ID", commit);
    			json.put("github_login", login.toString());
    			json.put("github_password", password.toString());
    			json.put("repository_url", url.toString());
    			json.put("project_name", projectName.toString());
    			json.put("method", "check-build-status");
    			
    			String postUrl = "http://localhost:8081";
    			
    			System.err.print("response Fail" + HttpRequest.PostJson(postUrl, json.toString()));
            }

            
//			System.err.print("\n\n\nREPORT: " + report + "\n");

			
			//out.flush(); Truncate etmenin yolunu bul

        } catch (NoFilepatternException e) {
            System.err.println(e.toString());
            
        } catch (WrongRepositoryStateException e) {
            System.err.println(e.toString());
           
        } catch (JGitInternalException e){
            System.err.println(e.toString());
           
            Runtime.getRuntime().exec("rm -r /tmp/" + projectName.toString()); //remove directory
            
        } catch (ConcurrentRefUpdateException e) {
            System.err.println(e.toString());
            
        } catch (NoMessageException e) {
            System.err.println(e.toString());
            
        } catch (NoHeadException e) {
            System.err.println(e.toString());
            
        } catch (InvalidRemoteException e) {
            System.err.println(e.toString());
            
        } catch (IOException e) {
            System.err.println(e.toString());
                    
        } catch (JSONException e) {
            System.err.println(e.toString());
                    
        } catch (MavenInvocationException e) {
        	
            System.err.println(e.toString());
                 
        } catch (Exception e) {
            System.err.println(e.toString());
                  
        }/*finally {
            
        }*/
        /*File delete = new File("report.txt");        
        delete.delete();*/
        
    }
    
    /**
     * method that parses json string 
     * 
     *  
     * @throws JSONException 
     */
    private  void parse_Json() throws JSONException   {
    	

        JSONObject obj = new JSONObject(JsonStr);
        projectName = obj.getString("project_name");
        login = obj.getString("github_login");
        password = obj.getString("github_password");
        method = obj.getString("method");
        object_type = obj.getString("object_type");
        url = obj.getString("repository_url");
        
        //url = "https://" + url.substring(4) + ".git";
        
        //card_id = obj.getString("card_id");


    }



}

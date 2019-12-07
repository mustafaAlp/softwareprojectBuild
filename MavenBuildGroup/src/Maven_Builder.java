
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Maven_Builder {

    private String url;
    private String directory; // change project name
    private Git git;
    private RevCommit commit;
    private String username;
    private String password;


    public Maven_Builder(String target_url , String direc , String username_ , String pass){
        this.url = target_url;
        this.directory = direc;
        this.username = username_;
        this.password = pass;
    }

    public void build() throws Exception {

	
        Runtime.getRuntime().exec("rm -r /tmp/" + directory ); //remove directory
        // 1-clone

        git = Git.cloneRepository()
                .setURI( url )
                .setDirectory(new File("/tmp/" + directory))
                .call();


        
        InvocationRequest request = new DefaultInvocationRequest();

        request.setPomFile( new File( "/tmp/" + directory + "/pom.xml" ) ); //set pom file to run

        request.setGoals( Collections.singletonList( "clean" ) ); //set clean command
        request.setGoals( Collections.singletonList( "compile" ) ); //set build command


		System.out.println("request setted");

        Invoker invoker = new DefaultInvoker();
      
        
		String M2_HOME = System.getenv("/usr/share/maven");

		if (M2_HOME == null)
			M2_HOME = "/usr/share/maven";
		
		
        invoker.setMavenHome(new File(M2_HOME)); //need to set the maven home
		System.out.println("hasdgds");

        invoker.execute( request ); //build the project


    }

    /**
	 * 
	 * @param signal
	 * 
	 * 
     * @throws NoFilepatternException 
     * @throws WrongRepositoryStateException 
     * @throws JGitInternalException 
     * @throws ConcurrentRefUpdateException 
     * @throws NoMessageException 
     * @throws NoHeadException 
     * @throws InvalidRemoteException 
     * @throws IOException 
	 */
    public void push(Boolean signal) throws NoFilepatternException,
    			NoHeadException, NoMessageException, ConcurrentRefUpdateException, JGitInternalException,
    			WrongRepositoryStateException, InvalidRemoteException, IOException 
    {

        if(signal){

            // add
            DirCache index = git.add().addFilepattern( "." ).call();

            // commit
            commit = git.commit().setMessage( "Build Success!" ).call();
            // push to remote:
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));

            pushCommand.call();
        }

        Runtime.getRuntime().exec("rm -r /tmp/" + directory ); //remove directory
    }

    /**
	 * 
	 * @return
	 */
    public String getCommitID(){

        String commitId = commit.getId().toString();
        String writeToJson = commitId.split(" ")[1];

        return writeToJson;
    }
}
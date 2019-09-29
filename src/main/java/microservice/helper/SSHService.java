package microservice.helper;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;

import static microservice.helper.SeleniumHelper.printMethodName;

public class SSHService {
    private final String hostname;
    private final String username;
    private final String password;
    private final File keyfile;
    private final String keyPassphrase;

    private static final Logger log = LoggerFactory.getLogger(SSHService.class);

    /* USAGE: Two options for authentication:
     * Password based authentication:
     * SSHService ssh = new SSHService("192.168.50.4", "vagrant", "vagrant",new File(""),"");
     *
     * SSH key based authentication:
     * SSHService ssh = new SSHService("192.168.50.4", "vagrant", "",new File("/Users/jariheikkila/.ssh/id_rsa"),"");
     */

    public SSHService(String hostname, String username, String password, File keyfile, String keyPassphrase) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.keyfile = keyfile;
        this.keyPassphrase = keyPassphrase;

    }

    public String executeCommand(String authenticationMethod, String command) {
        log.info(printMethodName());

        Connection connection = getConnectionBasedOnAuthenticationMethod(authenticationMethod);

        String lines = new String();

        try{
            Session session = connection.openSession();
            session.execCommand(command);
            InputStream stdout = new StreamGobbler(session.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while (true){
                String line =  br.readLine();
                if (line == null)
                    break;
                lines = lines +line;
            }
            session.close();
            log.info("Successfully executed command: "+ command + " to host " +hostname);

        }
        catch (IOException e){
            e.printStackTrace(System.err);
            System.exit(2);
        } finally {
            connection.close();
        }
        return lines;
    }


    public String writeToShell(String authenticationMethod, String command) {
        log.info(printMethodName());

        Connection connection = getConnectionBasedOnAuthenticationMethod(authenticationMethod);

        String lines = new String();
        try{
            Session session = connection.openSession();
            session.requestDumbPTY();
            session.startShell();
            PrintWriter out = new PrintWriter(session.getStdin());
            out.println(command+";");
            out.println("exit\n");
            out.flush();
            out.close();
            InputStream stdout = new StreamGobbler(session.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while (true){
                String line =  br.readLine();
                if (line == null)
                    break;
                lines = lines +"\n"+ line;
//                log.info(line);
            }
            session.close();

        }
        catch (IOException e){
            e.printStackTrace(System.err);
            System.exit(2);
        } finally {
            connection.close();
        }
        return lines;
    }


    /*Download file from server, example:
    sshService.downloadFile("passwordAuth","/home/omaeladm/xstartup","/Users/jaheikki");
    Suggesting still to use downloadFiles method*/
    public void downloadFile(String authenticationMethod, String remoteFile,String localFolder) {
        log.info(printMethodName());

        Connection connection = getConnectionBasedOnAuthenticationMethod(authenticationMethod);

        SCPClient client=new SCPClient(connection);
        try {
            client.get(remoteFile, localFolder);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file: " + remoteFile + " to " + localFolder + " from " + connection.getHostname(), e);
        } finally {
            log.info("Successfully downloaded file: "+ remoteFile + " to folder " + localFolder);
            connection.close();
        }
    }

    /**Download file or files, with default permision: 0600, examples
    *String localFolder = "/Users/jaheikki";
    *downloadFiles("passwordAuth",localFolder, "test1.txt");
    *downloadFiles("passwordAuth",localFolder, "test1.txt", "test2.txt");
    *downloadFiles("passwordAuth",localFolder, "test1.txt", "test2.txt", "test3.txt");
    *String[] files = new String[] {"test1.txt", "test2.txt", "test3.txt"};
    downloadFiles("passwordAuth",localFolder, files);
    * Note: "..." notation creates array automatically*/
    public void downloadFiles(String authenticationMethod, String localFolder,String... remoteFiles) {
        log.info(printMethodName());

        Connection connection = getConnectionBasedOnAuthenticationMethod(authenticationMethod);

        SCPClient client=new SCPClient(connection);
        try {
            client.get(remoteFiles, localFolder);
        } catch (IOException e) {
            log.info("Tried to download following files:");
            Arrays.asList(remoteFiles).stream().forEach(s -> log.info(s));
            throw new RuntimeException("Failed to download files to " + localFolder + " from " + connection.getHostname(), e);
        } finally {
            log.info("Successfully downloaded files: "+ remoteFiles + " to folder " + localFolder);
            connection.close();
        }
        log.info("Successfully downloaded following files to "+ localFolder + " from " + connection.getHostname());
        Arrays.asList(remoteFiles).stream().forEach(s -> log.info(s));

    }

    /*Upload single file to server allowing to rename transferred files. Otherwise proposing to use uploadFilesWithDefaultPermission methods*/
    public void uploadFile(String authenticationMethod, String localFile,String remoteFile,String remoteFolder,String permission) {
        log.info(printMethodName());

        Connection connection = getConnectionBasedOnAuthenticationMethod(authenticationMethod);

        SCPClient client=new SCPClient(connection);
        try {
            client.put(localFile,remoteFile,remoteFolder,permission);
            log.info("Successfully uploaded file: "+ localFile + " to folder " + remoteFolder + " as "+remoteFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + localFile + " to " + remoteFolder + " to " + connection.getHostname(), e);
        } finally {
            connection.close();
        }
    }

    /**Upload file or files, with default permision: 0600, examples
    *uploadFilesWithDefaultPermission("sshKeyAuth",remoteFolder, "test1.txt");
    *uploadFilesWithDefaultPermission("sshKeyAuth",remoteFolder, "test1.txt", "test2.txt");
    *uploadFilesWithDefaultPermission("sshKeyAuth",remoteFolder, "test1.txt", "test2.txt", "test3.txt");
    *String[] files = new String[] {"test1.txt", "test2.txt", "test3.txt"};
    *uploadFilesWithDefaultPermission("sshKeyAuth",remoteFolder, files);
    * Note: "..." notation creates array automatically*/
    public void uploadFilesWithDefaultPermission(String authenticationMethod, String remoteFolder, String... localFiles) {
        log.info(printMethodName());

        Connection connection = getConnectionBasedOnAuthenticationMethod(authenticationMethod);

        SCPClient client=new SCPClient(connection);
        try {
            client.put(localFiles,remoteFolder);
        } catch (IOException e) {
            log.info("Tried to upload following files:");
            Arrays.asList(localFiles).forEach(s -> log.info(s));
            throw new RuntimeException("Failed to upload files to " + remoteFolder + " to " + connection.getHostname(), e);
        } finally {
            connection.close();
        }
        log.info("Successfully uploaded following files to "+ remoteFolder + " to " + connection.getHostname());
        Arrays.asList(localFiles).forEach(s -> log.info(s));
    }

    /**Upload file or files, with custom permision, examples
    *String remoteFolder = "/foo/bar";
    *uploadFilesWithDefaultPermission("sshKeyAuth",remoteFolder, "0755" ,"test1.txt");
    *uploadFilesWithDefaultPermission("sshLKeyAuth",remoteFolder, "0755", "test1.txt", "test2.txt");
    *uploadFilesWithDefaultPermission("sshKeyAuth",remoteFolder, "0755", "test1.txt", "test2.txt", "test3.txt");
    *String[] files = new String[] {"test1.txt", "test2.txt", "test3.txt"};
    *uploadFilesWithDefaultPermission("sshKeyAuth",remoteFolder, "0755", files);*/
    public void uploadFiles(String authenticationMethod, String remoteFolder, String permission, String... localFiles) {
        log.info(printMethodName());

        Connection connection = getConnectionBasedOnAuthenticationMethod(authenticationMethod);

        SCPClient client=new SCPClient(connection);
        try {
            client.put(localFiles,remoteFolder,permission);
        } catch (IOException e) {
            log.info("Tried to upload following files:");
            Arrays.asList(localFiles).stream().forEach(s -> log.info(s));
            throw new RuntimeException("Failed to upload files to " + remoteFolder + " to " + connection.getHostname(), e);

        } finally {
            connection.close();
        }
        log.info("Successfully uploaded following files to "+ remoteFolder + " to " + connection.getHostname());
        Arrays.asList(localFiles).stream().forEach(s -> log.info(s));

    }

    private Connection createConnectionAndLogIn() {
        Connection connection = new Connection(hostname);
        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(username,password);
            if (!isAuthenticated){
                throw new IOException("Authentication failed");
            }
            return connection;
        }
        catch (IOException e){
            e.printStackTrace(System.err);
            System.exit(2);
        }
        throw new RuntimeException("Failed to create password authenticated SSH connection to:" + hostname);
    }

    private Connection createConnectionAndLogInWithSSHKeys() {
        Connection connection = new Connection(hostname);
        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPublicKey(username, keyfile, keyPassphrase);
            if (!isAuthenticated){
                throw new IOException("Authentication failed");
            }
            return connection;
        }
        catch (IOException e){
            e.printStackTrace(System.err);
            System.exit(2);
        }
        throw new RuntimeException("Failed to create SSH key authenticated connection to:" + hostname);
    }

    private Connection getConnectionBasedOnAuthenticationMethod(String authenticationMethod) {
        Connection connection;

        switch (authenticationMethod) {
            case "sshKeyAuth":
                connection = createConnectionAndLogInWithSSHKeys();
                return connection;
            case "passwordAuth":
                connection = createConnectionAndLogIn();
                return connection;
            default:
                throw new IllegalArgumentException("Invalid authentication method: " + authenticationMethod);
        }
    }

}

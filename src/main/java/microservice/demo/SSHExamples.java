package microservice.demo;

import com.jcraft.jsch.SftpException;
import microservice.helper.SFTPUpload;
import microservice.helper.SSHService;
import org.apache.log4j.Logger;

import java.io.File;

import static microservice.helper.SeleniumHelper.printMethodName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SSHExamples {

    static Logger log = Logger.getLogger(
            SSHExamples.class.getName());

    public void scpExample() {

        //SSH key based authentication
        SSHService ssh = new SSHService("192.168.50.4", "vagrant", "", new File("/Users/jariheikkila/.ssh/id_rsa"), "");

        //Password based authentication
        //SSHService ssh = new SSHService("192.168.50.4", "vagrant", "vagrant",new File(""),"");

        String localFolder = "/Users/jariheikkila/Downloads/temp/";
        String remoteFolder = "/home/vagrant/";
        String[] remotefiles = new String[]{remoteFolder + "test1-remote.txt", remoteFolder + "test2-remote.txt", remoteFolder + "test3-remote.txt"};
        String[] localfiles = new String[]{localFolder + "test1-local.txt", localFolder + "test2-local.txt", localFolder + "test3-local.txt"};

        String response = ssh.writeToShell("sshKeyAuth", "ifconfig");
        //String response = ssh.writeToShell("passwordAuth", "ifconfig");

        System.out.println("resp: "+response);

        //delete remote files from local folder
        final File folder = new File(localFolder);
            for(File f: folder.listFiles())
                if(f.getName().endsWith("-remote.txt"))
                f.delete();

        //delete local files from remote folder
        String cmd = "rm "+remoteFolder + "test*-local.txt";
            ssh.executeCommand("sshKeyAuth",cmd);
        //ssh.executeCommand("passwordAuth",cmd);

        //upload file and rename
            ssh.uploadFile("sshKeyAuth",localFolder+"iloveme-local.txt","iloveme-remote.txt","/home/vagrant","0755");
        //ssh.uploadFile("passwordAuth",localFolder+"iloveme-local.txt","iloveme-remote.txt","/home/vagrant","0755");

        //upload files
            ssh.uploadFiles("sshKeyAuth",remoteFolder, "0700", localfiles);
        //ssh.uploadFiles("passwordAuth",remoteFolder, "0700", localfiles);

        //download file
            ssh.downloadFile("sshKeyAuth", remoteFolder+"iloveme-remote.txt", localFolder);
        //ssh.downloadFile("passwordAuth", remoteFolder+"iloveme-remote.txt", localFolder);

        //download files
            ssh.downloadFiles("sshKeyAuth", localFolder, remotefiles);
        //ssh.downloadFiles("passwordAuth", localFolder, remotefiles);

    }

    public void iTransferCSVFilesToSFTPServer() throws SftpException {
        log.info(printMethodName());

        String remoteCSVDir = "/csvdir/csvfiles";
        SFTPUpload sftp = new SFTPUpload("sshuser", "192.168.0.10", 22, "privateKeyFile");

        //delete old folder if exists
        sftp.removeFolderRecursively(remoteCSVDir);

        //create new directory for csv files
        sftp.createDirectory(remoteCSVDir);

        //upload new files
        sftp.uploadFiles(remoteCSVDir, "src/test/resources/mycsvfile1",
                "src/test/resources/mycsvfile2"
        );

        sftp.exit();
    }
}

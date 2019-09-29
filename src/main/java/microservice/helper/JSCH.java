package microservice.helper;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import static microservice.helper.SeleniumHelper.printMethodName;

public class JSCH {
    private static final Logger log = LoggerFactory.getLogger(JSCH.class);

    /* Use example:
     * resp = JSCH.executeShellCmdSSHAndReturnResp(<host>, <user>,<password or>, <private key file>, sshCmd);
     * log.info(resp.entrySet().stream().findFirst());
    */
    public static HashMap<Integer, String> executeShellCmdSSHAndReturnResp(String host, String user, String password, String privateKey, String command){
        log.info(printMethodName());

        try{

            log.info("Running command started, wait...");
            log.info("Command: "+command);

            String lines = new String();
            HashMap<Integer, String> hmap = new HashMap();

            JSch jsch = new JSch();

            if (!privateKey.equals("")) jsch.addIdentity(privateKey);

            Session session = jsch.getSession(user, host, 22);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            if (!password.equals("")) session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream input = channel.getInputStream();
            channel.connect();

            log.info("SSH channel Connected to machine " + host);

            int exitStatus = 0;
            try{
                InputStreamReader inputReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputReader);
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    log.info(line);
                    lines = lines +"\n"+ line;
                }
                exitStatus = channel.getExitStatus();
                bufferedReader.close();
                inputReader.close();


            }catch(IOException ex){
                ex.printStackTrace();
            }

            channel.disconnect();
            session.disconnect();
            hmap.put(exitStatus, lines);
            return hmap;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


}


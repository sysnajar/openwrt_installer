/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

import java.io.IOException;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

/** This examples demonstrates how a remote command can be executed. */
public class Test {

    public static void main(String... args)
            throws IOException {
        final SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        
        ssh.addHostKeyVerifier(new HostKeyVerifier(){
            @Override
            public boolean verify(String string, int i, PublicKey pk) {
                return true;
            }
        
        });
      

        //ssh.connect("sm.hjkl.ninja");
        ssh.connect("localhost");
        try {
            //ssh.authPublickey(System.getProperty("user.name"));
            ssh.authPassword(args[0], args[1]);
            final Session session = ssh.startSession();
            try {
                final Command cmd = session.exec("ping -c 1 google.com");
                System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
                cmd.join(5, TimeUnit.SECONDS);
                System.out.println("\n** exit status: " + cmd.getExitStatus());
            } finally {
                session.close();
            }
        } finally {
            ssh.disconnect();
        }
    }
 

}

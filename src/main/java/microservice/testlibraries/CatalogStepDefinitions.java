package microservice.testlibraries;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import microservice.common.MsConstants;
import microservice.helper.SSHService;
import microservice.msrest.MsCatalogRest;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class CatalogStepDefinitions {

    private static JsonNode catalogItem;

    @Given("^catalog item exists at the database$")
    public void catalogItemExistsAtTheDatabase() throws InterruptedException {

//        //SSH key based authentication
//        SSHService ssh = new SSHService("192.168.50.4", "vagrant", "",new File("/Users/jariheikkila/.ssh/id_rsa"),"");
//
//        //Password based authentication
//        //SSHService ssh = new SSHService("192.168.50.4", "vagrant", "vagrant",new File(""),"");
//
//        String localFolder = "/Users/jariheikkila/Downloads/temp/";
//        String remoteFolder = "/home/vagrant/";
//        String[] remotefiles = new String[] {remoteFolder+"test1-remote.txt", remoteFolder+"test2-remote.txt", remoteFolder+"test3-remote.txt"};
//        String[] localfiles = new String[] {localFolder+"test1-local.txt", localFolder+"test2-local.txt", localFolder+"test3-local.txt"};
//
//        String response = ssh.writeToShell("sshKeyAuth", "ifconfig");
//        //String response = ssh.writeToShell("passwordAuth", "ifconfig");
//        System.out.println("resp: "+response);
//
//        //delete remote files from local folder
//        final File folder = new File(localFolder);
//        for(File f: folder.listFiles())
//            if(f.getName().endsWith("-remote.txt"))
//                f.delete();
//
//        //delete local files from remote folder
//        String cmd = "rm "+remoteFolder + "test*-local.txt";
//        ssh.executeCommand("sshKeyAuth",cmd);
//        //ssh.executeCommand("passwordAuth",cmd);
//
//        //upload file and rename
//        ssh.uploadFile("sshKeyAuth",localFolder+"iloveme-local.txt","iloveme-remote.txt","/home/vagrant","0755");
//        //ssh.uploadFile("passwordAuth",localFolder+"iloveme-local.txt","iloveme-remote.txt","/home/vagrant","0755");
//
//        //upload files
//        ssh.uploadFiles("sshKeyAuth",remoteFolder, "0700", localfiles);
//        //ssh.uploadFiles("passwordAuth",remoteFolder, "0700", localfiles);
//
//        //download file
//        ssh.downloadFile("sshKeyAuth", remoteFolder+"iloveme-remote.txt", localFolder);
//        //ssh.downloadFile("passwordAuth", remoteFolder+"iloveme-remote.txt", localFolder);
//
//        //download files
//        ssh.downloadFiles("sshKeyAuth", localFolder, remotefiles);
//        //ssh.downloadFiles("passwordAuth", localFolder, remotefiles);
    }

    @When("i get the catalog item from rest")
    public void iGetTheCatalogItemFromRest() {


        //System.out.println("Sleeping due to check if services are up...");
        //Selenide.sleep(30000);
        //catalogItem = MsCatalogRest.getSingleCatalogItemWithId(MsConstants.catalogServiceUrl, MsConstants.catalogURI, "2");
        catalogItem = MsCatalogRest.waitForGetSingleCatalogItemWithId(60,5,MsConstants.catalogServiceUrl, MsConstants.catalogURI, "2");
    }

    @Then("catalog item name should be (.*)")
    public void catalogItemNameShouldBe(String catalogItemName) {

        assertThat((catalogItem.get("name").asText()), is(catalogItemName));
        System.out.println("Catalog item name was OK: "+catalogItemName);

    }

    @And("catalog item price should be (.*)")
    public void catalogItemPriceShouldBe(String catalogItemPrice) {

        assertThat((catalogItem.get("price").asText()), is(catalogItemPrice));
        System.out.println("Catalog item price was OK: "+catalogItemPrice);

    }
}



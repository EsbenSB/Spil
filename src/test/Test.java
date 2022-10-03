package test;

import utils.PackageService;

import java.util.HashMap;

public class Test {
  public static void main(String[] args) {
    HashMap<String, String> dataOnServer = new HashMap<>();
    dataOnServer.put("task", "init");
    dataOnServer.put("client_id", "123123");
    System.out.printf("Data on Server: %s%n", dataOnServer);

    String queryOnServer = PackageService.constructQuery(dataOnServer);
    System.out.printf("Query on Server: %s%n", queryOnServer);

    // send query

    String queryOnClient = queryOnServer;
    System.out.printf("Query on client: %s%n", queryOnClient);

    HashMap<String, String> dataOnClient = PackageService.deconstructQuery(queryOnClient);
    System.out.printf("Data on client: %s%n", dataOnClient);

    // PROCESS DATA
    if (dataOnClient.get("task").equals("init")) {
      String id = dataOnClient.get("client_id");
      System.out.printf("Received query from server with my new ID!!!! %s%n", id);
    } else if (dataOnClient.get("task").equals("move")) {

    }
   }
}

package server.network;

import java.util.HashMap;

public interface ServerInterface {
  void processData(CommunicationService commService, HashMap<String, String> data);
  void commDisconnected(CommunicationService commService);
}

package utils;

import java.util.HashMap;

public interface NetworkUser {
  void processData(HashMap<String, String> data);
  void commDisconnected(CommunicationService commService);
}

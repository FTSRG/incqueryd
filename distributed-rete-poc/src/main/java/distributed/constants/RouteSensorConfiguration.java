package distributed.constants;

import java.util.HashMap;
import java.util.Map;

import com.typesafe.config.Config;

public class RouteSensorConfiguration {

	public static String setActorIp(Config config, String actorName) {
		String actorPath = config.getString("akka.actor.deployment.\"/" + actorName + "\".remote");
		String actorIp = actorPath.replace("akka://ReteNet@", "").replace(":2552", "");
		map.put(actorName, actorIp);
		return actorIp;
	}
	
	public static String getActorPath(String actorName) {
		return "akka://ReteNet@" + map.get(actorName) + ":2552/remote/ReteNet@127.0.0.1:2554/user/" + actorName;
	}
	
	// <ActorName, IP> map
	protected static Map<String, String> map = new HashMap<>();

}

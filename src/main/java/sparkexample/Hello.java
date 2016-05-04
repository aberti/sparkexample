package sparkexample;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import static spark.Spark.get;

public class Hello {

    static final Logger LOGGER = Logger.getLogger(Hello.class.getName());

    static final String REDIS_ADDR = "redis";
    static final int REDIS_PORT = 6379;

    private static boolean connected = false;
    static Socket s;

    private static Jedis jedis = null;

    public static void main(String[] args) {

        get("/", (req, res) -> {
            StringBuilder out = new StringBuilder();
            out.append("host: ");
            out.append(getHostName());
            out.append("<br/>");
            out.append("hello world (called more than: ");
            out.append( getCounter() );
            out.append(" times )");
            return out.toString();
        });

    }

    static String getHostName(){
        Map<String, String> env = System.getenv();
        if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "brak env HOSTNAME";
    }

    static void conectToRedis() {
        if (hostAvailabilityCheck()) {
            jedis = new Jedis(REDIS_ADDR, REDIS_PORT);
            jedis.connect();
        }
    }

    static String getCounter() {

        if (jedis == null) {
            conectToRedis();
        }

        if (jedis != null) {
            String counter = jedis.get("counter");
            if (counter == null) {
                counter = "1";
            }
            String c = jedis.incr("counter").toString();
            LOGGER.info(c);
            return c;
        } else {
            LOGGER.error("NO REDIS");
            return "---NO REDIS---";
        }
    }


    static boolean hostAvailabilityCheck() {

        boolean available = true;
        try {
            if (connected == false) {
                (s = new Socket(REDIS_ADDR, REDIS_PORT)).close();
            }
        } catch (UnknownHostException e) { // unknown host
            available = false;
            s = null;
        } catch (IOException e) { // io exception, service probably not running
            available = false;
            s = null;
        } catch (NullPointerException e) {
            available = false;
            s = null;
        }


        return available;
    }

}
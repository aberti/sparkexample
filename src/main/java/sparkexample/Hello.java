package sparkexample;

import java.util.Map;

import static spark.Spark.get;

public class Hello {

    public static void main(String[] args) {

        get("/", (req, res) -> {
            return "hello world - 4";
        });

        get("/envs", (req, res) -> {
            Map<String, String> env = System.getenv();
            if (env.containsKey("HOSTNAME"))
                return env.get("HOSTNAME");
            else
                return "brak env HOSTNAME";
        });

    }

}
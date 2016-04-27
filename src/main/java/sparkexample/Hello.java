package sparkexample;

import java.util.Map;

import static spark.Spark.get;

public class Hello {

    public static void main(String[] args) {

        get("/", (req, res) -> {
            return "hello from sparkjava.com";
        });

        get("/envs", (req, res) -> {
            StringBuilder buf = new StringBuilder();
            Map<String, String> env = System.getenv();
            for (String envName : env.keySet()) {
                buf.append(envName + " : " + env.get(envName) + "<br/>");
            }
            return buf.toString();
        });
    }

}
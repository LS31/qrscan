package nl.ls31.qrscan;

/**
 * It's a dirty trick to be able to create a runnable jar with all the required JavaFX dependencies. To do so, we need
 * to use a launcher class that doesn't extend from Application.
 *
 * @see <a href="https://mail.openjdk.java.net/pipermail/openjfx-dev/2018-June/021977.html">discussion at OpenJDK</a>
 */
public class Launcher {

    public static void main(String[] args) {
        App.main(args);
    }
}
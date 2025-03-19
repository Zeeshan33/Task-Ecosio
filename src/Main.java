import java.time.LocalDateTime;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        LocalDateTime startTime = LocalDateTime.now();
        UrlTask urlTask = new UrlTask("https://www.ecosio.com/");
        Set<URLInformationModel> collectedUrls = urlTask.startProcess();

        collectedUrls.stream().sorted().forEach(collectedUrl -> System.out.println(collectedUrl.getUrl()+ " = "+ collectedUrl.getPageName()));
        System.out.println("Start Time =" + startTime);
        System.out.println("End Time =" + LocalDateTime.now());
    }
}
import java.net.URL;
import java.util.Set;
import java.util.concurrent.*;

public class UrlTask {

    public static final String HOME_PAGE = "Home Page";
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static final Set<URLInformationModel> allCollectedUrls = ConcurrentHashMap.newKeySet();

    public static final ConcurrentHashMap<String, Boolean> visitedUrls = new ConcurrentHashMap<>();

    public static final BlockingQueue<String> unVisitedUrls = new LinkedBlockingQueue<>();

    public final String baseDomain;

    public UrlTask(String baseUrl) {
        baseDomain = extractDomain(baseUrl);
        visitedUrls.put(baseUrl, true);
        unVisitedUrls.offer(baseUrl);
        allCollectedUrls.add(new URLInformationModel(HOME_PAGE, baseUrl));
    }

    public Set<URLInformationModel> startProcess() throws InterruptedException {
        while (!unVisitedUrls.isEmpty()) {
            String url = unVisitedUrls.poll();

            this.startExecutorService(url);
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);

        return allCollectedUrls;
    }

    public void processUrl(String url) throws InterruptedException {
        visitedUrls.put(url, true);

        PageContentFinder pageContentFinder = new PageContentFinder();
        String pageContent = pageContentFinder.getPageContent(url);

        UrlCollector urlCollector = new UrlCollector();
        urlCollector.getUrlsFromPageContent(pageContent, baseDomain);

        this.startExecutorService(url);
    }

    public void startExecutorService(String url) {
        executorService.submit(() -> {
            try {
                this.processUrl(url);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }

    private String extractDomain(String url) {
        try {
            URL urlObj = new URL(url);

            String host = urlObj.getHost();
            String[] parts = host.split("\\.");

            if (parts.length >= 2) {
                return parts[parts.length - 2] + "." + parts[parts.length - 1];
            } else {
                return host;
            }
        } catch (Exception e) {
            System.out.println("Invalid URL " + url);
            return null;
        }
    }
}

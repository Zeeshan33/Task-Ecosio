import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlCollector {

    public void getUrlsFromPageContent(String pageContent, String domain) throws InterruptedException {
        String regex = "(https?|http?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pageContent);

        while (matcher.find())
        {
            try {
                if(validateDomain(matcher.group(), domain)
                        && !isUnwantedFile(matcher.group())
                        && !UrlTask.visitedUrls.containsKey(matcher.group())
                        && !UrlTask.unVisitedUrls.contains(matcher.group())) {

                    String collectedUrl = matcher.group();

                    UrlTask.unVisitedUrls.put(collectedUrl);
                    UrlTask.allCollectedUrls.add(new URLInformationModel(getPageName(collectedUrl, domain), collectedUrl));
                }
            } catch (Exception exception) {
                System.out.println("Exception while collecting URLs" + exception.getMessage());
            }
        }
    }

    private String getPageName(String collectedUrl, String domain) {
        String [] pageName = collectedUrl.split(domain);
        if(pageName.length > 0) {
            return pageName[1];
        }
        return null;
    }

    private boolean isUnwantedFile(String url) {
        String[] blockedExtensions = {".js", ".css", ".pdf", ".jpg", ".jpeg", ".exe", ".xml", ".png", ".gif", ".svg", ".mp4", ".mp3", ".zip", ".tar", ".gz", ".rar"};
        for (String ext : blockedExtensions) {
            if (url.toLowerCase().contains(ext)) {
                return true;
            }
        }

        return false;
    }

    public Boolean validateDomain(String url, String domain) {
        String currentUrlDomain = extractDomain(url);

        return currentUrlDomain.equals(domain);
    }

    public synchronized String extractDomain(String url) {
        try {
            URL urlObj = new URL(url);

            String host = urlObj.getHost();
            String[] parts = host.split("\\.");

            if (parts.length >= 2) {
                return parts[parts.length - 2] + "." + parts[parts.length - 1];
            } else {
                return host;
            }
        } catch (Exception exception) {
            System.out.println("Invalid URL " + url);
            return null;
        }
    }
}

import java.util.Objects;

public class URLInformationModel implements Comparable<URLInformationModel> {

    private String pageName;

    private String url;

    public URLInformationModel(String pageName, String url) {
        this.url = url;
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URLInformationModel obj = (URLInformationModel) o;
        return Objects.equals(url, obj.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public int compareTo(URLInformationModel object) {
        if (object == null) return 1;
        return Objects.compare(this.pageName, object.pageName, String::compareTo);
    }
}

package in.edureal.opennews;

class ListItem {

    private String title;
    private String name;
    private String url;
    private String imageUrl;

    ListItem(String title, String name, String url, String imageUrl) {
        this.title = title;
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    String getTitle() {
        return title;
    }

    String getName() {
        return name;
    }

    String getUrl() {
        return url;
    }

    String getImageUrl() {
        return imageUrl;
    }
}

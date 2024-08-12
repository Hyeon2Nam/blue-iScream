package emoticon;

public class EmoticonItem {
    private String text;
    private String imagePath;

    public EmoticonItem(String text, String imagePath) {
        this.text = text;
        this.imagePath = imagePath;
    }

    public String getText() {
        return text;
    }

    public String getImagePath() {
        return imagePath;
    }
}
